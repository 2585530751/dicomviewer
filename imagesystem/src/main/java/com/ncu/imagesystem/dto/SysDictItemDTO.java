package com.ncu.imagesystem.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class SysDictItemDTO {
    @ApiModelProperty(value = "字典id")
    private Integer dictId;
    @ApiModelProperty(value = "字典名")
    private String dictName;
    @ApiModelProperty(value = "字典编码")
    private String dictCode;
    @ApiModelProperty(value = "字典项id")
    private Integer dictItemId;
    @ApiModelProperty(value = "字典项文本")
    private String dictItemText;
    @ApiModelProperty(value = "字典项值")
    private Integer dictItemValue;
    @ApiModelProperty(value = "排序")
    private Integer sortOrder;

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

    public Integer getDictItemId() {
        return dictItemId;
    }

    public void setDictItemId(Integer dictItemId) {
        this.dictItemId = dictItemId;
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

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
