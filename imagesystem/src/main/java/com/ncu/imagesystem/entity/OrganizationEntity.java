package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@ApiModel
@TableName("organization")
public class OrganizationEntity {
    @TableId(value = "org_id", type = IdType.AUTO)
    @ApiModelProperty(value = "组织id")
    private Integer orgId;
    @TableField(value = "org_parent_id")
    @ApiModelProperty(value = "父组织id")
    private Integer orgParentId;
    @TableField(value = "org_parent_name")
    @ApiModelProperty(value = "父组织名")
    private String orgParentName;
    @TableField(value = "org_name")
    @ApiModelProperty(value = "组织名")
    private String orgName;
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @TableField(value = "description")
    @ApiModelProperty(value = "描述")
    private String description;
    @TableField(value = "count")
    @ApiModelProperty(value = "人数")
    private Integer count;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getOrgParentId() {
        return orgParentId;
    }

    public void setOrgParentId(Integer orgParentId) {
        this.orgParentId = orgParentId;
    }

    public String getOrgParentName() {
        return orgParentName;
    }

    public void setOrgParentName(String orgParentName) {
        this.orgParentName = orgParentName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
