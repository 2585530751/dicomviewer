package com.ncu.imagesystem.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.entity.SysUserEntity;
import com.ncu.imagesystem.mapper.SysUserMapper;
import com.ncu.imagesystem.service.SysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {
    @Override
    public SysUserEntity getUserByAccount(String account) {
        LambdaQueryWrapper<SysUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserEntity::getAccount, account);
        return this.getOne(wrapper);
    }
}
