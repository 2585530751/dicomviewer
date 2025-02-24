package com.ncu.imagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncu.imagesystem.entity.PatientDepartmentRelationEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PatientDepartmentRelationMapper extends BaseMapper<PatientDepartmentRelationEntity> {
    boolean saveOrUpdateBatch(List<PatientDepartmentRelationEntity> list);
}
