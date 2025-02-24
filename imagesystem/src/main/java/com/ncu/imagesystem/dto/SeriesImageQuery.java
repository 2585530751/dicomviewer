package com.ncu.imagesystem.dto;

import java.util.List;

public class SeriesImageQuery {
    private Long pageSize;
    private Long current;
    private List<Integer> departmentIdList;

    private String seriesName;

    private String seriesFormat;

    private String seriesCount;

    private String patientName;

    private String seriesDate;

    private String seriesTime;

    private  String seriesDesc;

    private  String seriesDateBegin;

    private  String seriesDateEnd;

    private  Integer patientId;

    private Integer studyId;

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public List<Integer> getDepartmentIdList() {
        return departmentIdList;
    }

    public void setDepartmentIdList(List<Integer> departmentIdList) {
        this.departmentIdList = departmentIdList;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesFormat() {
        return seriesFormat;
    }

    public void setSeriesFormat(String seriesFormat) {
        this.seriesFormat = seriesFormat;
    }

    public String getSeriesCount() {
        return seriesCount;
    }

    public void setSeriesCount(String seriesCount) {
        this.seriesCount = seriesCount;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getSeriesDate() {
        return seriesDate;
    }

    public void setSeriesDate(String seriesDate) {
        this.seriesDate = seriesDate;
    }

    public String getSeriesTime() {
        return seriesTime;
    }

    public void setSeriesTime(String seriesTime) {
        this.seriesTime = seriesTime;
    }

    public String getSeriesDesc() {
        return seriesDesc;
    }

    public void setSeriesDesc(String seriesDesc) {
        this.seriesDesc = seriesDesc;
    }

    public String getSeriesDateBegin() {
        return seriesDateBegin;
    }

    public void setSeriesDateBegin(String seriesDateBegin) {
        this.seriesDateBegin = seriesDateBegin;
    }

    public String getSeriesDateEnd() {
        return seriesDateEnd;
    }

    public void setSeriesDateEnd(String seriesDateEnd) {
        this.seriesDateEnd = seriesDateEnd;
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
}
