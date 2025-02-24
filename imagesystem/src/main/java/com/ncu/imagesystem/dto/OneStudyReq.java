package com.ncu.imagesystem.dto;

import com.ncu.imagesystem.entity.StudyEntity;

import java.util.List;

public class OneStudyReq {
    private StudyEntity study;

    private List<Integer> departmentIdList;

    public StudyEntity getStudy() {
        return study;
    }

    public void setStudy(StudyEntity study) {
        this.study = study;
    }

    public List<Integer> getDepartmentIdList() {
        return departmentIdList;
    }

    public void setDepartmentIdList(List<Integer> departmentIdList) {
        this.departmentIdList = departmentIdList;
    }
}
