package com.ncu.imagesystem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ncu.imagesystem.entity.ImageEntity;

import java.util.List;

public interface ImageService extends IService<ImageEntity> {

    List<ImageEntity> getListBySeriesId(Integer seriesId);
    ImageEntity getSingleImageById(Integer singleImageId);
    void checkAndDeleteImage(Integer seriesId);

    boolean removeOneById(Integer id);
    boolean removeListByIds(List<Integer> ids);
    boolean removeList(LambdaQueryWrapper<ImageEntity> wrapper);
}
