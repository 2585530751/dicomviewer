package com.ncu.imagesystem.dto;

import com.ncu.imagesystem.entity.ImageFeatureEntity;

import java.util.Date;

public class ModelResultWithImageFeatureDTO {
    private Integer modelResId;
    private Integer seriesId;
    private Integer imageId;
    private Integer creatorId;
    private String creatorName;
    private Date createTime;
    private String resData;
    private String resultPath;
    private String resultName;
    private String resultDes;
    private Integer modelId;
    private ImageFeatureEntity imageFeature;

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

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public ImageFeatureEntity getImageFeature() {
        return imageFeature;
    }

    public void setImageFeature(ImageFeatureEntity imageFeature) {
        this.imageFeature = imageFeature;
    }
}
