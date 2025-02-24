package com.ncu.imagesystem.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.entity.DoctorEntity;
import com.ncu.imagesystem.mapper.DoctorMapper;
import com.ncu.imagesystem.service.DoctorService;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, DoctorEntity> implements DoctorService {
}
