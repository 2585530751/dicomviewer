package com.ncu.imagesystem.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.entity.ToolEntity;
import com.ncu.imagesystem.mapper.ToolMapper;

import com.ncu.imagesystem.service.ToolService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolServiceImpl extends ServiceImpl<ToolMapper, ToolEntity> implements ToolService {

    @Override
    public List<ToolEntity> getListByUserId(Integer userId) {
        LambdaQueryWrapper<ToolEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ToolEntity::getUserId, userId);
        return this.list(wrapper);
    }
}
