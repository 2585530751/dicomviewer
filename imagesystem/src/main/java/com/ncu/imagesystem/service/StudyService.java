package com.ncu.imagesystem.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ncu.imagesystem.dto.*;
import com.ncu.imagesystem.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudyService extends IService<StudyEntity> {

    Integer checkAndSavePatientData(MultipartFile[] files, List<SysPermissionEntity> permissionList, JSONObject userJson);

    Integer saveStudyData(MultipartFile[] files, PatientEntity patient, List<SysPermissionEntity> permissionList, JSONObject userJson);

    boolean saveOneStudy(StudyEntity study, List<Integer> departmentIdList);

    boolean saveOneSeries(SeriesEntity series, List<Integer> departmentIdList);

    boolean addImages(MultipartFile[] files, ImagesReq imagesReq);

    boolean saveSeriesData(MultipartFile[] files, PatientEntity patient, JSONObject userJson, Integer studyId, List<SysPermissionEntity> permissionList);

    CommonPage<StudyDTO> getPatientStudyPageByDepartmentId(PatientStudyQuery query);

    CommonPage<StudySeriesDTO> getStudySeriesPageByDepartmentId(StudySeriesQuery query);

    CommonPage<SeriesImageDTO> getSeriesImagePageByDepartmentId(SeriesImageQuery query);
}
