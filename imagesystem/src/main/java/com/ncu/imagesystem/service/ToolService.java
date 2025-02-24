package com.ncu.imagesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncu.imagesystem.entity.ToolEntity;

import java.util.List;

public interface ToolService extends IService<ToolEntity> {
    List<ToolEntity> getListByUserId(Integer userId);
}