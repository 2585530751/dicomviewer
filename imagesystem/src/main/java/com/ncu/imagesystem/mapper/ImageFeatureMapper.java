package com.ncu.imagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncu.imagesystem.dto.ModelResultWithImageFeatureDTO;
import com.ncu.imagesystem.dto.ModelResultWithImageFeatureQuery;
import com.ncu.imagesystem.entity.ImageFeatureEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ImageFeatureMapper extends BaseMapper<ImageFeatureEntity> {

    boolean saveOrUpdateBatch(List<ImageFeatureEntity> imageFeatureEntityList);

    List<ModelResultWithImageFeatureDTO> getModelResultWithImageFeatureList(ModelResultWithImageFeatureQuery query);
}
