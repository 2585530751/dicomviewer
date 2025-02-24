package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@ApiModel
@TableName("sys_group")
public class SysGroupEntity {
    @TableId(value = "group_id", type = IdType.AUTO)
    @ApiModelProperty(value = "分组id")
    private Integer groupId;
    @TableField(value = "group_parent_id")
    @ApiModelProperty(value = "父分组id")
    private Integer groupParentId;
    @TableField(value = "group_parent_name")
    @ApiModelProperty(value = "父分组名")
    private String groupParentName;
    @TableField(value = "group_name")
    @ApiModelProperty(value = "分组名")
    private String groupName;
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @TableField(value = "description")
    @ApiModelProperty(value = "描述")
    private String description;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupParentId() {
        return groupParentId;
    }

    public void setGroupParentId(Integer groupParentId) {
        this.groupParentId = groupParentId;
    }

    public String getGroupParentName() {
        return groupParentName;
    }

    public void setGroupParentName(String groupParentName) {
        this.groupParentName = groupParentName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
}
