package com.ncu.imagesystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ncu.imagesystem.entity.ImageEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class SeriesImageDTO {

    private Integer seriesId;
    private String seriesName;
    private String seriesEquipment;
    private String seriesFormat;
    private String seriesPath;
    private String seriesPreviewPath;
    private Integer seriesCount;
    private String seriesCheckPart;
    private Date seriesCheckTime;
    private Integer patientId;
    private String patientName;
    private Integer creatorId;
    private String creatorName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Integer seriesStatus;
    private Integer isDeleted;
    private String seriesNumber;
    private String seriesInstanceUid;
    private String seriesModality;
    private String seriesDesc;
    private LocalDate seriesDate;
    private LocalTime seriesTime;
    private String imagePosition;
    private String spacingBetweenSlices;
    private String mrAcquisitionType;
    private Integer studyId;
    private String readerView;
    private String doctorView;
    private String markSeriesPreviewPath;
    private String markSeriesName;
    private Integer readerViewId;
    private String readerViewName;
    private Date readerViewTime;
    private Integer doctorViewId;
    private String doctorViewName;
    private Date doctorViewTime;
    private List<ImageEntity> imageList;

    public Integer getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesEquipment() {
        return seriesEquipment;
    }

    public void setSeriesEquipment(String seriesEquipment) {
        this.seriesEquipment = seriesEquipment;
    }

    public String getSeriesFormat() {
        return seriesFormat;
    }

    public void setSeriesFormat(String seriesFormat) {
        this.seriesFormat = seriesFormat;
    }

    public String getSeriesPath() {
        return seriesPath;
    }

    public void setSeriesPath(String seriesPath) {
        this.seriesPath = seriesPath;
    }

    public String getSeriesPreviewPath() {
        return seriesPreviewPath;
    }

    public void setSeriesPreviewPath(String seriesPreviewPath) {
        this.seriesPreviewPath = seriesPreviewPath;
    }

    public Integer getSeriesCount() {
        return seriesCount;
    }

    public void setSeriesCount(Integer seriesCount) {
        this.seriesCount = seriesCount;
    }

    public String getSeriesCheckPart() {
        return seriesCheckPart;
    }

    public void setSeriesCheckPart(String seriesCheckPart) {
        this.seriesCheckPart = seriesCheckPart;
    }

    public Date getSeriesCheckTime() {
        return seriesCheckTime;
    }

    public void setSeriesCheckTime(Date seriesCheckTime) {
        this.seriesCheckTime = seriesCheckTime;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getSeriesStatus() {
        return seriesStatus;
    }

    public void setSeriesStatus(Integer seriesStatus) {
        this.seriesStatus = seriesStatus;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getSeriesInstanceUid() {
        return seriesInstanceUid;
    }

    public void setSeriesInstanceUid(String seriesInstanceUid) {
        this.seriesInstanceUid = seriesInstanceUid;
    }

    public String getSeriesModality() {
        return seriesModality;
    }

    public void setSeriesModality(String seriesModality) {
        this.seriesModality = seriesModality;
    }

    public String getSeriesDesc() {
        return seriesDesc;
    }

    public void setSeriesDesc(String seriesDesc) {
        this.seriesDesc = seriesDesc;
    }

    public LocalDate getSeriesDate() {
        return seriesDate;
    }

    public void setSeriesDate(LocalDate seriesDate) {
        this.seriesDate = seriesDate;
    }

    public LocalTime getSeriesTime() {
        return seriesTime;
    }

    public void setSeriesTime(LocalTime seriesTime) {
        this.seriesTime = seriesTime;
    }

    public String getImagePosition() {
        return imagePosition;
    }

    public void setImagePosition(String imagePosition) {
        this.imagePosition = imagePosition;
    }

    public String getSpacingBetweenSlices() {
        return spacingBetweenSlices;
    }

    public void setSpacingBetweenSlices(String spacingBetweenSlices) {
        this.spacingBetweenSlices = spacingBetweenSlices;
    }

    public String getMrAcquisitionType() {
        return mrAcquisitionType;
    }

    public void setMrAcquisitionType(String mrAcquisitionType) {
        this.mrAcquisitionType = mrAcquisitionType;
    }

    public Integer getStudyId() {
        return studyId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
    }

    public String getReaderView() {
        return readerView;
    }

    public void setReaderView(String readerView) {
        this.readerView = readerView;
    }

    public String getDoctorView() {
        return doctorView;
    }

    public void setDoctorView(String doctorView) {
        this.doctorView = doctorView;
    }

    public String getMarkSeriesPreviewPath() {
        return markSeriesPreviewPath;
    }

    public void setMarkSeriesPreviewPath(String markSeriesPreviewPath) {
        this.markSeriesPreviewPath = markSeriesPreviewPath;
    }

    public String getMarkSeriesName() {
        return markSeriesName;
    }

    public void setMarkSeriesName(String markSeriesName) {
        this.markSeriesName = markSeriesName;
    }

    public Integer getReaderViewId() {
        return readerViewId;
    }

    public void setReaderViewId(Integer readerViewId) {
        this.readerViewId = readerViewId;
    }

    public String getReaderViewName() {
        return readerViewName;
    }

    public void setReaderViewName(String readerViewName) {
        this.readerViewName = readerViewName;
    }

    public Date getReaderViewTime() {
        return readerViewTime;
    }

    public void setReaderViewTime(Date readerViewTime) {
        this.readerViewTime = readerViewTime;
    }

    public Integer getDoctorViewId() {
        return doctorViewId;
    }

    public void setDoctorViewId(Integer doctorViewId) {
        this.doctorViewId = doctorViewId;
    }

    public String getDoctorViewName() {
        return doctorViewName;
    }

    public void setDoctorViewName(String doctorViewName) {
        this.doctorViewName = doctorViewName;
    }

    public Date getDoctorViewTime() {
        return doctorViewTime;
    }

    public void setDoctorViewTime(Date doctorViewTime) {
        this.doctorViewTime = doctorViewTime;
    }

    public List<ImageEntity> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageEntity> imageList) {
        this.imageList = imageList;
    }
}
