package com.ncu.imagesystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ncu.imagesystem.entity.SeriesEntity;

import java.time.LocalDateTime;
import java.util.List;

public class StudySeriesDTO {
    private Integer studyId;
    private String studyIdDcm;
    private String studyInstanceUid;
    private String studyDate;
    private String studyTime;
    private String accessionNumber;
    private String modalitiesInStudy;
    private String bodyPartExamined;
    private String studyDescription;
    private Integer patientAge;
    private Integer patientId;
    private Integer creatorId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private List<SeriesEntity> seriesList;

    public Integer getStudyId() {
        return studyId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
    }

    public String getStudyIdDcm() {
        return studyIdDcm;
    }

    public void setStudyIdDcm(String studyIdDcm) {
        this.studyIdDcm = studyIdDcm;
    }

    public String getStudyInstanceUid() {
        return studyInstanceUid;
    }

    public void setStudyInstanceUid(String studyInstanceUid) {
        this.studyInstanceUid = studyInstanceUid;
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

    public String getModalitiesInStudy() {
        return modalitiesInStudy;
    }

    public void setModalitiesInStudy(String modalitiesInStudy) {
        this.modalitiesInStudy = modalitiesInStudy;
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

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public List<SeriesEntity> getSeriesList() {
        return seriesList;
    }

    public void setSeriesList(List<SeriesEntity> seriesList) {
        this.seriesList = seriesList;
    }
}
