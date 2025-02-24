package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
@ApiModel
@TableName("tool")
public class ToolEntity {
    @TableId(value = "tool_id", type = IdType.AUTO)
    @ApiModelProperty(value = "项目id")
    private Integer toolId;
    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户名")
    private Integer userId;
    @TableField(value = "tool_date")
    @ApiModelProperty(value = "项目创建日期")
    private String toolDate;
    @TableField(value = "tool_name")
    @ApiModelProperty(value = "项目名称")
    private String toolName;
    @TableField(value = "tool_background")
    @ApiModelProperty(value = "项目背景")
    private String toolBackground;
    @TableField(value = "tool_abstract")
    @ApiModelProperty(value = "项目背景")
    private String toolAbstract;
    @TableField(value = "tool_route")
    @ApiModelProperty(value = "项目存放路径")
    private String toolRoute;
    @TableField(value = "tool_operate")
    @ApiModelProperty(value = "项目操作后的存放路径")
    private String toolOperate;

    public String getToolOperate() {
        return toolOperate;
    }

    public void setToolOperate(String toolOperate) {
        this.toolOperate = toolOperate;
    }

    public Integer getToolId() {
        return toolId;
    }

    public void setToolId(Integer toolId) {
        this.toolId = toolId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToolDate() {
        return toolDate;
    }

    public void setToolDate(String toolDate) {
        this.toolDate = toolDate;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getToolBackground() {
        return toolBackground;
    }

    public void setToolBackground(String toolBackground) {
        this.toolBackground = toolBackground;
    }

    public String getToolAbstract() {
        return toolAbstract;
    }

    public void setToolAbstract(String toolAbstract) {
        this.toolAbstract = toolAbstract;
    }

    public String getToolRoute() {
        return toolRoute;
    }

    public void setToolRoute(String toolRoute) {
        this.toolRoute = toolRoute;
    }
}
