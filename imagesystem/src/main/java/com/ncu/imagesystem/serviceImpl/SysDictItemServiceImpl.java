package com.ncu.imagesystem.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.entity.SysDictItemEntity;
import com.ncu.imagesystem.mapper.SysDictItemMapper;
import com.ncu.imagesystem.service.SysDictItemService;
import org.springframework.stereotype.Service;

@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItemEntity> implements SysDictItemService {
}
