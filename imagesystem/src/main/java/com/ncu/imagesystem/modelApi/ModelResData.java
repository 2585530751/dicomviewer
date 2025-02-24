package com.ncu.imagesystem.modelApi;

public class ModelResData {

    private Integer seriesId;
    private Integer singleImageId;
    private String resultPath;
    private String resultName;
    private String resultDes;

    public Integer getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    public Integer getSingleImageId() {
        return singleImageId;
    }

    public void setSingleImageId(Integer singleImageId) {
        this.singleImageId = singleImageId;
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
