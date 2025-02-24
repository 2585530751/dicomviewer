package com.ncu.imagesystem.dto;

import cn.hutool.json.JSONObject;
import com.ncu.imagesystem.entity.SysUserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel
public class TokenContent {
    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "账号名")
    private String account;


    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "角色列表")
    private List<String> roles;

    @ApiModelProperty(value = "生成时间")
    private Date start;

    @ApiModelProperty(value = "到期时间")
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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    //accessToken的json字符串
    public String getJsonStringObjAccess(){
        JSONObject object = new JSONObject();
        object.set("userId", this.userId);
        object.set("roles", this.roles);
        object.set("account", this.account);
        object.set("userName", this.userName);
//        object.set("start", this.start);
//        object.set("expires", this.expires);
        return object.toString();
    }

    //accessToken的json字符串
    public String getJsonStringObjRefresh(){
        JSONObject object = new JSONObject();
        object.set("userId", this.userId);
        object.set("roles", this.roles);
//        object.set("start", this.start);
//        object.set("expires", this.expires);
        return object.toString();
    }

    public TokenContent(SysUserEntity user, String effectiveSecond) {
        this.userId = user.getUserId();
        this.account = user.getAccount();
        this.userName = user.getName();
        this.roles = new ArrayList<>();
        this.start = new Date();
        this.expires = this.start.getTime() + Integer.parseInt(effectiveSecond) * 1000L;
    }

    public TokenContent(String jsonString, String effectiveSecond){
        JSONObject object = new JSONObject(jsonString);
        this.userId = object.get("userId", Integer.class);
        this.account = object.get("account", String.class);
        this.roles = object.get("roles", ArrayList.class);
        this.start = new Date();
        this.expires = this.start.getTime() + Integer.parseInt(effectiveSecond) * 1000L;
    }

    public TokenContent() {
    }
}
