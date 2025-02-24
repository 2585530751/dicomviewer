package com.ncu.imagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncu.imagesystem.entity.SysPermissionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SysPermissionMapper extends BaseMapper<SysPermissionEntity> {
    List<SysPermissionEntity> getPermissionsByUserId(Integer userId);

    List<SysPermissionEntity> getPermissionsByGroupId(List<Integer> groupId);
}
