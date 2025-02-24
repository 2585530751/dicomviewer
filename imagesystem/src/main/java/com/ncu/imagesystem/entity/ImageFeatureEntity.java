package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.opencsv.bean.CsvBindByName;

@TableName("image_feature")
public class ImageFeatureEntity {
    @TableId(value = "feature_id", type = IdType.AUTO)
    private Integer featureId;
    @TableField(value = "model_res_id")
    private Integer modelResId;
    @CsvBindByName(column = "file_name")
    @TableField(value = "file_name")
    private String file_name;
    @CsvBindByName(column = "original_firstorder_10Percentile")
    @TableField(value = "original_firstorder_10Percentile")
    private String original_firstorder_10Percentile;
    @CsvBindByName(column = "original_firstorder_90Percentile")
    @TableField(value = "original_firstorder_90Percentile")
    private String original_firstorder_90Percentile;
    @CsvBindByName(column = "original_firstorder_Energy")
    @TableField(value = "original_firstorder_Energy")
    private String original_firstorder_Energy;
    @CsvBindByName(column = "original_firstorder_Entropy")
    @TableField(value = "original_firstorder_Entropy")
    private String original_firstorder_Entropy;
    @CsvBindByName(column = "original_firstorder_InterquartileRange")
    @TableField(value = "original_firstorder_InterquartileRange")
    private String original_firstorder_InterquartileRange;
    @CsvBindByName(column = "original_firstorder_Kurtosis")
    @TableField(value = "original_firstorder_Kurtosis")
    private String original_firstorder_Kurtosis;
    @CsvBindByName(column = "original_firstorder_Maximum")
    @TableField(value = "original_firstorder_Maximum")
    private String original_firstorder_Maximum;
    @CsvBindByName(column = "original_firstorder_MeanAbsoluteDeviation")
    @TableField(value = "original_firstorder_MeanAbsoluteDeviation")
    private String original_firstorder_MeanAbsoluteDeviation;
    @CsvBindByName(column = "original_firstorder_Mean")
    @TableField(value = "original_firstorder_Mean")
    private String original_firstorder_Mean;
    @CsvBindByName(column = "original_firstorder_Median")
    @TableField(value = "original_firstorder_Median")
    private String original_firstorder_Median;
    @CsvBindByName(column = "original_firstorder_Minimum")
    @TableField(value = "original_firstorder_Minimum")
    private String original_firstorder_Minimum;
    @CsvBindByName(column = "original_firstorder_Range")
    @TableField(value = "original_firstorder_Range")
    private String original_firstorder_Range;
    @CsvBindByName(column = "original_firstorder_RobustMeanAbsoluteDeviation")
    @TableField(value = "original_firstorder_RobustMeanAbsoluteDeviation")
    private String original_firstorder_RobustMeanAbsoluteDeviation;
    @CsvBindByName(column = "original_firstorder_RootMeanSquared")
    @TableField(value = "original_firstorder_RootMeanSquared")
    private String original_firstorder_RootMeanSquared;
    @CsvBindByName(column = "original_firstorder_Skewness")
    @TableField(value = "original_firstorder_Skewness")
    private String original_firstorder_Skewness;
    @CsvBindByName(column = "original_firstorder_TotalEnergy")
    @TableField(value = "original_firstorder_TotalEnergy")
    private String original_firstorder_TotalEnergy;
    @CsvBindByName(column = "original_firstorder_Uniformity")
    @TableField(value = "original_firstorder_Uniformity")
    private String original_firstorder_Uniformity;
    @CsvBindByName(column = "original_firstorder_Variance")
    @TableField(value = "original_firstorder_Variance")
    private String original_firstorder_Variance;

    public Integer getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Integer featureId) {
        this.featureId = featureId;
    }

    public Integer getModelResId() {
        return modelResId;
    }

    public void setModelResId(Integer modelResId) {
        this.modelResId = modelResId;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getOriginal_firstorder_10Percentile() {
        return original_firstorder_10Percentile;
    }

    public void setOriginal_firstorder_10Percentile(String original_firstorder_10Percentile) {
        this.original_firstorder_10Percentile = original_firstorder_10Percentile;
    }

    public String getOriginal_firstorder_90Percentile() {
        return original_firstorder_90Percentile;
    }

    public void setOriginal_firstorder_90Percentile(String original_firstorder_90Percentile) {
        this.original_firstorder_90Percentile = original_firstorder_90Percentile;
    }

    public String getOriginal_firstorder_Energy() {
        return original_firstorder_Energy;
    }

    public void setOriginal_firstorder_Energy(String original_firstorder_Energy) {
        this.original_firstorder_Energy = original_firstorder_Energy;
    }

    public String getOriginal_firstorder_Entropy() {
        return original_firstorder_Entropy;
    }

    public void setOriginal_firstorder_Entropy(String original_firstorder_Entropy) {
        this.original_firstorder_Entropy = original_firstorder_Entropy;
    }

    public String getOriginal_firstorder_InterquartileRange() {
        return original_firstorder_InterquartileRange;
    }

    public void setOriginal_firstorder_InterquartileRange(String original_firstorder_InterquartileRange) {
        this.original_firstorder_InterquartileRange = original_firstorder_InterquartileRange;
    }

    public String getOriginal_firstorder_Kurtosis() {
        return original_firstorder_Kurtosis;
    }

    public void setOriginal_firstorder_Kurtosis(String original_firstorder_Kurtosis) {
        this.original_firstorder_Kurtosis = original_firstorder_Kurtosis;
    }

    public String getOriginal_firstorder_Maximum() {
        return original_firstorder_Maximum;
    }

    public void setOriginal_firstorder_Maximum(String original_firstorder_Maximum) {
        this.original_firstorder_Maximum = original_firstorder_Maximum;
    }

    public String getOriginal_firstorder_MeanAbsoluteDeviation() {
        return original_firstorder_MeanAbsoluteDeviation;
    }

    public void setOriginal_firstorder_MeanAbsoluteDeviation(String original_firstorder_MeanAbsoluteDeviation) {
        this.original_firstorder_MeanAbsoluteDeviation = original_firstorder_MeanAbsoluteDeviation;
    }

    public String getOriginal_firstorder_Mean() {
        return original_firstorder_Mean;
    }

    public void setOriginal_firstorder_Mean(String original_firstorder_Mean) {
        this.original_firstorder_Mean = original_firstorder_Mean;
    }

    public String getOriginal_firstorder_Median() {
        return original_firstorder_Median;
    }

    public void setOriginal_firstorder_Median(String original_firstorder_Median) {
        this.original_firstorder_Median = original_firstorder_Median;
    }

    public String getOriginal_firstorder_Minimum() {
        return original_firstorder_Minimum;
    }

    public void setOriginal_firstorder_Minimum(String original_firstorder_Minimum) {
        this.original_firstorder_Minimum = original_firstorder_Minimum;
    }

    public String getOriginal_firstorder_Range() {
        return original_firstorder_Range;
    }

    public void setOriginal_firstorder_Range(String original_firstorder_Range) {
        this.original_firstorder_Range = original_firstorder_Range;
    }

    public String getOriginal_firstorder_RobustMeanAbsoluteDeviation() {
        return original_firstorder_RobustMeanAbsoluteDeviation;
    }

    public void setOriginal_firstorder_RobustMeanAbsoluteDeviation(String original_firstorder_RobustMeanAbsoluteDeviation) {
        this.original_firstorder_RobustMeanAbsoluteDeviation = original_firstorder_RobustMeanAbsoluteDeviation;
    }

    public String getOriginal_firstorder_RootMeanSquared() {
        return original_firstorder_RootMeanSquared;
    }

    public void setOriginal_firstorder_RootMeanSquared(String original_firstorder_RootMeanSquared) {
        this.original_firstorder_RootMeanSquared = original_firstorder_RootMeanSquared;
    }

    public String getOriginal_firstorder_Skewness() {
        return original_firstorder_Skewness;
    }

    public void setOriginal_firstorder_Skewness(String original_firstorder_Skewness) {
        this.original_firstorder_Skewness = original_firstorder_Skewness;
    }

    public String getOriginal_firstorder_TotalEnergy() {
        return original_firstorder_TotalEnergy;
    }

    public void setOriginal_firstorder_TotalEnergy(String original_firstorder_TotalEnergy) {
        this.original_firstorder_TotalEnergy = original_firstorder_TotalEnergy;
    }

    public String getOriginal_firstorder_Uniformity() {
        return original_firstorder_Uniformity;
    }

    public void setOriginal_firstorder_Uniformity(String original_firstorder_Uniformity) {
        this.original_firstorder_Uniformity = original_firstorder_Uniformity;
    }

    public String getOriginal_firstorder_Variance() {
        return original_firstorder_Variance;
    }

    public void setOriginal_firstorder_Variance(String original_firstorder_Variance) {
        this.original_firstorder_Variance = original_firstorder_Variance;
    }
}
