package com.ncu.imagesystem.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel
public class TokenResult {

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "账号名")
    private String account;

    @ApiModelProperty(value = "角色列表")
    private List<String> roles;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "访问token")
    private String accessToken;

    @ApiModelProperty(value = "刷新token")
    private String refreshToken;

    @ApiModelProperty(value = "访问token过期时间")
    private Long expires;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public TokenResult(TokenContent content, String accessToken, String refreshToken){
        this.userId = content.getUserId();
        this.account = content.getAccount();
        this.userName = content.getUserName();
        this.roles = content.getRoles();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expires = content.getExpires();
    }

    public TokenResult() {
    }
}
