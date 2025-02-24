package com.ncu.imagesystem.tools;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import com.ncu.imagesystem.enums.ResultCode;

/**
 * controller统一返回对象
 */
@ApiModel(description= "返回响应数据")
public class Result<T> implements Serializable {
    // 自定义状态码
    @ApiModelProperty(value = "状态码")
    private Integer code;

    // 提示内容，如果接口出错，则存放异常信息
    @ApiModelProperty(value = "状态信息")
    private String msg;

    // 返回数据体
    @ApiModelProperty(value = "数据")
    private T data;

    // 接口成功检测。拓展字段，前台可用该接口判断接口是否正常，或者通过code状态码
    @ApiModelProperty(value = "是否成功",required=true)
    private boolean success = true;
    private static final long serialVersionUID = 1L;

    public Result() {}

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    /**
     * 请求成功返回
     * public和返回值间的<T>指定的这是一个泛型方法，这样才可以在方法内使用T类型的变量
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), null);
    }

    public static <T> Result<T> success(String msg) {
        return new Result<>(ResultCode.SUCCESS.getCode(), msg, null);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 请求失败返回
     * @param msg:
     * @author cc
     * @date 2021-07-12 10:11
     */
    public static <T> Result<T> failed(String msg) {
        return new Result<>(ResultCode.FAILED.getCode(), msg, null);
    }

    public static <T> Result<T> failed(String msg, T data) {
        return new Result<>(ResultCode.FAILED.getCode(), msg, data);
    }

    public static <T> Result<T> failed(ResultCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMsg(), null);
    }

    public static <T> Result<T> failed(ResultCode errorCode, T data) {
        return new Result<>(errorCode.getCode(), errorCode.getMsg(), data);
    }

    public static <T> Result<T> failedToken(String msg) {
        return new Result<>(ResultCode.TOKEN_FAILED.getCode(), msg, null);
    }
    public boolean isSuccess() {
        return this.code == ResultCode.SUCCESS.getCode();
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" + "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", success=" + success +
                '}';
    }
}
