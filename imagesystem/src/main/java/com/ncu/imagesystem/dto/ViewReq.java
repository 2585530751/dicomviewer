package com.ncu.imagesystem.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description= "阅片流程对象")
public class ViewReq {
    @ApiModelProperty(value = "序列id")
    private Integer seriesId;
    @ApiModelProperty(value = "图像id")
    private Integer imageId;
    @ApiModelProperty(value = "阅片员意见")
    private String readerView;

    @ApiModelProperty(value = "医生意见")
    private String doctorView;

    public Integer getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getReaderView() {
        return readerView;
    }

    public void setReaderView(String readerView) {
        this.readerView = readerView;
    }

    public String getDoctorView() {
        return doctorView;
    }

    public void setDoctorView(String doctorView) {
        this.doctorView = doctorView;
    }
}
