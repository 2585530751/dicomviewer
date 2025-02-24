package com.ncu.imagesystem.enums;

/**
 * Result返回的状态码表
 */
public enum ResultCode {
    SUCCESS(200, "请求成功"),
    FAILED(400, "请求失败"),
    TOKEN_FAILED(300, "token失效"),
    NONE(999, "无");

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

