package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@TableName("series_doctor_relation")
public class ImageDoctorRelationEntity {
    @TableId(value = "idr_id", type = IdType.AUTO)
    @ApiModelProperty(value = "关系id")
    private Integer idrId;
    @TableField(value = "series_id")
    @ApiModelProperty(value = "图像集id")
    private Integer seriesId;
    @TableField(value = "user_id")
    @ApiModelProperty(value = "医生id")
    private Integer userId;

    public Integer getIdrId() {
        return idrId;
    }

    public void setIdrId(Integer idrId) {
        this.idrId = idrId;
    }

    public Integer getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
