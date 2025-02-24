package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@TableName("doctor")
public class DoctorEntity {
    @TableId(value = "doctor_id", type = IdType.AUTO)
    @ApiModelProperty(value = "医生id")
    private Integer doctorId;
    @TableField(value = "doctor_name")
    @ApiModelProperty(value = "姓名")
    private String doctorName;
    @TableField(value = "phone_number")
    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;
    @TableField(value = "email")
    @ApiModelProperty(value = "电子邮件")
    private String email;
    @TableField(value = "org_id")
    @ApiModelProperty(value = "组织id")
    private Integer orgId;
    @TableField(value = "org_name")
    @ApiModelProperty(value = "组织名")
    private String orgName;
    @TableField(value = "introduction")
    @ApiModelProperty(value = "简介")
    private String introduction;
    @TableField(value = "speciality")
    @ApiModelProperty(value = "擅长")
    private String speciality;
    @TableField(value = "level")
    @ApiModelProperty(value = "职位")
    private Integer level;
    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
