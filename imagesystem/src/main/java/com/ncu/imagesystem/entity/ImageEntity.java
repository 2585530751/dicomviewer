package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@ApiModel
@TableName("image")
public class ImageEntity {

    @TableId(value = "image_id", type = IdType.AUTO)
    @ApiModelProperty(value = "图像文件id")
    private Integer imageId;
    @TableField(value = "image_name")
    @ApiModelProperty(value = "图像文件名")
    private String imageName;

    @TableField(value = "image_path")
    @ApiModelProperty(value = "图像文件保存路径")
    private String imagePath;

    @TableField(value = "type")
    @ApiModelProperty(value = "图像文件类型")
    private Integer type;

    @TableField(value = "series_id")
    @ApiModelProperty(value = "图像文件关联的图像id")
    private Integer seriesId;

    @TableField(value = "image_id_rel")
    @ApiModelProperty(value = "图像文件关联的图像文件id")
    private Integer imageIdRel;

    @TableField(value = "operate_id")
    @ApiModelProperty(value = "操作人id")
    private Integer operateId;

    @TableField(value = "operate_name")
    @ApiModelProperty(value = "操作人名称")
    private String operateName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "operate_time")
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime operateTime;

    @TableField(value = "image_status")
    @ApiModelProperty(value = "图像文件状态")
    private Integer imageStatus;

    @TableField(value = "image_equipment")
    @ApiModelProperty(value = "成像设备")
    private String imageEquipment;

    @TableField(value = "image_format")
    @ApiModelProperty(value = "图像文件格式")
    private String imageFormat;

    @TableField(value = "image_desc")
    @ApiModelProperty(value = "图像文件描述")
    private String imageDesc;

    @TableField(value = "image_check_part")
    @ApiModelProperty(value = "检查部位")
    private String imageCheckPart;

    @TableField(value = "image_check_time")
    @ApiModelProperty(value = "成像时间")
    private Date imageCheckTime;

    @TableField(value = "patient_id")
    @ApiModelProperty(value = "病人id")
    private Integer patientId;

    @TableField(value = "patient_name")
    @ApiModelProperty(value = "病人名称")
    private String patientName;

    @TableField(value = "is_deleted")
    @ApiModelProperty(value = "是否删除了")
    private Integer isDeleted;
    @TableField(value = "image_position")
    private String imagePosition;
    @TableField(value = "image_orientation")
    private String imageOrientation;
    @TableField(value = "slice_thickness")
    private String sliceThickness;
    @TableField(value = "slice_location")
    private String sliceLocation;
    @TableField(value = "image_rows")
    private String imageRows;
    @TableField(value = "image_columns")
    private String imageColumns;
    @TableField(value = "pixel_spacing")
    private String pixelSpacing;
    @TableField(value = "bits_allocated")
    private String bitsAllocated;
    @TableField(value = "bits_stored")
    private String bitsStored;
    @TableField(value = "high_bit")
    private String highBit;
    @TableField(value = "pixel_representation")
    private String pixelRepresentation;
    @TableField(value = "window_center")
    private String windowCenter;
    @TableField(value = "window_width")
    private String windowWidth;
    @TableField(value = "rescale_intercept")
    private String rescaleIntercept;
    @TableField(value = "rescale_slope")
    private String rescaleSlope;
    @TableField(value = "rescale_type")
    private String rescaleType;
    @TableField(value = "image_type")
    private String imageType;
    @TableField(value = "sop_instance_uid")
    private String sopInstanceUid;
    @TableField(value = "content_date")
    private LocalDate contentDate;
    @TableField(value = "content_time")
    private LocalTime contentTime;
    @TableField(value = "image_number")
    private String imageNumber;
    @TableField(value = "samples_per_pixel")
    private String samplesPerPixel;
    @TableField(value = "photometric_interpretation")
    private String photometricInterpretation;
    @TableField(value = "mark_image_name")
    private String markImageName;
    @TableField(value = "mark_image_path")
    private String markImagePath;
    @TableField(value = "mark_image_desc")
    private String markImageDesc;
    @TableField(value = "creator_id")
    private Integer creatorId;
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    public Integer getImageIdRel() {
        return imageIdRel;
    }

    public void setImageIdRel(Integer imageIdRel) {
        this.imageIdRel = imageIdRel;
    }

    public Integer getOperateId() {
        return operateId;
    }

    public void setOperateId(Integer operateId) {
        this.operateId = operateId;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public LocalDateTime getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(LocalDateTime operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(Integer imageStatus) {
        this.imageStatus = imageStatus;
    }

    public String getImageEquipment() {
        return imageEquipment;
    }

    public void setImageEquipment(String imageEquipment) {
        this.imageEquipment = imageEquipment;
    }

    public String getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat;
    }

    public String getImageDesc() {
        return imageDesc;
    }

    public void setImageDesc(String imageDesc) {
        this.imageDesc = imageDesc;
    }

    public String getImageCheckPart() {
        return imageCheckPart;
    }

    public void setImageCheckPart(String imageCheckPart) {
        this.imageCheckPart = imageCheckPart;
    }

    public Date getImageCheckTime() {
        return imageCheckTime;
    }

    public void setImageCheckTime(Date imageCheckTime) {
        this.imageCheckTime = imageCheckTime;
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

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getImagePosition() {
        return imagePosition;
    }

    public void setImagePosition(String imagePosition) {
        this.imagePosition = imagePosition;
    }

    public String getImageOrientation() {
        return imageOrientation;
    }

    public void setImageOrientation(String imageOrientation) {
        this.imageOrientation = imageOrientation;
    }

    public String getSliceThickness() {
        return sliceThickness;
    }

    public void setSliceThickness(String sliceThickness) {
        this.sliceThickness = sliceThickness;
    }

    public String getSliceLocation() {
        return sliceLocation;
    }

    public void setSliceLocation(String sliceLocation) {
        this.sliceLocation = sliceLocation;
    }

    public String getImageRows() {
        return imageRows;
    }

    public void setImageRows(String imageRows) {
        this.imageRows = imageRows;
    }

    public String getImageColumns() {
        return imageColumns;
    }

    public void setImageColumns(String imageColumns) {
        this.imageColumns = imageColumns;
    }

    public String getPixelSpacing() {
        return pixelSpacing;
    }

    public void setPixelSpacing(String pixelSpacing) {
        this.pixelSpacing = pixelSpacing;
    }

    public String getBitsAllocated() {
        return bitsAllocated;
    }

    public void setBitsAllocated(String bitsAllocated) {
        this.bitsAllocated = bitsAllocated;
    }

    public String getBitsStored() {
        return bitsStored;
    }

    public void setBitsStored(String bitsStored) {
        this.bitsStored = bitsStored;
    }

    public String getHighBit() {
        return highBit;
    }

    public void setHighBit(String highBit) {
        this.highBit = highBit;
    }

    public String getPixelRepresentation() {
        return pixelRepresentation;
    }

    public void setPixelRepresentation(String pixelRepresentation) {
        this.pixelRepresentation = pixelRepresentation;
    }

    public String getWindowCenter() {
        return windowCenter;
    }

    public void setWindowCenter(String windowCenter) {
        this.windowCenter = windowCenter;
    }

    public String getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(String windowWidth) {
        this.windowWidth = windowWidth;
    }

    public String getRescaleIntercept() {
        return rescaleIntercept;
    }

    public void setRescaleIntercept(String rescaleIntercept) {
        this.rescaleIntercept = rescaleIntercept;
    }

    public String getRescaleSlope() {
        return rescaleSlope;
    }

    public void setRescaleSlope(String rescaleSlope) {
        this.rescaleSlope = rescaleSlope;
    }

    public String getRescaleType() {
        return rescaleType;
    }

    public void setRescaleType(String rescaleType) {
        this.rescaleType = rescaleType;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getSopInstanceUid() {
        return sopInstanceUid;
    }

    public void setSopInstanceUid(String sopInstanceUid) {
        this.sopInstanceUid = sopInstanceUid;
    }

    public LocalDate getContentDate() {
        return contentDate;
    }

    public void setContentDate(LocalDate contentDate) {
        this.contentDate = contentDate;
    }

    public LocalTime getContentTime() {
        return contentTime;
    }

    public void setContentTime(LocalTime contentTime) {
        this.contentTime = contentTime;
    }

    public String getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(String imageNumber) {
        this.imageNumber = imageNumber;
    }

    public String getSamplesPerPixel() {
        return samplesPerPixel;
    }

    public void setSamplesPerPixel(String samplesPerPixel) {
        this.samplesPerPixel = samplesPerPixel;
    }

    public String getPhotometricInterpretation() {
        return photometricInterpretation;
    }

    public void setPhotometricInterpretation(String photometricInterpretation) {
        this.photometricInterpretation = photometricInterpretation;
    }

    public String getMarkImageName() {
        return markImageName;
    }

    public void setMarkImageName(String markImageName) {
        this.markImageName = markImageName;
    }

    public String getMarkImagePath() {
        return markImagePath;
    }

    public void setMarkImagePath(String markImagePath) {
        this.markImagePath = markImagePath;
    }

    public String getMarkImageDesc() {
        return markImageDesc;
    }

    public void setMarkImageDesc(String markImageDesc) {
        this.markImageDesc = markImageDesc;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public ImageEntity() {
    }
    public ImageEntity(SeriesEntity series){
        this.seriesId = series.getSeriesId();
        this.imageCheckPart = series.getSeriesCheckPart();
        this.imageEquipment = series.getSeriesEquipment();
        this.imageCheckTime = series.getSeriesCheckTime();
        this.patientId = series.getPatientId();
        this.patientName = series.getPatientName();
    }
}
