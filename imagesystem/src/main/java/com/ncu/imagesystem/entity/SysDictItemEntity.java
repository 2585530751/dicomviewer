package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@ApiModel
@TableName("sys_dict_item")
public class SysDictItemEntity {
    @TableId(value = "dict_item_id", type = IdType.AUTO)
    @ApiModelProperty(value = "字典项id")
    private Integer dictItemId;
    @TableField(value = "dict_id")
    @ApiModelProperty(value = "字典id")
    private Integer dictId;
    @TableField(value = "dict_item_text")
    @ApiModelProperty(value = "字典项文本")
    private String dictItemText;
    @TableField(value = "dict_item_value")
    @ApiModelProperty(value = "字典项值")
    private Integer dictItemValue;
    @TableField(value = "description")
    @ApiModelProperty(value = "描述")
    private String description;
    @TableField(value = "sort_order")
    @ApiModelProperty(value = "排序")
    private Integer sortOrder;
    @TableField(value = "status")
    @ApiModelProperty(value = "状态")
    private Integer status;
    @TableField(value = "creator_id")
    @ApiModelProperty(value = "创建人id")
    private Integer creatorId;
    @TableField(value = "creator_name")
    @ApiModelProperty(value = "创建人名")
    private String creatorName;
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @TableField(value = "update_id")
    @ApiModelProperty(value = "修改人id")
    private Integer updateId;
    @TableField(value = "update_name")
    @ApiModelProperty(value = "修改人名")
    private String updateName;
    @TableField(value = "update_time")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    public Integer getDictItemId() {
        return dictItemId;
    }

    public void setDictItemId(Integer dictItemId) {
        this.dictItemId = dictItemId;
    }

    public Integer getDictId() {
        return dictId;
    }

    public void setDictId(Integer dictId) {
        this.dictId = dictId;
    }

    public String getDictItemText() {
        return dictItemText;
    }

    public void setDictItemText(String dictItemText) {
        this.dictItemText = dictItemText;
    }

    public Integer getDictItemValue() {
        return dictItemValue;
    }

    public void setDictItemValue(Integer dictItemValue) {
        this.dictItemValue = dictItemValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
}
