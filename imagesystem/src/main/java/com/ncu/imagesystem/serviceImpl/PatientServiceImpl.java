package com.ncu.imagesystem.serviceImpl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.dto.CommonPage;
import com.ncu.imagesystem.dto.PatientQuery;
import com.ncu.imagesystem.entity.*;
import com.ncu.imagesystem.mapper.PatientDepartmentRelationMapper;
import com.ncu.imagesystem.mapper.PatientMapper;
import com.ncu.imagesystem.service.PatientService;
import com.ncu.imagesystem.service.SysPermissionService;
import com.ncu.imagesystem.tools.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;


@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, PatientEntity> implements PatientService {
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private PatientDepartmentRelationMapper patientDepartmentRelationMapper;
    @Override
    public CommonPage<PatientEntity> getPatientPageByQuery(PatientQuery patientQuery) {
        LambdaQueryWrapper<PatientEntity> wrapper = new LambdaQueryWrapper<>();
        // 分页查询条件
        if(patientQuery.getPatientName() != null){
            wrapper.like(PatientEntity::getPatientName, patientQuery.getPatientName());
        }
        if(patientQuery.getGender() != null){
            wrapper.eq(PatientEntity::getPatientGender, patientQuery.getGender());
        }
        // 设置分页信息
        Page<PatientEntity> page = new Page<>(patientQuery.getCurrent(), patientQuery.getPageSize());
        // 执行分页查询
        Page<PatientEntity> patientPage = this.page(page, wrapper);
        // 获取分页查询结果
        if(patientPage == null) return null;
        return new CommonPage<>(patientPage.getRecords(),
                patientPage.getTotal(),
                patientPage.getSize(),
                patientPage.getCurrent(),
                patientPage.getPages());
    }

    @Override
    public boolean addOnePatient(PatientEntity patient, List<Integer> departmentIdList) {
        JSONObject userJson = UserUtils.getUserJsonObject();
        patient.setCreatorId(userJson.getInt("userId"));
        patient.setCreateTime(LocalDateTime.now());
        boolean save = this.save(patient);
        boolean b = false;
        if (save){
            List<SysPermissionEntity> permissionList = sysPermissionService.listByIds(departmentIdList);
            List<PatientDepartmentRelationEntity> list = getSDRelListByGroupList(permissionList, patient.getPatientId());
            b = patientDepartmentRelationMapper.saveOrUpdateBatch(list);
        }
        return save && b;
    }

    private List<PatientDepartmentRelationEntity> getSDRelListByGroupList(List<SysPermissionEntity> permissionList, Integer studyId){
        List<PatientDepartmentRelationEntity> relList = new ArrayList<>();
        for(SysPermissionEntity permission : permissionList){
            PatientDepartmentRelationEntity relation = new PatientDepartmentRelationEntity();
            relation.setPatientId(studyId);
            relation.setDepartmentId(permission.getPermissionId());
            relation.setDepartmentName(permission.getPermissionName());
            relation.setDepartmentDesc(permission.getDescription());
            relList.add(relation);
        }
        return relList;
    }
}
