package com.ncu.imagesystem.controller;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.ncu.imagesystem.entity.ModelEntity;
import com.ncu.imagesystem.enums.ResultCode;
import com.ncu.imagesystem.service.ModelService;
import com.ncu.imagesystem.tools.Result;
import com.ncu.imagesystem.tools.FileUtil;
import com.ncu.imagesystem.tools.MyConstant;
import org.springframework.core.env.Environment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.hutool.json.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Api("模型介绍信息表controller")
@RestController
@RequestMapping("/modelInfo")
public class ModelController {
    @Autowired
    private ModelService modelService;
    @Autowired
    private Environment environment;

    @ApiOperation("获取所有的模型信息接口")
    @GetMapping("/getAllModelInfo")
    public Result getAllModelInfo(){
        List<ModelEntity> list = modelService.list();
        if(list != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), list);
        }else{
            return Result.failed("模型信息获取失败！");
        }
    }

    @ApiOperation("获取所有的模型信息接口")
    @GetMapping("/getModelInfoByModelId")
    public Result getModelInfoByModelId(@RequestParam Integer modelId){
        ModelEntity modelEntity = modelService.getById(modelId);
        JSONObject object = new JSONObject(modelEntity);
        if(modelEntity != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), object);
        }else{
            return Result.failed("模型信息获取失败！");
        }
    }

    @ApiOperation("上传模型接口")
    @PostMapping(value = "/uploadModelInfo", consumes = "multipart/form-data")
    public Result uploadModelInfo(@RequestPart("modelImage") MultipartFile modelImage,
                              @RequestPart("modelInfo") String modelInfo) {

        // 检查modelImage是否为空（根据你的业务需求）
        if (modelImage == null || modelImage.isEmpty()|| modelInfo == null|| modelInfo.isEmpty()) {
            return Result.failed("模型信息填写不完整！"); // 假设你有一个Result类来封装响应
        }
        ModelEntity modelEntity = new ModelEntity();
        // 解析描述信息
        if(modelInfo != null){
            modelEntity = JSONUtil.toBean(modelInfo, ModelEntity.class);
        }

        String path = environment.getProperty("web.upload-path") + File.separator + MyConstant.MODEL_IMAGE_DIR;
        String fileName = FileUtil.saveFile2Path(modelImage, path);
        modelEntity.setModelImage(MyConstant.MODEL_IMAGE_DIR+ File.separator + fileName);

        Integer userId = getUserId();
        modelEntity.setCreatorId(userId);

        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        modelEntity.setCreateTime(currentDate);

        boolean save = modelService.save(modelEntity);
        if(save){
            JSONObject object = new JSONObject(modelEntity);
            return Result.success("模型信息上传成功！",object);
        }else {
            return Result.failed("图像信息上传失败！");
        }
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
}
