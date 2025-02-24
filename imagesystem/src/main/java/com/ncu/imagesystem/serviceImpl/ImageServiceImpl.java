package com.ncu.imagesystem.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.entity.SeriesEntity;
import com.ncu.imagesystem.entity.ImageEntity;
import com.ncu.imagesystem.mapper.ImageMapper;
import com.ncu.imagesystem.service.SeriesService;
import com.ncu.imagesystem.service.ImageService;
import com.ncu.imagesystem.tools.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, ImageEntity> implements ImageService {
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private Environment environment;

    @Override
    public List<ImageEntity> getListBySeriesId(Integer seriesId) {
        LambdaQueryWrapper<ImageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ImageEntity::getSeriesId, seriesId);
        wrapper.eq(ImageEntity::getIsDeleted, 0);
        return this.list(wrapper);
    }

    @Override
    public ImageEntity getSingleImageById(Integer singleImageId) {
        LambdaQueryWrapper<ImageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ImageEntity::getImageId, singleImageId);
        wrapper.eq(ImageEntity::getIsDeleted, 0);
        return this.getOne(wrapper);
    }

    @Override
    public void checkAndDeleteImage(Integer seriesId) {
        LambdaQueryWrapper<ImageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ImageEntity::getSeriesId, seriesId);
        wrapper.eq(ImageEntity::getIsDeleted, 0);
        List<ImageEntity> list = this.list(wrapper);
        if(list.isEmpty()){
            SeriesEntity seriesEntity = new SeriesEntity();
            seriesEntity.setSeriesId(seriesId);
            seriesEntity.setIsDeleted(1);
            seriesService.updateById(seriesEntity);
        }else{
            SeriesEntity seriesEntity = new SeriesEntity();
            seriesEntity.setSeriesId(seriesId);
            seriesEntity.setSeriesCount(list.size());
            seriesService.updateById(seriesEntity);
        }
    }

    @Override
    public boolean removeOneById(Integer id) {
        ImageEntity image = this.getById(id);
        String imagePath = image.getImagePath();
        if(StringUtils.isNotBlank(imagePath)){
            String filePath = environment.getProperty("web.upload-path") + File.separator + imagePath;
            File file = new File(filePath);
            FileUtil.deleteFileOrDir(file);
        }
        return this.removeById(id);
    }

    @Override
    public boolean removeListByIds(List<Integer> ids) {
        List<ImageEntity> images = this.listByIds(ids);
        String property = environment.getProperty("web.upload-path");
        for(ImageEntity image : images){
            String imagePath = image.getImagePath();
            if(StringUtils.isNotBlank(imagePath)) {
                String filePath = property + File.separator + imagePath;
                File file = new File(filePath);
                FileUtil.deleteFileOrDir(file);
            }
        }
        return this.removeByIds(ids);
    }

    @Override
    public boolean removeList(LambdaQueryWrapper<ImageEntity> wrapper) {
        List<ImageEntity> images = this.list(wrapper);
        String property = environment.getProperty("web.upload-path");
        for(ImageEntity image : images){
            String imagePath = image.getImagePath();
            if(StringUtils.isNotBlank(imagePath)) {
                String filePath = property + File.separator + imagePath;
                File file = new File(filePath);
                FileUtil.deleteFileOrDir(file);
            }
        }
        return this.remove(wrapper);
    }
}
