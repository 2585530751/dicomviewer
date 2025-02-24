package com.ncu.imagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@ApiModel
@TableName("sys_user")
public class SysUserEntity {
    @TableId(value = "user_id", type = IdType.AUTO)
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    @TableField(value = "org_id")
    @ApiModelProperty(value = "组织id")
    private Integer orgId;
    @TableField(value = "org_name")
    @ApiModelProperty(value = "组织名")
    private String orgName;
    @TableField(value = "account")
    @ApiModelProperty(value = "账号")
    private String account;
    @TableField(value = "password")
    @ApiModelProperty(value = "密码")
    private String password;
    @TableField(value = "name")
    @ApiModelProperty(value = "姓名")
    private String name;
    @TableField(value = "phone_number")
    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;
    @TableField(value = "email")
    @ApiModelProperty(value = "电子邮件")
    private String email;
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @TableField(value = "login_count")
    @ApiModelProperty(value = "登录次数")
    private Integer loginCount;
    @TableField(value = "user_name")
    @ApiModelProperty(value = "用户名")
    private String userName;
    @TableField(value = "id_card")
    @ApiModelProperty(value = "身份证号")
    private String idCard;
    @TableField(value = "birth_of_date")
    @ApiModelProperty(value = "出生日期")
    private Date birthOfDate;
    @TableField(value = "gender")
    @ApiModelProperty(value = "性别")
    private String gender;
    @TableField(value = "address")
    @ApiModelProperty(value = "地址")
    private String address;
    @TableField(value = "user_height")
    @ApiModelProperty(value = "身高")
    private Float userHeight;
    @TableField(value = "user_weight")
    @ApiModelProperty(value = "体重")
    private Float userWeight;
    @TableField(value = "place")
    @ApiModelProperty(value = "位置")
    private String place;
    @TableField(value = "head_icon")
    @ApiModelProperty(value = "头像")
    private String headIcon;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Date getBirthOfDate() {
        return birthOfDate;
    }

    public void setBirthOfDate(Date birthOfDate) {
        this.birthOfDate = birthOfDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(Float userHeight) {
        this.userHeight = userHeight;
    }

    public Float getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(Float userWeight) {
        this.userWeight = userWeight;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }
}
