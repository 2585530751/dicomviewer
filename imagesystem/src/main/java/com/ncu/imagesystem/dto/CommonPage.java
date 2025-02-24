package com.ncu.imagesystem.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description= "通用分页对象")
public class CommonPage<T> {
    @ApiModelProperty(value = "数据")
    private List<T> records;
    @ApiModelProperty(value = "总记录数量")
    private Long total;
    @ApiModelProperty(value = "页大小")
    private Long size;
    @ApiModelProperty(value = "当前页码")
    private Long current;
    @ApiModelProperty(value = "总页数")
    private Long totalPage;

    public CommonPage(List<T> records, Long total, Long size, Long current, Long totalPage) {
        this.records = records;
        this.total = total;
        this.size = size;
        this.current = current;
        this.totalPage = totalPage;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }
}
