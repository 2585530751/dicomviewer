package com.ncu.imagesystem.dto;

import com.ncu.imagesystem.entity.DoctorEntity;
import com.ncu.imagesystem.entity.ImageEntity;
import com.ncu.imagesystem.entity.SeriesEntity;

import java.util.List;

public class ImageSetDTO {

    private SeriesEntity series;

    private DoctorEntity doctor;

    private List<ImageEntity> singleImageList;

    public SeriesEntity getImage() {
        return series;
    }

    public void setImage(SeriesEntity series) {
        this.series = series;
    }

    public DoctorEntity getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorEntity doctor) {
        this.doctor = doctor;
    }

    public List<ImageEntity> getSingleImageList() {
        return singleImageList;
    }

    public void setSingleImageList(List<ImageEntity> singleImageList) {
        this.singleImageList = singleImageList;
    }
}
