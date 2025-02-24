package com.ncu.imagesystem.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.entity.OrganizationEntity;
import com.ncu.imagesystem.mapper.OrganizationMapper;
import com.ncu.imagesystem.service.OrganizationService;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, OrganizationEntity> implements OrganizationService {
}
