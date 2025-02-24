package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Date;

@ApiModel
@TableName("patient")
public class PatientEntity {
    @TableId(value = "patient_id", type = IdType.AUTO)
    @ApiModelProperty(value = "病人id")
    private Integer patientId;
    @TableField(value = "patient_name", condition = SqlCondition.LIKE)
    @ApiModelProperty(value = "姓名")
    private String patientName;
    @TableField(value = "patient_id_card_number")
    @ApiModelProperty(value = "身份证号")
    private String patientIdCardNumber;
    @TableField(value = "patient_gender")
    @ApiModelProperty(value = "性别")
    private String patientGender;
    @TableField(value = "patient_height")
    @ApiModelProperty(value = "身高")
    private Float patientHeight;
    @TableField(value = "patient_weight")
    @ApiModelProperty(value = "体重")
    private Float patientWeight;

    @TableField(value = "date_of_birth")
    @ApiModelProperty(value = "出生日期")
    private LocalDateTime dateOfBirth;
    @TableField(value = "phone_number")
    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;
    @TableField(value = "telephone_number")
    @ApiModelProperty(value = "电话号码")
    private String telephoneNumber;
    @TableField(value = "fax_number")
    @ApiModelProperty(value = "传真号码")
    private String faxNumber;
    @TableField(value = "email")
    @ApiModelProperty(value = "电子邮件")
    private String email;
    @TableField(value = "address")
    @ApiModelProperty(value = "住址")
    private String address;
    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    @TableField(value = "patient_id_dcm")
    @ApiModelProperty(value = "患者id")
    private String patientIdDcm;
    @TableField(value = "creator_id")
    private Integer creatorId;
    @TableField(value = "create_time")
    private LocalDateTime createTime;
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


    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
}
