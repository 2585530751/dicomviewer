package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@ApiModel
@TableName("sys_permission")
public class SysPermissionEntity {
    @TableId(value = "permission_id", type = IdType.AUTO)
    @ApiModelProperty(value = "权限id")
    private Integer permissionId;
    @TableField(value = "permission_parent_id")
    @ApiModelProperty(value = "父权限id")
    private Integer permissionParentId;
    @TableField(value = "permission_parent_name")
    @ApiModelProperty(value = "父权限名")
    private String permissionParentName;
    @TableField(value = "permission_name")
    @ApiModelProperty(value = "权限名")
    private String permissionName;
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @TableField(value = "description")
    @ApiModelProperty(value = "描述")
    private String description;

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public Integer getPermissionParentId() {
        return permissionParentId;
    }

    public void setPermissionParentId(Integer permissionParentId) {
        this.permissionParentId = permissionParentId;
    }

    public String getPermissionParentName() {
        return permissionParentName;
    }

    public void setPermissionParentName(String permissionParentName) {
        this.permissionParentName = permissionParentName;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
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
