package com.ncu.imagesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncu.imagesystem.entity.ModelResultEntity;

import java.util.List;

public interface ModelResultService extends IService<ModelResultEntity> {
    Boolean addNewModelResult(ModelResultEntity modelResult);

    boolean saveBatch(List<ModelResultEntity> modelResultList, int modelId);

    boolean removeListBySeriesIds(List<Integer> ids);
}
