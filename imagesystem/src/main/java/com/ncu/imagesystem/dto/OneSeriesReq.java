package com.ncu.imagesystem.dto;

import com.ncu.imagesystem.entity.SeriesEntity;

import java.util.List;

public class OneSeriesReq {
    private SeriesEntity series;

    private List<Integer> departmentIdList;

    public SeriesEntity getSeries() {
        return series;
    }

    public void setSeries(SeriesEntity series) {
        this.series = series;
    }

    public List<Integer> getDepartmentIdList() {
        return departmentIdList;
    }

    public void setDepartmentIdList(List<Integer> departmentIdList) {
        this.departmentIdList = departmentIdList;
    }
}
