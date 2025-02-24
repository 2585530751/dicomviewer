package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@TableName("sys_group_perm_relation")
public class SysGroupPermRelationEntity {

    @TableId(value = "gpr_id", type = IdType.AUTO)
    @ApiModelProperty(value = "关系id")
    private Integer gprId;
    @TableField(value = "group_id")
    @ApiModelProperty(value = "组id")
    private Integer groupId;
    @TableField(value = "permission_id")
    @ApiModelProperty(value = "权限id")
    private Integer permissionId;
    @TableField(value = "permission_type")
    @ApiModelProperty(value = "权限类型")
    private Integer permissionType;

    public SysGroupPermRelationEntity() {
    }

    public SysGroupPermRelationEntity(Integer groupId, Integer permissionId) {
        this.groupId = groupId;
        this.permissionId = permissionId;
    }

    public Integer getGprId() {
        return gprId;
    }

    public void setGprId(Integer gprId) {
        this.gprId = gprId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public Integer getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(Integer permissionType) {
        this.permissionType = permissionType;
    }
}
