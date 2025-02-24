package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
@ApiModel
@TableName("series")
public class SeriesEntity {
    @TableId(value = "series_id", type = IdType.AUTO)
    @ApiModelProperty(value = "图像id")
    private Integer seriesId;
    @TableField(value = "series_name")
    @ApiModelProperty(value = "图像名")
    private String seriesName;
    @TableField(value = "series_equipment")
    @ApiModelProperty(value = "生成图像的设备")
    private String seriesEquipment;
    @TableField(value = "series_format")
    @ApiModelProperty(value = "图像格式")
    private String seriesFormat;
    @TableField(value = "series_path")
    @ApiModelProperty(value = "图像保存路径")
    private String seriesPath;
    @TableField(value = "series_preview_path")
    @ApiModelProperty(value = "图像描述")
    private String seriesPreviewPath;
    @TableField(value = "series_count")
    @ApiModelProperty(value = "图像序列数量")
    private Integer seriesCount;
    @TableField(value = "series_check_part")
    @ApiModelProperty(value = "图像检查部位")
    private String seriesCheckPart;
    @TableField(value = "series_check_time")
    @ApiModelProperty(value = "图像检查时间")
    private Date seriesCheckTime;
    @TableField(value = "patient_id")
    @ApiModelProperty(value = "病人id")
    private Integer patientId;
    @TableField(value = "patient_name")
    @ApiModelProperty(value = "病人名")
    private String patientName;
    @TableField(value = "creator_id")
    @ApiModelProperty(value = "创建者id")
    private Integer creatorId;
    @TableField(value = "creator_name")
    @ApiModelProperty(value = "创建者名")
    private String creatorName;
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @TableField(value = "series_status")
    @ApiModelProperty(value = "图像状态")
    private Integer seriesStatus;
    @TableField(value = "is_deleted")
    @ApiModelProperty(value = "是否被删除")
    private Integer isDeleted;
    @TableField(value = "series_number")
    @ApiModelProperty(value = "序列id")
    private String seriesNumber;
    @TableField(value = "series_instance_uid")
    @ApiModelProperty(value = "序列实例uid")
    private String seriesInstanceUid;
    @TableField(value = "series_modality")
    @ApiModelProperty(value = "检查模态")
    private String seriesModality;
    @TableField(value = "series_desc")
    @ApiModelProperty(value = "序列描述")
    private String seriesDesc;
    @TableField(value = "series_date")
    @ApiModelProperty(value = "检查日期")
    private LocalDate seriesDate;
    @TableField(value = "series_time")
    @ApiModelProperty(value = "检查时间")
    private LocalTime seriesTime;
    @TableField(value = "image_position")
    @ApiModelProperty(value = "图像位置")
    private String imagePosition;
    @TableField(value = "spacing_between_slices")
    @ApiModelProperty(value = "层与层之间的间距")
    private String spacingBetweenSlices;
    @TableField(value = "mr_acquisition_type")
    @ApiModelProperty(value = "MR图像的采集类型")
    private String mrAcquisitionType;
    @TableField(value = "study_id")
    @ApiModelProperty(value = "检查id")
    private Integer studyId;
    @TableField(value = "reader_view")
    private String readerView;
    @TableField(value = "doctor_view")
    private String doctorView;
    @TableField(value = "mark_series_preview_path")
    private String markSeriesPreviewPath;
    @TableField(value = "mark_series_name")
    private String markSeriesName;
    @TableField(value = "reader_view_id")
    private Integer readerViewId;
    @TableField(value = "reader_view_name")
    private String readerViewName;
    @TableField(value = "reader_view_time")
    private Date readerViewTime;
    @TableField(value = "doctor_view_id")
    private Integer doctorViewId;
    @TableField(value = "doctor_view_name")
    private String doctorViewName;
    @TableField(value = "doctor_view_time")
    private Date doctorViewTime;

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
}
