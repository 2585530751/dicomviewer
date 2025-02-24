package com.ncu.imagesystem.dto;

import java.util.List;

public class StudySeriesQuery {
    private Long pageSize;
    private Long current;
    private List<Integer> departmentIdList;

    private String studyDate;

    private String studyTime;

    private Integer patientAge;

    private String accessionNumber;

    private String bodyPartExamined;

    private String studyDescription;

    private  String dateOfStudyBegin;

    private  String dateOfStudyEnd;

    private  Integer patientAgeBegin;

    private  Integer patientAgeEnd;

    private  Integer patientId;

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public List<Integer> getDepartmentIdList() {
        return departmentIdList;
    }

    public void setDepartmentIdList(List<Integer> departmentIdList) {
        this.departmentIdList = departmentIdList;
    }

    public String getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(String studyDate) {
        this.studyDate = studyDate;
    }

    public String getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(String studyTime) {
        this.studyTime = studyTime;
    }


    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getBodyPartExamined() {
        return bodyPartExamined;
    }

    public void setBodyPartExamined(String bodyPartExamined) {
        this.bodyPartExamined = bodyPartExamined;
    }

    public String getStudyDescription() {
        return studyDescription;
    }

    public void setStudyDescription(String studyDescription) {
        this.studyDescription = studyDescription;
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public String getDateOfStudyBegin() {
        return dateOfStudyBegin;
    }

    public void setDateOfStudyBegin(String dateOfStudyBegin) {
        this.dateOfStudyBegin = dateOfStudyBegin;
    }

    public String getDateOfStudyEnd() {
        return dateOfStudyEnd;
    }

    public void setDateOfStudyEnd(String dateOfStudyEnd) {
        this.dateOfStudyEnd = dateOfStudyEnd;
    }

    public Integer getPatientAgeBegin() {
        return patientAgeBegin;
    }

    public void setPatientAgeBegin(Integer patientAgeBegin) {
        this.patientAgeBegin = patientAgeBegin;
    }

    public Integer getPatientAgeEnd() {
        return patientAgeEnd;
    }

    public void setPatientAgeEnd(Integer patientAgeEnd) {
        this.patientAgeEnd = patientAgeEnd;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }
}
