package com.ncu.imagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncu.imagesystem.entity.SysGroupEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SysGroupMapper extends BaseMapper<SysGroupEntity> {
    List<SysGroupEntity> getGroupByUserId(Integer userId);
}
