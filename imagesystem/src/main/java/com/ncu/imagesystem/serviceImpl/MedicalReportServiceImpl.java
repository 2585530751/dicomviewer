package com.ncu.imagesystem.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.entity.MedicalReportEntity;
import com.ncu.imagesystem.mapper.MedicalReportMapper;
import com.ncu.imagesystem.service.MedicalReportService;
import org.springframework.stereotype.Service;

@Service
public class MedicalReportServiceImpl extends ServiceImpl<MedicalReportMapper, MedicalReportEntity> implements MedicalReportService {
}
