package com.ncu.imagesystem.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ncu.imagesystem.dto.CommonPage;
import com.ncu.imagesystem.dto.OnePatientReq;

import com.ncu.imagesystem.dto.PatientQuery;
import com.ncu.imagesystem.entity.*;
import com.ncu.imagesystem.enums.ResultCode;
import com.ncu.imagesystem.mapper.ImageDepartmentRelationMapper;
import com.ncu.imagesystem.mapper.PatientDepartmentRelationMapper;
import com.ncu.imagesystem.mapper.StudyDepartmentRelationMapper;
import com.ncu.imagesystem.service.*;
import com.ncu.imagesystem.tools.Result;
import com.ncu.imagesystem.tools.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Api("病人信息表controller")
@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private PatientDepartmentRelationMapper patientDepartmentRelationMapper;
    @Autowired
    private StudyDepartmentRelationMapper studyDepartmentRelationMapper;
    @Autowired
    private ImageDepartmentRelationMapper seriesDepartmentRelationMapper;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private StudyService studyService;
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private ImageService imageService;

    @ApiOperation("新增一条病人信息接口")
    @PostMapping("/addPatient")
    public Result addPatient(@RequestBody PatientEntity patient){
        JSONObject userJson = UserUtils.getUserJsonObject();
        Integer userId = userJson.getInt("userId");

        patient.setCreatorId(userId);
        patient.setCreateTime(LocalDateTime.now());

        boolean save = patientService.save(patient);

        List<SysPermissionEntity> permissionList = sysPermissionService.getPermissionsByUserId(userId);
        List<PatientDepartmentRelationEntity> list = new ArrayList<>();
        HashSet<Integer> patientIdSet = new HashSet<>();
        permissionList.forEach(p-> patientIdSet.add(p.getPermissionParentId()));
        permissionList.forEach(p->{
            if(!patientIdSet.contains(p.getPermissionId())) {
                PatientDepartmentRelationEntity pdr = new PatientDepartmentRelationEntity();
                pdr.setPatientId(patient.getPatientId());
                pdr.setDepartmentDesc(p.getDescription());
                pdr.setDepartmentName(p.getPermissionName());
                pdr.setDepartmentId(p.getPermissionId());
                list.add(pdr);
            }
        });
        patientDepartmentRelationMapper.saveOrUpdateBatch(list);
        if(save){
            return Result.success("病人信息添加成功！");
        }else {
            return Result.failed("病人信息添加失败！");
        }
    }

    @ApiOperation("新增一个患者接口接口")
    @PostMapping(value = "/addOnePatient")
    public Result addOnePatient(@RequestBody OnePatientReq onePatientReq) {
        PatientEntity patient = onePatientReq.getPatient();
        String patientIdCardNumber = onePatientReq.getPatient().getPatientIdCardNumber();
        if (!patientIdCardNumber.isEmpty()) {
            QueryWrapper<SysUserEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("id_card", patientIdCardNumber);
            SysUserEntity sysUserEntity = sysUserService.getOne(wrapper);
            if (sysUserEntity != null) {
                patient.setUserId(sysUserEntity.getUserId());
            }
        }
        boolean b = patientService.addOnePatient(onePatientReq.getPatient(), onePatientReq.getDepartmentIdList());
        if (b) {
            return Result.success("检查新增成功！");
        } else {
            return Result.failed("检查新增失败！");
        }
    }

    @ApiOperation("新增多条病人信息接口")
    @PostMapping("/addList")
    public Result addList(@RequestBody List<PatientEntity> patientList){
        boolean save = patientService.saveBatch(patientList);
        if(save){
            return Result.success("病人信息添加成功！");
        }else {
            return Result.failed("病人信息添加失败！");
        }
    }

    @ApiOperation("根据patientId查询病人信息接口")
    @GetMapping("/getPatientById")
    public Result getPatientById(Integer patientId){
        PatientEntity patient = patientService.getById(patientId);
        if(patient != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), patient);
        }else {
            return Result.failed("病人信息获取失败！");
        }
    }

    @ApiOperation("根据条件查询病人信息接口")
    @PostMapping("/getPatientByQuery")
    public Result getPatientByQuery(@RequestBody PatientEntity patient){
        QueryWrapper<PatientEntity> wrapper = new QueryWrapper<>(patient);
        List<PatientEntity> list = patientService.list(wrapper);
        if(list != null && list.size() > 0){
            return Result.success(ResultCode.SUCCESS.getMsg(), list);
        }else {
            return Result.failed("病人信息获取失败！");
        }
    }

    @ApiOperation("根据patientId更新病人信息接口")
    @PostMapping("/updatePatientById")
    public Result updatePatientById(@RequestBody PatientEntity patient){
        boolean b = patientService.updateById(patient);
        if(b){
            return Result.success("病人信息更新成功！");
        }else {
            return Result.failed("病人信息更新失败！");
        }
    }

    @ApiOperation("根据条件分页查询病人信息接口")
    @PostMapping("/getPatientPageByQuery")
    public Result getPatientPageByQuery(@RequestBody PatientQuery patientQuery){
        CommonPage<PatientEntity> page = patientService.getPatientPageByQuery(patientQuery);
        if(page != null && page.getRecords().size() > 0){
            return Result.success(ResultCode.SUCCESS.getMsg(), page);
        }else {
            return Result.failed("病人信息获取失败！");
        }
    }

    @ApiOperation("返回所有病人信息")
    @GetMapping("/getAllPatientInformation")
    public Result getAllPatientInformation(){
        List<PatientEntity> list = patientService.list();
//        JSONObject object = new JSONObject(list);
        if(list != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), list);
        }else {
            return Result.failed("病人信息获取失败！");
        }
    }

    @ApiOperation("删除单个病人接口")
    @GetMapping("/deleteOnePatient")
    public Result deleteOnePatient(Integer patientId){
        LambdaQueryWrapper<StudyEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudyEntity::getPatientId, patientId);
        List<StudyEntity> studyList = studyService.list(wrapper);
        if(studyList != null && !studyList.isEmpty()) {
            List<Integer> ids = studyList.stream()
                    .map(StudyEntity::getStudyId).collect(Collectors.toList());
            LambdaQueryWrapper<SeriesEntity> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.in(SeriesEntity::getStudyId, ids);
            List<SeriesEntity> list = seriesService.list(wrapper1);
            if(list != null && !list.isEmpty()) {
                List<Integer> seriesIdList = list.stream()
                        .map(SeriesEntity::getSeriesId).collect(Collectors.toList());
                LambdaQueryWrapper<ImageEntity> wrapper2 = new LambdaQueryWrapper<>();
                wrapper2.in(ImageEntity::getSeriesId, seriesIdList);
                imageService.removeList(wrapper2);
                seriesService.removeListByIds(seriesIdList);
                LambdaQueryWrapper<ImageDepartmentRelationEntity> wrapper3 = new LambdaQueryWrapper<>();
                wrapper3.in(ImageDepartmentRelationEntity::getSeriesId, seriesIdList);
                seriesDepartmentRelationMapper.delete(wrapper3);
            }
            studyService.removeByIds(ids);
            LambdaQueryWrapper<StudyDepartmentRelationEntity> wrapper4 = new LambdaQueryWrapper<>();
            wrapper4.in(StudyDepartmentRelationEntity::getStudyId, ids);
            studyDepartmentRelationMapper.delete(wrapper4);
        }
        LambdaQueryWrapper<PatientDepartmentRelationEntity> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(PatientDepartmentRelationEntity::getPatientId, patientId);
        patientDepartmentRelationMapper.delete(wrapper1);
        boolean b = patientService.removeById(patientId);
        if(b){
            return Result.success("病人删除成功！");
        }else {
            return Result.failed("病人删除失败！");
        }
    }

    @ApiOperation("删除多个病人接口")
    @PostMapping("/deletePatients")
    public Result deletePatients(@RequestBody List<Integer> patientIdList){
        LambdaQueryWrapper<StudyEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(StudyEntity::getPatientId, patientIdList);
        List<StudyEntity> studyList = studyService.list(wrapper);
        if(studyList != null && !studyList.isEmpty()) {
            List<Integer> ids = studyList.stream()
                    .map(StudyEntity::getStudyId).collect(Collectors.toList());
            LambdaQueryWrapper<SeriesEntity> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.in(SeriesEntity::getStudyId, ids);
            List<SeriesEntity> list = seriesService.list(wrapper1);
            if(list != null && !list.isEmpty()) {
                List<Integer> seriesIdList = list.stream()
                        .map(SeriesEntity::getSeriesId).collect(Collectors.toList());
                LambdaQueryWrapper<ImageEntity> wrapper2 = new LambdaQueryWrapper<>();
                wrapper2.in(ImageEntity::getSeriesId, seriesIdList);
                imageService.removeList(wrapper2);
                seriesService.removeListByIds(seriesIdList);
                LambdaQueryWrapper<ImageDepartmentRelationEntity> wrapper3 = new LambdaQueryWrapper<>();
                wrapper3.in(ImageDepartmentRelationEntity::getSeriesId, seriesIdList);
                seriesDepartmentRelationMapper.delete(wrapper3);
            }
            studyService.removeByIds(ids);
            LambdaQueryWrapper<StudyDepartmentRelationEntity> wrapper4 = new LambdaQueryWrapper<>();
            wrapper4.in(StudyDepartmentRelationEntity::getStudyId, ids);
            studyDepartmentRelationMapper.delete(wrapper4);
        }
        LambdaQueryWrapper<PatientDepartmentRelationEntity> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(PatientDepartmentRelationEntity::getPatientId, patientIdList);
        patientDepartmentRelationMapper.delete(wrapper1);
        boolean b = patientService.removeByIds(patientIdList);
        if(b){
            return Result.success("病人删除成功！");
        }else {
            return Result.failed("病人删除失败！");
        }
    }

    @ApiOperation("更新病人接口")
    @PostMapping("/updateOnePatient")
    public Result updateOnePatient(@RequestBody PatientEntity patient){
        boolean b = patientService.updateById(patient);
        if(b){
            return Result.success("病人更新成功！");
        }else {
            return Result.failed("病人更新失败！");
        }
    }
}



