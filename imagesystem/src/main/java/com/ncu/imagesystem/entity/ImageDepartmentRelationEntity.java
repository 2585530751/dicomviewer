package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@TableName("series_department_relation")
public class ImageDepartmentRelationEntity {
    @TableId(value = "idr_id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer idrId;
    @TableField(value = "series_id")
    @ApiModelProperty(value = "图像序列id")
    private Integer seriesId;
    @TableField(value = "department_id")
    @ApiModelProperty(value = "科室id")
    private Integer departmentId;
    @TableField(value = "department_name")
    @ApiModelProperty(value = "科室名")
    private String departmentName;
    @TableField(value = "department_desc")
    @ApiModelProperty(value = "科室名称")
    private String departmentDesc;

    public Integer getIdrId() {
        return idrId;
    }

    public void setIdrId(Integer idrId) {
        this.idrId = idrId;
    }

    public Integer getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentDesc() {
        return departmentDesc;
    }

    public void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
    }
}
