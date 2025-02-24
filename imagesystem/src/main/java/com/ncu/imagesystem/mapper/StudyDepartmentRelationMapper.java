package com.ncu.imagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncu.imagesystem.entity.StudyDepartmentRelationEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StudyDepartmentRelationMapper extends BaseMapper<StudyDepartmentRelationEntity> {
    boolean saveOrUpdateBatch(List<StudyDepartmentRelationEntity> list);
}
