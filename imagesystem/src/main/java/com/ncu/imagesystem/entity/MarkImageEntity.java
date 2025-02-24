package com.ncu.imagesystem.entity;

public class MarkImageEntity {
    private Integer seriesId;
    private Integer imageId;
    private Integer modelId;
    private String markImageName;
    private String markImageDesc;

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

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getMarkImageName() {
        return markImageName;
    }

    public void setMarkImageName(String markImageName) {
        this.markImageName = markImageName;
    }

    public String getMarkImageDesc() {
        return markImageDesc;
    }

    public void setMarkImageDesc(String markImageDesc) {
        this.markImageDesc = markImageDesc;
    }
}
