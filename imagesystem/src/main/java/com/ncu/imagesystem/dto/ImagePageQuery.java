package com.ncu.imagesystem.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ImagePageQuery {
    @ApiModelProperty(value = "页大小")
    private Long pageSize;
    @ApiModelProperty(value = "当前页码")
    private Long current;
    @ApiModelProperty(value = "病人名")
    private String patientName;
    @ApiModelProperty(value = "医生id")
    private Integer userId;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
