package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@ApiModel
@TableName("log_operate")
public class LogOperate {
    @TableId(value = "operate_log_id", type = IdType.AUTO)
    @ApiModelProperty(value = "日志记录id")
    private Integer operateLogId;
    @TableField(value = "operate_type")
    @ApiModelProperty(value = "操作类型")
    private Integer operateType;
    @TableField(value = "operate_content")
    @ApiModelProperty(value = "操作内容")
    private String operateContent;
    @TableField(value = "operator_id")
    @ApiModelProperty(value = "操作人id")
    private Integer operatorId;
    @TableField(value = "operator_name")
    @ApiModelProperty(value = "操作人名")
    private String operatorName;
    @TableField(value = "operate_time")
    @ApiModelProperty(value = "操作时间")
    private Date operateTime;
    @TableField(value = "log_type")
    @ApiModelProperty(value = "日志类型")
    private Integer logType;

    public Integer getOperateLogId() {
        return operateLogId;
    }

    public void setOperateLogId(Integer operateLogId) {
        this.operateLogId = operateLogId;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getOperateContent() {
        return operateContent;
    }

    public void setOperateContent(String operateContent) {
        this.operateContent = operateContent;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }
}
