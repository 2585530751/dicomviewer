package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@TableName("sys_user_perm_relation")
public class SysUserPermRelationEntity {

    @TableId(value = "upr_id", type = IdType.AUTO)
    @ApiModelProperty(value = "关系id")
    private Integer uprId;
    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    @TableField(value = "permission_id")
    @ApiModelProperty(value = "权限id")
    private Integer permissionId;
    @TableField(value = "permission_type")
    @ApiModelProperty(value = "权限类型")
    private Integer permissionType;

    public SysUserPermRelationEntity() {
    }

    public SysUserPermRelationEntity(Integer userId, Integer permissionId) {
        this.userId = userId;
        this.permissionId = permissionId;
    }

    public Integer getUprId() {
        return uprId;
    }

    public void setUprId(Integer uprId) {
        this.uprId = uprId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
