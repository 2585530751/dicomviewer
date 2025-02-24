package com.ncu.imagesystem.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.entity.SysGroupEntity;
import com.ncu.imagesystem.mapper.SysGroupMapper;
import com.ncu.imagesystem.service.SysGroupService;
import org.springframework.stereotype.Service;

@Service
public class SysGroupServiceImpl extends ServiceImpl<SysGroupMapper, SysGroupEntity> implements SysGroupService {
}
