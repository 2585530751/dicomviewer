package com.ncu.imagesystem.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ncu.imagesystem.entity.ImageEntity;
import com.ncu.imagesystem.entity.MarkImageEntity;
import com.ncu.imagesystem.service.ImageService;
import com.ncu.imagesystem.service.SeriesService;
import com.ncu.imagesystem.tools.FileUtil;
import com.ncu.imagesystem.tools.MyConstant;
import com.ncu.imagesystem.tools.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Api("标注图像文件信息表controller")
@RestController
@RequestMapping("/markImage")
public class MarkImageController {

    @Autowired
    private SeriesService seriesService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private Environment environment;

    @ApiOperation("新增标注图像文件信息接口")
    @PostMapping("/uploadMarkImageForm")
    public Result uploadMarkImageForm(@RequestPart("file") MultipartFile file, @RequestPart("info")String info){

        //获取文件关联的信息
        if(info == null) return Result.failed("没有文件关联信息！");
        MarkImageEntity markImage = JSONUtil.toBean(info, MarkImageEntity.class);
        if(markImage.getImageId() == null) return Result.failed("没有文件关联的图像ID！");
        ImageEntity image = imageService.getById(markImage.getImageId());

        String path = environment.getProperty("web.upload-path") + File.separator + MyConstant.MARK_IMAGE_DIR;
        File dir = new File(path);
        if(!dir.exists()){
            boolean mkdir = dir.mkdir();
        }
        //对文件的具体操作
        String fileName = FileUtil.saveFile2Path(file, path);
        if(fileName==null) return Result.failed("图像文件保存失败！");
        image.setMarkImageDesc(markImage.getMarkImageDesc());
        //删除原来的标注图像文件
        if(StringUtils.isNotBlank(image.getMarkImageName())){
            String markImageName = image.getMarkImageName();
            File oldFile = new File(path + File.separator + markImageName);
            boolean delete = oldFile.delete();
        }
        image.setMarkImageName(fileName);
        image.setMarkImagePath(MyConstant.MARK_IMAGE_DIR + File.separator + fileName);
        image.setImageStatus(1);

        //更新数据
        boolean b = imageService.updateById(image);

        if(b){
            return Result.success("标注图像文件添加成功！", image);
        }else {
            return Result.failed("标注图像文件添加失败！");
        }
    }
}
