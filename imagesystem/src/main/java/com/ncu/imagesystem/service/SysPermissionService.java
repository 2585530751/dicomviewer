package com.ncu.imagesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncu.imagesystem.entity.SysPermissionEntity;

import java.util.List;

public interface SysPermissionService extends IService<SysPermissionEntity> {
    List<SysPermissionEntity> getPermissionsByUserId(Integer userId);

    List<SysPermissionEntity> getPermissionsByGroupId(List<Integer> groupId);

    List<SysPermissionEntity> getUserAllPermissionsByUserId(Integer userId);

    SysPermissionEntity getPermissionByName(String name);
}
