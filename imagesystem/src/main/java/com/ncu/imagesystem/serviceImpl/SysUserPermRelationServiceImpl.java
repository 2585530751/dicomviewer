package com.ncu.imagesystem.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.entity.SysUserPermRelationEntity;
import com.ncu.imagesystem.mapper.SysUserPermRelationMapper;
import com.ncu.imagesystem.service.SysUserPermRelationService;
import org.springframework.stereotype.Service;

@Service
public class SysUserPermRelationServiceImpl extends ServiceImpl<SysUserPermRelationMapper, SysUserPermRelationEntity> implements SysUserPermRelationService {
}
