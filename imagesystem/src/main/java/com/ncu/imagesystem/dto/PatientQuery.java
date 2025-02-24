package com.ncu.imagesystem.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PatientQuery {
    @ApiModelProperty(value = "页大小")
    private Long pageSize;
    @ApiModelProperty(value = "当前页码")
    private Long current;
    @ApiModelProperty(value = "姓名")
    private String patientName;
    @ApiModelProperty(value = "性别")
    private String gender;

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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
