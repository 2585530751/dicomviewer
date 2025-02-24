package com.ncu.imagesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncu.imagesystem.entity.SysUserEntity;

public interface SysUserService extends IService<SysUserEntity> {
    SysUserEntity getUserByAccount(String account);
}
