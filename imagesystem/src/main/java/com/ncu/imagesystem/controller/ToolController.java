package com.ncu.imagesystem.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;

import com.ncu.imagesystem.entity.ToolEntity;
import com.ncu.imagesystem.service.ToolService;
import com.ncu.imagesystem.tools.FileUtil;
import com.ncu.imagesystem.tools.MyConstant;
import com.ncu.imagesystem.tools.Result;
import com.ncu.imagesystem.enums.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;


@Api("工具集控制controller")
@RestController
@RequestMapping("/tool")
public class ToolController {

    @Autowired
    private ToolService toolService;
    @Autowired
    private Environment environment;

    @ApiOperation("上传工具集信息及Excel文件接口")
    @PostMapping(value="/addTool",consumes = "multipart/form-data")

    public Result addTool(@RequestPart("toolFile") MultipartFile toolFile,
                          @RequestPart("toolInfo") String toolInfo){
        // 检查modelImage是否为空（根据你的业务需求）
        if (toolFile == null || toolFile.isEmpty()|| toolInfo == null|| toolInfo.isEmpty()) {
            return Result.failed("模型信息填写不完整！"); // 假设你有一个Result类来封装响应
        }
        ToolEntity toolEntity = new ToolEntity();
        if(toolInfo !=null){
            toolEntity = JSONUtil.toBean(toolInfo, ToolEntity.class);
        }
        String path = environment.getProperty("web.upload-path") + File.separator + MyConstant.TOOL_FILE_DIR;
        String fileName = FileUtil.saveFile2Path(toolFile, path);
        toolEntity.setToolRoute(MyConstant.TOOL_FILE_DIR+ File.separator + fileName);

        Integer userId = getUserId();
        toolEntity.setUserId(userId);
        boolean save = toolService.save(toolEntity);
        if(save){
            JSONObject object = new JSONObject(toolEntity);
            return Result.success("模型信息上传成功！",object);
        }else {
            return Result.failed("图像信息上传失败！");
        }
    }

    @ApiOperation("获取工具集信息及Excel文件接口")
    @GetMapping("/getToolInfoByUserId")
    public Result getToolInfoByUserId(){
        Integer userId = getUserId();
        List<ToolEntity> toolList = toolService.getListByUserId(userId);

        if(toolList != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), toolList);
        }else{
            return Result.failed("模型信息获取失败！");
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

    @ApiOperation("根据toolid获取信息接口")
    @GetMapping("/getToolInfoByToolId")
    public Result getToolInfoByToolId(Integer toolId){
        ToolEntity toolEntity =toolService.getById(toolId);
        JSONObject object = new JSONObject(toolEntity);
        if(toolEntity != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), object);
        }else{
            return Result.failed("模型信息获取失败！");
        }
    }

    @ApiOperation("更新工具集信息及Excel文件接口")
    @PostMapping(value="/updateTool",consumes = "multipart/form-data")

    public Result updateTool(@RequestPart("toolFile") MultipartFile toolFile,
                          @RequestPart("toolInfo") String toolInfo){
        if (toolFile == null || toolFile.isEmpty()|| toolInfo == null|| toolInfo.isEmpty()) {
            return Result.failed("模型信息填写不完整！"); // 假设你有一个Result类来封装响应
        }
        ToolEntity toolEntity = new ToolEntity();
        if(toolInfo !=null){
            toolEntity = JSONUtil.toBean(toolInfo, ToolEntity.class);
        }
        String path = environment.getProperty("web.upload-path") + File.separator + MyConstant.TOOL_FILE_DIR;
        String fileName = FileUtil.saveFile2PathAddXlsx(toolFile, path);//这里添加了一种方法给上传的blob文件加上了.xlsx后缀方便前端读取
        toolEntity.setToolOperate(MyConstant.TOOL_FILE_DIR+ File.separator + fileName); //

        Integer userId = getUserId();
        toolEntity.setUserId(userId);
        boolean save = toolService.updateById(toolEntity);
        if(save){
            JSONObject object = new JSONObject(toolEntity);
            return Result.success("模型信息上传成功！",object);
        }else {
            return Result.failed("图像信息上传失败！");
        }
    }
}
