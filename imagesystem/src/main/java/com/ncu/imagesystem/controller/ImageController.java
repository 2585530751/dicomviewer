package com.ncu.imagesystem.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ncu.imagesystem.dto.ImagesReq;
import com.ncu.imagesystem.entity.ImageEntity;
import com.ncu.imagesystem.entity.SeriesEntity;
import com.ncu.imagesystem.service.ImageService;
import com.ncu.imagesystem.service.SeriesService;
import com.ncu.imagesystem.service.StudyService;
import com.ncu.imagesystem.tools.FileUtil;
import com.ncu.imagesystem.tools.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Api("图像文件信息表controller")
@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private SeriesService seriesService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private StudyService studyService;
    @Autowired
    private Environment environment;

    @ApiOperation("新增一条图像文件信息接口")
    @PostMapping("/uploadSingleImageForm")
    public Result uploadSingleImageForm(@RequestPart("file")MultipartFile file, @RequestPart("info")String info){
        boolean b = true;
        //获取文件关联的图像集合信息
        if(info == null) return Result.failed("没有图像文件关联信息！");
        ImageEntity imageEntity = JSONUtil.toBean(info, ImageEntity.class);
        if(imageEntity.getSeriesId() == null) return Result.failed("没有图像文件关联的图像集合ID！");
        SeriesEntity seriesEntity = seriesService.getById(imageEntity.getSeriesId());
        String seriesPath = seriesEntity.getSeriesPath();
        String path = environment.getProperty("web.upload-path") + File.separator + seriesPath;

        //对文件的具体操作
        String fileName = FileUtil.saveFile2Path(file, path);
        if(fileName==null) return Result.failed("图像文件保存失败！");
        imageEntity.setImagePath(seriesPath + File.separator + fileName);
        imageEntity.setImageName(fileName);

        //保存到数据库
        imageEntity.setIsDeleted(0);
        boolean save = imageService.save(imageEntity);
        if(save){
            return Result.success("图像文件添加成功！");
        }else {
            return Result.failed("图像文件添加失败！");
        }
    }

    @ApiOperation("根据singleImageId获取图像文件接口")
    @GetMapping("/getSingleImagesURLById")
    public Result getSingleImagesURLById(Integer singleImageId, HttpServletRequest request){
        ImageEntity imageEntity = imageService.getSingleImageById(singleImageId);
        if(imageEntity == null) return Result.failed("图像文件查找失败！");
        String singleImagePath = imageEntity.getImagePath();  //文件保存路径
        if(singleImagePath == null || singleImagePath.isEmpty()) return Result.failed("图像文件保存路径获取失败！");
        String url = request.getScheme() + "://" +
                request.getServerName() + ":" +
                request.getServerPort() + "/" + singleImagePath.replace("\\", "/");
        return Result.success("获取文件url成功！", url);
    }

    @ApiOperation("根据singleImageId删除单个图像文件接口")
    @GetMapping("/deleteSingleImageById")
    public Result deleteSingleImageById(Integer singleImageId){
        ImageEntity imageEntity = imageService.getSingleImageById(singleImageId);
        if(imageEntity == null) return Result.success("图像文件删除成功！");
        imageEntity.setIsDeleted(1);
        boolean b = imageService.updateById(imageEntity);
        imageService.checkAndDeleteImage(imageEntity.getSeriesId());
        if(b){
            return Result.success("图像文件删除成功！");
        }else {
            return Result.failed("图像文件删除失败！");
        }
    }

    @ApiOperation("根据singleImageId删除多个图像文件接口")
    @PostMapping("/deleteSingleImageByIds")
    public Result deleteSingleImageByIds(@RequestBody List<Integer> ids){
        ImageEntity imageEntity = imageService.getById(ids.get(0));
        LambdaQueryWrapper<ImageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ImageEntity::getImageId, ids);
        ImageEntity imageEntity1 = new ImageEntity();
        imageEntity1.setIsDeleted(1);
        boolean b = imageService.update(imageEntity1, wrapper);
        imageService.checkAndDeleteImage(imageEntity.getSeriesId());
        if(b){
            return Result.success("图像文件删除成功！");
        }else {
            return Result.failed("图像文件删除失败！");
        }
    }

    @ApiOperation("批量新增图像文件信息接口")
    @PostMapping(value = "/addImages", consumes ="multipart/form-data")
    public Result addImages(@RequestPart("files") MultipartFile[] files, @RequestPart("info")String info){
        ImagesReq imagesReq = JSONUtil.toBean(info, ImagesReq.class);
        boolean b = studyService.addImages(files, imagesReq);
        if(b){
            return Result.success("图像文件新增成功！");
        }else {
            return Result.failed("图像文件新增失败！");
        }
    }

    @ApiOperation("删除单个图像接口")
    @GetMapping("/deleteOneImage")
    public Result deleteOneImage(Integer imageId){
        boolean b = imageService.removeOneById(imageId);
        if(b){
            return Result.success("图像删除成功！");
        }else {
            return Result.failed("图像删除失败！");
        }
    }

    @ApiOperation("删除多个图像接口")
    @PostMapping("/deleteImages")
    public Result deleteImages(@RequestBody List<Integer> ids){
        boolean b = imageService.removeListByIds(ids);
        if(b){
            return Result.success("图像删除成功！");
        }else {
            return Result.failed("图像删除失败！");
        }
    }

    @ApiOperation("更新图像接口")
    @PostMapping("/updateOneImage")
    public Result updateOneImage(@RequestBody ImageEntity image){
        image.setOperateTime(LocalDateTime.now());
        boolean b = imageService.updateById(image);
        if(b){
            return Result.success("图像更新成功！");
        }else {
            return Result.failed("图像更新失败！");
        }
    }
}
