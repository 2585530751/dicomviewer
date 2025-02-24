package com.ncu.imagesystem.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ncu.imagesystem.dto.LoginReq;
import com.ncu.imagesystem.dto.RefreshToken;
import com.ncu.imagesystem.dto.TokenContent;
import com.ncu.imagesystem.dto.TokenResult;
import com.ncu.imagesystem.entity.*;
import com.ncu.imagesystem.enums.ResultCode;
import com.ncu.imagesystem.service.*;
import com.ncu.imagesystem.tools.FileUtil;
import com.ncu.imagesystem.tools.MyConstant;
import com.ncu.imagesystem.tools.RedisUtils;
import com.ncu.imagesystem.tools.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Api("系统用户controller")
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private Environment environment;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private SysUserPermRelationService sysUserPermRelationService;
    @Autowired
    private SysUserGroupRelationService sysUserGroupRelationService;


    @ApiOperation("用户登录接口")
    @PostMapping("/login")
    public Result login(@RequestBody LoginReq req) {

        //System.out.println(bCryptPasswordEncoder.encode(req.getPassword()));
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(req.getAccount(), req.getPassword());
        RedisUtils redisUtils = new RedisUtils(redisTemplate);
        try {
            authenticationManager.authenticate(authenticationToken);
        }catch (AuthenticationException ex){
            return Result.failed("用户校验失败！");
        }
        //上一步没有抛出异常说明认证成功，我们向用户颁发jwt令牌
        String accessTokenEffectiveSecond = MyConstant.ACCESS_EXPIRATION_TIME;
        String refreshTokenEffectiveSecond = MyConstant.REFRESH_EXPIRATION_TIME;
        SysUserEntity user = sysUserService.getUserByAccount(req.getAccount());
        TokenContent accessTokenContent = new TokenContent(user, accessTokenEffectiveSecond);
        List<SysPermissionEntity> permissions = sysPermissionService.getUserAllPermissionsByUserId(user.getUserId());
        List<String> roles = permissions.stream().map(SysPermissionEntity::getPermissionName).collect(Collectors.toList());
        accessTokenContent.setRoles(roles);
        String accessToken = JWT.create()
                .setHeader("tokenType", "accessToken")
                .setPayload(MyConstant.TOKEN_PAYLOAD, accessTokenContent.getJsonStringObjAccess())
                .setPayload(JWTPayload.EXPIRES_AT, new DateTime(accessTokenContent.getExpires()))
                .setKey(MyConstant.JWT_SIGN_KEY.getBytes(StandardCharsets.UTF_8))
                .sign();
        TokenContent refreshTokenContent = new TokenContent(user, refreshTokenEffectiveSecond);
        String refreshToken = JWT.create()
                .setHeader("tokenType", "refreshToken")
                .setPayload(MyConstant.TOKEN_PAYLOAD, refreshTokenContent.getJsonStringObjRefresh())
                .setPayload(JWTPayload.EXPIRES_AT, new DateTime(refreshTokenContent.getExpires()))
                .setKey(MyConstant.JWT_SIGN_KEY.getBytes(StandardCharsets.UTF_8))
                .sign();

        //redis键值对
        MD5 md5 = new MD5(MyConstant.MD5_SALINITY);
        redisUtils.set(md5.digestHex(refreshToken),refreshToken, Long.parseLong(refreshTokenEffectiveSecond), TimeUnit.SECONDS);
        redisUtils.set(md5.digestHex(user.getUserId().toString()),user);
        return Result.success("登录成功！", new TokenResult(accessTokenContent, accessToken, refreshToken));
    }

    @ApiOperation("用户注册接口")
    @PostMapping("/register")
    public Result register(@RequestBody LoginReq req){
        String account = req.getAccount();
        if(StringUtils.isBlank(account)){
            return Result.failed("账户名为空！");
        }
        String password = req.getPassword();
        if(StringUtils.isBlank(password)){
            return Result.failed("密码为空！");
        }
        SysUserEntity user = new SysUserEntity();
        user.setAccount(account);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPhoneNumber(req.getPhoneNumber());
        user.setCreateTime(new Date());
        boolean save = sysUserService.save(user);
        if(!StringUtils.isBlank(req.getPermissionName())){
            if(!"patient".equals(req.getPermissionName())){
                DoctorEntity doctor = new DoctorEntity();
                doctor.setPhoneNumber(user.getPhoneNumber());
                doctor.setUserId(user.getUserId());
                doctorService.save(doctor);
            }
            setPermission(user.getUserId(), req.getPermissionName());
            setGroup(user.getUserId(), req.getPermissionName());
        }
        if(save){
            return Result.success("注册成功");
        }else{
            return Result.failed("注册失败！");
        }
    }

    private void setPermission(Integer userId, String permissionName){
        List<SysPermissionEntity> permissionList = new ArrayList<>();
        getParentPermissionByName(permissionName, permissionList);
        List<SysUserPermRelationEntity> permRelationList = new ArrayList<>();
        for(SysPermissionEntity permission : permissionList){
            permRelationList.add(new SysUserPermRelationEntity(userId, permission.getPermissionId()));
        }
        sysUserPermRelationService.saveBatch(permRelationList);
    }

    private void getParentPermissionByName(String permissionName, List<SysPermissionEntity> permissionList){
        SysPermissionEntity permission = (SysPermissionEntity)redisTemplate.opsForValue().get(permissionName);
        if(permission!=null && permission.getPermissionParentId() != null && permission.getPermissionParentName() != null){
            getParentPermissionByName(permission.getPermissionParentName(), permissionList);
        }
        permissionList.add(permission);
    }

    private void setGroup(Integer userId, String permissionName){
        SysGroupEntity group = (SysGroupEntity)redisTemplate.opsForHash().get("sysGroups", permissionName);
        if(group != null && group.getGroupId() != null)
            sysUserGroupRelationService.save(new SysUserGroupRelationEntity(group.getGroupId(), userId));
    }

    @ApiOperation("根据手机号码更新密码接口")
    @PostMapping("/updatePasswordByPhoneNumber")
    public Result updatePasswordByPhoneNumber(@RequestBody LoginReq req){
        String phoneNumber = req.getPhoneNumber();
        if(StringUtils.isBlank(phoneNumber)){
            return Result.failed("手机号码为空！");
        }
        String account = req.getAccount();
        if(StringUtils.isBlank(account)){
            return Result.failed("账号为空！");
        }
        String password = req.getPassword();
        if(StringUtils.isBlank(password)){
            return Result.failed("密码为空！");
        }
        LambdaQueryWrapper<SysUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserEntity::getPhoneNumber, phoneNumber);
        wrapper.eq(SysUserEntity::getAccount, account);
        SysUserEntity sysUser = sysUserService.getOne(wrapper);
        if(sysUser != null){
            sysUser.setPassword(bCryptPasswordEncoder.encode(password));
            boolean save = sysUserService.updateById(sysUser);
            if(save){
                return Result.success("密码修改成功！");
            }
        }
        return Result.failed("密码修改失败！");
    }

    @ApiOperation("账户名是否存在接口")
    @PostMapping("/isAccountExisted")
    public Result isAccountExisted(@RequestParam String account){
        if(StringUtils.isBlank(account)){
            return Result.failed("账户名为空！");
        }
        LambdaQueryWrapper<SysUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserEntity::getAccount, account);
        SysUserEntity sysUser = sysUserService.getOne(wrapper);
        if(sysUser == null){
            return Result.success("用户名不存在");
        }
        return Result.failed("用户名已存在");
    }

    @ApiOperation("刷新token接口")
    @PostMapping("/refreshToken")
    public Result refreshToken(@RequestBody RefreshToken token){
        JWT jwt = new JWT(token.getRefreshToken());
        MD5 md5 = new MD5(MyConstant.MD5_SALINITY);
        RedisUtils redisUtils = new RedisUtils(redisTemplate);
        jwt.setKey(MyConstant.JWT_SIGN_KEY.getBytes(StandardCharsets.UTF_8));
        if (!(jwt.verify() && jwt.validate(0)&& redisUtils.get(md5.digestHex(token.getRefreshToken()))!=null)) {
            return Result.failed("刷新token过期，请重新登录！");
        }
        redisUtils.delete(md5.digestHex(token.getRefreshToken()));
        String userInfo = (String) jwt.getPayload(MyConstant.TOKEN_PAYLOAD);
        JSONObject object = new JSONObject(userInfo);
        SysUserEntity user= (SysUserEntity) redisUtils.get(md5.digestHex(object.get("userId").toString()));
        String accessTokenEffectiveSecond = MyConstant.ACCESS_EXPIRATION_TIME;
        String refreshTokenEffectiveSecond = MyConstant.REFRESH_EXPIRATION_TIME;
        TokenContent accessTokenContent = new TokenContent(user, accessTokenEffectiveSecond);
        String accessToken = JWT.create()
                .setHeader("tokenType", "accessToken")
                .setPayload(MyConstant.TOKEN_PAYLOAD, accessTokenContent.getJsonStringObjAccess())
                .setPayload(JWTPayload.EXPIRES_AT, new DateTime(accessTokenContent.getExpires()))
                .setKey(MyConstant.JWT_SIGN_KEY.getBytes(StandardCharsets.UTF_8))
                .sign();
        TokenContent refreshTokenContent = new TokenContent(user, refreshTokenEffectiveSecond);
        String refreshToken = JWT.create()
                .setHeader("tokenType", "refreshToken")
                .setPayload(MyConstant.TOKEN_PAYLOAD, refreshTokenContent.getJsonStringObjRefresh())
                .setPayload(JWTPayload.EXPIRES_AT, new DateTime(refreshTokenContent.getExpires()))
                .setKey(MyConstant.JWT_SIGN_KEY.getBytes(StandardCharsets.UTF_8))
                .sign();
        redisUtils.set(md5.digestHex(refreshToken),refreshToken, Long.parseLong(refreshTokenEffectiveSecond), TimeUnit.SECONDS);
        return Result.success(ResultCode.SUCCESS.getMsg(), new TokenResult(accessTokenContent, accessToken, refreshToken));
    }

    @ApiOperation("更新用户信息接口")
    @PostMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestParam(required = false) String name,
                                         @RequestParam(required = false) String email,
                                         @RequestParam(required = false) String address,
                                         @RequestParam(required = false) String phoneNumber,
                                         @RequestParam(required = false) String userName,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Date birthOfDate,
                                         @RequestParam(required=false) String gender,
                                         @RequestParam(required=false) Float userHeight,
                                         @RequestParam(required=false) Float userWeight,
                                         @RequestParam(required=false) String place,
                                         @RequestParam(required=false) String idCard
                                 ){
        Integer userId = getUserId();
        SysUserEntity user = sysUserService.getById(userId);
        user.setName(name);
        user.setEmail(email);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        user.setUserName(userName);
        user.setBirthOfDate(birthOfDate);
        user.setGender(gender);
        user.setUserHeight(userHeight);
        user.setUserWeight(userWeight);
        user.setPlace(place);
        user.setIdCard(idCard);
        boolean b = sysUserService.updateById(user);
        if(b){
            return Result.success("更新成功!");
        }
        return Result.failed("更新失败！");
    }

    @ApiOperation("更新用户地址接口")
    @PostMapping("/updateUserAddress")
    public Result updateUserAddress(@RequestBody String address){
        Integer userId = getUserId();
        SysUserEntity user = sysUserService.getById(userId);
        user.setAddress(address);
        boolean b = sysUserService.updateById(user);
        if(b){
            return Result.success("更新成功!");
        }
        return Result.failed("更新失败！");
    }


    private static Integer getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String authorization = request.getHeader("Authorization");
        authorization = authorization.replace("Bearer ", "");
        JWT jwt = new JWT(authorization);
        final String content = (String) jwt.getPayload(MyConstant.TOKEN_PAYLOAD);
        JSONObject object = new JSONObject(content);
        Integer userId = object.get("userId", Integer.class);
        return userId;
    }

    @ApiOperation("设置用户头像接口")
    @PostMapping("/setHeadIcon")
    public Result setHeadIcon(@RequestPart("file") MultipartFile file){
        if(file == null) return Result.failed("头像为空！");
        String path = environment.getProperty("web.upload-path") + File.separator + MyConstant.HEAD_ICON_DIR;
        String fileName = FileUtil.saveFile2Path(file, path);
        if(fileName != null){
            Integer userId = getUserId();
            SysUserEntity user = sysUserService.getById(userId);
            user.setHeadIcon(fileName);
            sysUserService.updateById(user);
            String filePath = MyConstant.HEAD_ICON_DIR + "/" + fileName;
            return Result.success("设置用户头像成功!", filePath);
        }
        return Result.failed("设置用户头像失败!");
    }

    @ApiOperation("获取用户头像接口")
    @GetMapping("/getHeadIcon")
    public Result getHeadIcon(){
        Integer userId = getUserId();
        SysUserEntity user = sysUserService.getById(userId);
        String headIcon = user.getHeadIcon();
        if(headIcon != null){
            String filePath = MyConstant.HEAD_ICON_DIR + "/" + headIcon;
            return Result.success(filePath);
        }
        return Result.failed("获取用户头像失败!");
    }


    @ApiOperation("获取用户信息")
    @GetMapping("/getUserInfo")
    public Result getUserInfo(){
        Integer userId = getUserId();
        SysUserEntity user = sysUserService.getById(userId);
        if(user != null){
            user.setPassword(null);
            JSONObject object = new JSONObject(user);
            return Result.success("获取用户信息成功!", object);
        }
        return Result.failed("获取用户信息失败!");
    }

}
