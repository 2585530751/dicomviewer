package com.ncu.imagesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncu.imagesystem.dto.CommonPage;
import com.ncu.imagesystem.dto.PatientQuery;
import com.ncu.imagesystem.entity.PatientEntity;
import com.ncu.imagesystem.entity.StudyEntity;

import java.util.List;


public interface PatientService extends IService<PatientEntity> {
    CommonPage<PatientEntity> getPatientPageByQuery(PatientQuery patientQuery);

    boolean addOnePatient(PatientEntity patient, List<Integer> departmentIdList);
}
