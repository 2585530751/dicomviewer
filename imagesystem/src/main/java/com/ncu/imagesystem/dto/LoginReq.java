package com.ncu.imagesystem.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description= "登录对象")
public class LoginReq {
    @ApiModelProperty(value = "账号")
    private String account;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;
    @ApiModelProperty(value = "身份")
    private String permissionName;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}
