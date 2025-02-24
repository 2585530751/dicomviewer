package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@ApiModel
@TableName("sys_dict")
public class SysDictEntity {
    @TableId(value = "dict_id", type = IdType.AUTO)
    @ApiModelProperty(value = "字典id")
    private Integer dictId;
    @TableField(value = "dict_name")
    @ApiModelProperty(value = "字典名")
    private String dictName;
    @TableField(value = "dict_code")
    @ApiModelProperty(value = "字典编码")
    private String dictCode;
    @TableField(value = "description")
    @ApiModelProperty(value = "描述")
    private String description;
    @TableField(value = "creator_id")
    @ApiModelProperty(value = "创建者id")
    private Integer creatorId;
    @TableField(value = "creator_name")
    @ApiModelProperty(value = "创建者名")
    private String creatorName;
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @TableField(value = "update_id")
    @ApiModelProperty(value = "修改者id")
    private Integer updateId;
    @TableField(value = "update_name")
    @ApiModelProperty(value = "修改者名")
    private String updateName;
    @TableField(value = "update_time")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    @TableField(value = "is_deleted")
    @ApiModelProperty(value = "是否被删除")
    private Integer isDeleted;

    public Integer getDictId() {
        return dictId;
    }

    public void setDictId(Integer dictId) {
        this.dictId = dictId;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
