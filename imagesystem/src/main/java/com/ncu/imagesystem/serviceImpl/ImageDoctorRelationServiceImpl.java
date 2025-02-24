package com.ncu.imagesystem.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.entity.ImageDoctorRelationEntity;
import com.ncu.imagesystem.mapper.ImageDoctorRelationMapper;
import com.ncu.imagesystem.service.ImageDoctorRelationService;
import org.springframework.stereotype.Service;

@Service
public class ImageDoctorRelationServiceImpl extends ServiceImpl<ImageDoctorRelationMapper, ImageDoctorRelationEntity> implements ImageDoctorRelationService {
}
