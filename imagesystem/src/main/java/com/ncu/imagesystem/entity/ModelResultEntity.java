package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ncu.imagesystem.modelApi.ModelResData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@ApiModel
@TableName("model_result")
public class ModelResultEntity {
    @TableId(value = "model_res_id", type = IdType.AUTO)
    @ApiModelProperty(value = "图像模型结果信息记录id")
    private Integer modelResId;
    @TableField(value = "series_id")
    @ApiModelProperty(value = "图像id")
    private Integer seriesId;
    @TableField(value = "image_id")
    @ApiModelProperty(value = "图像id")
    private Integer imageId;
    @TableField(value = "creator_id")
    @ApiModelProperty(value = "操作人id")
    private Integer creatorId;
    @TableField(value = "creator_name")
    @ApiModelProperty(value = "操作人名")
    private String creatorName;
    @TableField(value = "create_time")
    @ApiModelProperty(value = "操作时间")
    private Date createTime;
    @TableField(value = "res_data")
    @ApiModelProperty(value = "模型内容")
    private String resData;
    @TableField(value = "model_id")
    @ApiModelProperty(value = "模型id")
    private Integer modelId;
    @TableField(exist = false)
    private String resultPath;
    @TableField(exist = false)
    private String resultName;
    @TableField(exist = false)
    private String resultDes;

    public Integer getModelResId() {
        return modelResId;
    }

    public void setModelResId(Integer modelResId) {
        this.modelResId = modelResId;
    }

    public Integer getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
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

    public String getResData() {
        return resData;
    }

    public void setResData(String resData) {
        this.resData = resData;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }

    public String getResultName() {
        return resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public String getResultDes() {
        return resultDes;
    }

    public void setResultDes(String resultDes) {
        this.resultDes = resultDes;
    }
}
