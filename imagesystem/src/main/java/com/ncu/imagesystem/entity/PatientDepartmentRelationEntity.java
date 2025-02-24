package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@TableName("patient_department_relation")
public class PatientDepartmentRelationEntity {
    @TableId(value = "pdr_id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer pdrId;
    @TableField(value = "patient_id")
    @ApiModelProperty(value = "病人id")
    private Integer patientId;
    @TableField(value = "department_id")
    @ApiModelProperty(value = "科室id")
    private Integer departmentId;
    @TableField(value = "department_name")
    @ApiModelProperty(value = "科室名")
    private String departmentName;
    @TableField(value = "department_desc")
    @ApiModelProperty(value = "科室名称")
    private String departmentDesc;

    public Integer getPdrId() {
        return pdrId;
    }

    public void setPdrId(Integer pdrId) {
        this.pdrId = pdrId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
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
