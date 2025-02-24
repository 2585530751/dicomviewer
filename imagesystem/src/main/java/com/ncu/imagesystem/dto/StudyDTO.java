package com.ncu.imagesystem.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ncu.imagesystem.entity.StudyEntity;

import java.time.LocalDateTime;
import java.util.List;

public class StudyDTO {
    private Integer patientId;
    private String patientName;
    private String patientIdCardNumber;
    private String patientGender;
    private Float patientHeight;
    private Float patientWeight;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateOfBirth;
    private String phoneNumber;
    private String telephoneNumber;
    private String faxNumber;
    private String email;
    private String address;
    private Integer userId;
    private String patientIdDcm;
    private Integer creatorId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private List<StudyEntity> studyList;

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientIdCardNumber() {
        return patientIdCardNumber;
    }

    public void setPatientIdCardNumber(String patientIdCardNumber) {
        this.patientIdCardNumber = patientIdCardNumber;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public Float getPatientHeight() {
        return patientHeight;
    }

    public void setPatientHeight(Float patientHeight) {
        this.patientHeight = patientHeight;
    }

    public Float getPatientWeight() {
        return patientWeight;
    }

    public void setPatientWeight(Float patientWeight) {
        this.patientWeight = patientWeight;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPatientIdDcm() {
        return patientIdDcm;
    }

    public void setPatientIdDcm(String patientIdDcm) {
        this.patientIdDcm = patientIdDcm;
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

    public List<StudyEntity> getStudyList() {
        return studyList;
    }

    public void setStudyList(List<StudyEntity> studyList) {
        this.studyList = studyList;
    }
}
