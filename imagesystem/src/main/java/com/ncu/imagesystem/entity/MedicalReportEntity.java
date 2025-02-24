package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@ApiModel
@TableName("medical_report")
public class MedicalReportEntity {
    @TableId(value = "report_id", type = IdType.AUTO)
    @ApiModelProperty(value = "报告id")
    private Integer reportId;
    @TableField(value = "report_name")
    @ApiModelProperty(value = "报告名")
    private String reportName;
    @TableField(value = "series_id")
    @ApiModelProperty(value = "图像id")
    private Integer seriesId;
    @TableField(value = "creator_id")
    @ApiModelProperty(value = "操作人id")
    private Integer creatorId;
    @TableField(value = "creator_name")
    @ApiModelProperty(value = "操作人名")
    private String creatorName;
    @TableField(value = "create_time")
    @ApiModelProperty(value = "操作时间")
    private Date createTime;
    @TableField(value = "report_data")
    @ApiModelProperty(value = "报告内容")
    private String reportData;

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Integer getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getReportData() {
        return reportData;
    }

    public void setReportData(String reportData) {
        this.reportData = reportData;
    }
}
