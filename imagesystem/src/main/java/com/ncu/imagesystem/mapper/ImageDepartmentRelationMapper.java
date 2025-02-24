package com.ncu.imagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncu.imagesystem.entity.ImageDepartmentRelationEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ImageDepartmentRelationMapper extends BaseMapper<ImageDepartmentRelationEntity> {
    boolean saveOrUpdateBatch(List<ImageDepartmentRelationEntity> list);
}
