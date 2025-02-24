package com.ncu.imagesystem.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ncu.imagesystem.entity.ModelResultEntity;
import com.ncu.imagesystem.enums.ResultCode;
import com.ncu.imagesystem.service.ModelResultService;
import com.ncu.imagesystem.tools.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Api("图像模型结果信息表controller")
@RestController
@RequestMapping("/modelResult")
public class ModelResultController {

    @Autowired
    private ModelResultService modelResultService;

    @Autowired
    private Environment environment;

    @ApiOperation("新增一条图像模型结果信息接口")
    @PostMapping("/addOne")
    public Result addOne(@RequestBody ModelResultEntity modelResult){
        boolean save = modelResultService.save(modelResult);
        if(save){
            return Result.success("图像模型结果信息添加成功！");
        }else {
            return Result.failed("图像模型结果信息添加失败！");
        }
    }

    @ApiOperation("根据modelResultId查询图像模型结果信息接口")
    @GetMapping("/getModelResultById")
    public Result getModelResultById(Integer modelResultId){
        ModelResultEntity modelResult = modelResultService.getById(modelResultId);
        if(modelResult != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), modelResult);
        }else {
            return Result.failed("图像模型结果信息获取失败！");
        }
    }

    @ApiOperation("根据modelResultId更新图像模型结果信息接口")
    @PostMapping("/updateModelResultById")
    public Result updateModelResultById(@RequestBody ModelResultEntity modelResult){
        boolean b = modelResultService.updateById(modelResult);
        if(b){
            return Result.success("图像模型结果信息更新成功！");
        }else {
            return Result.failed("图像模型结果信息更新失败！");
        }
    }

    @ApiOperation("根据modelResultId查询图像模型结果文件接口")
    @GetMapping("/getModelResultFileURLById")
    public Result getModelResultFileURLById(Integer modelResultId, HttpServletRequest request){
        ModelResultEntity modelResult = modelResultService.getById(modelResultId);
        String resData = modelResult.getResData();
        JSONObject obj = JSONUtil.parseObj(resData);
        String path = obj.get("modelResultPath", String.class);
        String baseURL = request.getScheme() + "://" +
                request.getServerName() + ":" +
                request.getServerPort() + "/";
        List<String> urlList = new ArrayList<>();
        String basePath = environment.getProperty("web.upload-path");
        String dir = basePath + File.separator + path;
        File file = new File(dir);
        File[] files = file.listFiles();
        if(files != null){
            for(File f : files){
                String fileName = f.getName();
                String s = path + File.separator + fileName;
                urlList.add(baseURL + s.replace("\\", "/"));
            }
        }
        if(!urlList.isEmpty()){
            return Result.success("获取文件url成功！", urlList);
        }
        return Result.failed("获取文件url失败！");
    }
}
