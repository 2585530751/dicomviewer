package com.ncu.imagesystem.dto;

import com.ncu.imagesystem.entity.PatientEntity;

import java.util.List;

public class OnePatientReq {
    private PatientEntity patient;
    private List<Integer> departmentIdList;

    public PatientEntity getPatient() {
        return patient;
    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
    }

    public List<Integer> getDepartmentIdList() {
        return departmentIdList;
    }

    public void setDepartmentIdList(List<Integer> departmentIdList) {
        this.departmentIdList = departmentIdList;
    }
}
