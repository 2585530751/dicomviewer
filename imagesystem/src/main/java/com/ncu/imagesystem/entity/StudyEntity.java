package com.ncu.imagesystem.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@TableName("study")
public class StudyEntity {
    @TableId(value = "study_id", type = IdType.AUTO)
    @ApiModelProperty(value = "检查id")
    private Integer studyId;
    @TableField(value = "study_id_dcm")
    @ApiModelProperty(value = "dicom文件中的检查id")
    private String studyIdDcm;

    @TableField(value = "study_instance_uid")
    @ApiModelProperty(value = "检查实例号")
    private String studyInstanceUid;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(value = "study_date")
    @ApiModelProperty(value = "检查日期")
    private LocalDate studyDate;
    @JsonFormat(pattern = "HH:mm:ss")
    @TableField(value = "study_time")
    @ApiModelProperty(value = "检查时间")
    private LocalTime studyTime;
    @TableField(value = "accession_number")
    @ApiModelProperty(value = "检查号，标识做检查的次序")
    private String accessionNumber;
    @TableField(value = "modalities_in_study")
    @ApiModelProperty(value = "一个检查中含有的不同检查类型")
    private String modalitiesInStudy;
    @TableField(value = "body_part_examined")
    @ApiModelProperty(value = "检查的部位")
    private String bodyPartExamined;
    @TableField(value = "study_description")
    @ApiModelProperty(value = "检查描述")
    private String studyDescription;
    @TableField(value = "patient_age")
    @ApiModelProperty(value = "患者检查时的年龄")
    private Integer patientAge;
    @TableField(value = "patient_id")
    @ApiModelProperty(value = "患者id")
    private Integer patientId;
    @TableField(value = "creator_id")
    private Integer creatorId;
    @TableField(value = "create_time")
    private LocalDateTime createTime;
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

    public void setStudyDate(LocalDate studyDate) {
        this.studyDate = studyDate;
    }

    public void setStudyTime(LocalTime studyTime) {
        this.studyTime = studyTime;
    }

    public LocalDate getStudyDate() {
        return studyDate;
    }

    public LocalTime getStudyTime() {
        return studyTime;
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
}
