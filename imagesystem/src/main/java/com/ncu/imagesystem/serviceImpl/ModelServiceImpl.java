package com.ncu.imagesystem.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.entity.ModelEntity;
import com.ncu.imagesystem.mapper.ModelMapper;
import com.ncu.imagesystem.service.ModelService;
import org.springframework.stereotype.Service;

@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, ModelEntity> implements ModelService {
}
