package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
@TableName("model")
public class ModelEntity {

    @TableId(value = "model_id", type = IdType.AUTO)
    @ApiModelProperty(value = "模型id")
    private Integer modelId;
    @TableField(value = "model_name")
    @ApiModelProperty(value = "模型名")
    private String modelName;
    @TableField(value = "model_description")
    @ApiModelProperty(value = "描述")
    private String modelDescription;
    @TableField(value = "model_image")
    @ApiModelProperty(value = "图片路径")
    private String modelImage;

    @TableField(value = "model_abstract")
    @ApiModelProperty(value = "模型摘要")
    private String modelAbstract;

    @TableField(value = "model_paper_link")
    @ApiModelProperty(value = "论文链接")
    private String modelPaperLink;

    @TableField(value = "model_code_link")
    @ApiModelProperty(value = "代码链接")
    private String modelCodeLink;

    @TableField(value = "create_time")
    @ApiModelProperty(value = "论文年份")
    private Date createTime;

    @TableField(value = "model_pattern")
    @ApiModelProperty(value = "代码链接")
    private String modelPattern;

    @TableField(value = "creator_id")
    @ApiModelProperty(value = "代码链接")
    private Integer creatorId;

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelImage() {
        return modelImage;
    }

    public void setModelImage(String modelImage) {
        this.modelImage = modelImage;
    }

    public String getModelDescription() {
        return modelDescription;
    }

    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

    public String getModelAbstract() {
        return modelAbstract;
    }

    public void setModelAbstract(String modelAbstract) {
        this.modelAbstract = modelAbstract;
    }

    public String getModelPaperLink() {
        return modelPaperLink;
    }

    public void setModelPaperLink(String modelPaperLink) {
        this.modelPaperLink = modelPaperLink;
    }

    public String getModelCodeLink() {
        return modelCodeLink;
    }

    public void setModelCodeLink(String modelCodeLink) {
        this.modelCodeLink = modelCodeLink;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModelPattern() {
        return modelPattern;
    }

    public void setModelPattern(String modelPattern) {
        this.modelPattern = modelPattern;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }
}
