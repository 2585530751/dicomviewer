package com.ncu.imagesystem.controller;

import cn.hutool.json.JSONObject;
import com.ncu.imagesystem.entity.SysGroupPermRelationEntity;
import com.ncu.imagesystem.entity.SysPermissionEntity;
import com.ncu.imagesystem.entity.SysUserPermRelationEntity;
import com.ncu.imagesystem.enums.ResultCode;
import com.ncu.imagesystem.service.SysGroupPermRelationService;
import com.ncu.imagesystem.service.SysPermissionService;
import com.ncu.imagesystem.service.SysUserPermRelationService;
import com.ncu.imagesystem.tools.Result;
import com.ncu.imagesystem.tools.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Api("系统权限信息controller")
@RestController
@RequestMapping("/permission")
public class SysPermissionController {

    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private SysUserPermRelationService sysUserPermRelationService;
    @Autowired
    private SysGroupPermRelationService sysGroupPermRelationService;

    @ApiOperation("新增一条权限信息接口")
    @PostMapping("/addOne")
    @PreAuthorize("@vps.hasPermission('admin')")
    public Result addOne(@RequestBody SysPermissionEntity sysPermissionEntity){
        sysPermissionEntity.setCreateTime(new Date());
        boolean save = sysPermissionService.save(sysPermissionEntity);
        if(save){
            return Result.success("权限信息添加成功！");
        }else {
            return Result.failed("权限信息添加失败！");
        }
    }

    @ApiOperation("根据permissionId查询权限信息接口")
    @GetMapping("/getPermissionById")
    @PreAuthorize("@vps.hasPermission('admin')")
    public Result getPermissionById(Integer permId){
        SysPermissionEntity sysPermissionEntity = sysPermissionService.getById(permId);
        if(sysPermissionEntity != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), sysPermissionEntity);
        }else {
            return Result.failed("权限信息获取失败！");
        }
    }

    @ApiOperation("根据permissionId更新权限信息接口")
    @PostMapping("/updatePermissionById")
    @PreAuthorize("@vps.hasPermission('admin')")
    public Result updatePermissionById(@RequestBody SysPermissionEntity sysPermissionEntity){
        boolean b = sysPermissionService.updateById(sysPermissionEntity);
        if(b){
            return Result.success("权限信息更新成功！");
        }else {
            return Result.failed("权限信息更新失败！");
        }
    }

    @ApiOperation("给用户增加权限接口")
    @PostMapping("/addPermissionForUser")
    public Result addPermissionForUser(@RequestBody SysUserPermRelationEntity sysUserPermRelation){
        boolean save = sysUserPermRelationService.save(sysUserPermRelation);
        if(save){
            return Result.success("用户权限信息增加成功！");
        }else {
            return Result.failed("用户权限信息增加失败！");
        }
    }

    @ApiOperation("根据userId查询用户权限信息接口")
    @GetMapping("/getPermissionByUserId")
    public Result getPermissionByUserId(Integer userId){
        List<SysPermissionEntity> sysPermissionEntity = sysPermissionService.getUserAllPermissionsByUserId(userId);
        if(sysPermissionEntity != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), sysPermissionEntity);
        }else {
            return Result.failed("权限信息获取失败！");
        }
    }

    @ApiOperation("给组增加权限接口")
    @PostMapping("/addPermissionForGroup")
    public Result addPermissionForGroup(@RequestBody SysGroupPermRelationEntity sysGroupPermRelation){
        boolean save = sysGroupPermRelationService.save(sysGroupPermRelation);
        if(save){
            return Result.success("组权限信息增加成功！");
        }else {
            return Result.failed("组权限信息增加失败！");
        }
    }


    @ApiOperation("根据当前用户查询用户权限信息接口")
    @GetMapping("/getPermissionByCurrentUserId")
    public Result getPermissionByCurrentUserId(){
        JSONObject userJson = UserUtils.getUserJsonObject();
        Integer userId = userJson.getInt("userId");
        List<SysPermissionEntity> sysPermissionEntity = sysPermissionService.getUserAllPermissionsByUserId(userId);
        if(sysPermissionEntity != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), sysPermissionEntity);
        }else {
            return Result.failed("权限信息获取失败！");
        }
    }
}
