package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@TableName("sys_user_group_relation")
public class SysUserGroupRelationEntity {
    @TableId(value = "ugr_id", type = IdType.AUTO)
    @ApiModelProperty(value = "关系id")
    private Integer ugrId;
    @TableField(value = "group_id")
    @ApiModelProperty(value = "组id")
    private Integer groupId;
    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    public SysUserGroupRelationEntity() {
    }

    public SysUserGroupRelationEntity(Integer groupId, Integer userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public Integer getUgrId() {
        return ugrId;
    }

    public void setUgrId(Integer ugrId) {
        this.ugrId = ugrId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
