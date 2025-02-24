package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@TableName("study_department_relation")
public class StudyDepartmentRelationEntity {
    @TableId(value = "sdr_id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer sdrId;
    @TableField(value = "study_id")
    @ApiModelProperty(value = "检查id")
    private Integer studyId;
    @TableField(value = "department_id")
    @ApiModelProperty(value = "科室id")
    private Integer departmentId;
    @TableField(value = "department_name")
    @ApiModelProperty(value = "科室名")
    private String departmentName;
    @TableField(value = "department_desc")
    @ApiModelProperty(value = "科室名称")
    private String departmentDesc;

    public Integer getSdrId() {
        return sdrId;
    }

    public void setSdrId(Integer sdrId) {
        this.sdrId = sdrId;
    }

    public Integer getStudyId() {
        return studyId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
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
