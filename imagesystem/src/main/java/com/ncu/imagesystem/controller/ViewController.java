package com.ncu.imagesystem.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ncu.imagesystem.dto.ViewReq;
import com.ncu.imagesystem.entity.ImageEntity;
import com.ncu.imagesystem.entity.SeriesEntity;
import com.ncu.imagesystem.service.ImageService;
import com.ncu.imagesystem.service.SeriesService;
import com.ncu.imagesystem.tools.FileUtil;
import com.ncu.imagesystem.tools.MyConstant;
import com.ncu.imagesystem.tools.Result;
import com.ncu.imagesystem.tools.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

@Api("阅片流程接口")
@RestController
@RequestMapping("/view")
public class ViewController {
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private Environment environment;

    @ApiOperation("阅片员阅片接口")
    @PostMapping("/reader")
    public Result reader(@RequestPart("file") MultipartFile file, @RequestPart("info") String info) {
        if(info == null) return Result.failed("没有文件关联信息！");
        ViewReq req = JSONUtil.toBean(info, ViewReq.class);
        if(req.getSeriesId() == null) return Result.failed("没有文件关联的序列ID！");
        if(req.getImageId() == null) return Result.failed("没有文件关联的图像ID！");
        // 保存指定的图像并设置为预览图像
        String path = environment.getProperty("web.upload-path") + File.separator + MyConstant.MARK_IMAGE_DIR;
        File dir = new File(path);
        if(!dir.exists()){
            boolean mkdir = dir.mkdir();
        }
        //对文件的具体操作
        String fileName = FileUtil.saveFile2Path(file, path);
        if(fileName==null) return Result.failed("图像文件保存失败！");

        Integer seriesId = req.getSeriesId();
        SeriesEntity series = seriesService.getById(seriesId);
        Integer imageId = req.getImageId();
        ImageEntity image = imageService.getById(imageId);
        // 设置为预览图像
        series.setMarkSeriesName(fileName);
        series.setMarkSeriesPreviewPath(MyConstant.MARK_IMAGE_DIR + File.separator + fileName);
        series.setSeriesPreviewPath(image.getImagePath());
        // 保存阅片信息
        series.setReaderView(req.getReaderView());
        series.setSeriesStatus(1);
        JSONObject userJson = UserUtils.getUserJsonObject();
        series.setReaderViewId(userJson.getInt("userId"));
        series.setReaderViewName(userJson.getStr("userName"));
        series.setReaderViewTime(new Date());
        boolean b = seriesService.updateById(series);

        if(b){
            return Result.success("阅片成功！", series);
        }else{
            return Result.failed("阅片失败！");
        }
    }

    @ApiOperation("医生阅片接口")
    @PostMapping("/doctor")
    public Result doctor(@RequestBody ViewReq req) {
        Integer seriesId = req.getSeriesId();
        SeriesEntity series = seriesService.getById(seriesId);
        series.setDoctorView(req.getDoctorView());
        series.setSeriesStatus(2);
        JSONObject userJson = UserUtils.getUserJsonObject();
        series.setDoctorViewId(userJson.getInt("userId"));
        series.setDoctorViewName(userJson.getStr("userName"));
        series.setDoctorViewTime(new Date());
        boolean b = seriesService.updateById(series);

        if(b){
            return Result.success("诊断完成",series);
        }else{
            return Result.failed("未完成诊断！");
        }
    }
}
