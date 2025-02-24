package com.ncu.imagesystem.dto;

import com.ncu.imagesystem.entity.ImageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;

@ApiModel(description= "图像集合")
public class ImagesReq {
    @ApiModelProperty(value = "图像")
    private ImageEntity image;
    @ApiModelProperty(value = "图像类型：CT或MRI")
    private String modality;
    @ApiModelProperty(value = "病人id")
    private Integer patientId;
    @ApiModelProperty(value = "检查id")
    private Integer studyId;
    @ApiModelProperty(value = "序列id")
    private Integer seriesId;

    public ImageEntity getImage() {
        return image;
    }

    public void setImage(ImageEntity image) {
        this.image = image;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getStudyId() {
        return studyId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
    }

    public Integer getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }
}
