package com.wang.dto;

import org.springframework.http.HttpStatus;
/**
 * 封装json对象，所有返回结果都使用它
 */
public class Result<T> {

//    千万不能是int！
    private String code;// 是否成功标志
    private T data;// 成功时返回的数据

    private String error;// 错误信息


    public Result() {
    }

    /**
     * 成功时的构造器
     * @param status：HttpStatus.OK  200
     * @param data
     */
    public Result(HttpStatus status, T data) {
        this.code = String.valueOf(status.value());
        this.data = data;
    }
    HttpStatus code = HttpStatus.INTERNAL_SERVER_ERROR;
    HttpStatus code = HttpStatus.BAD_REQUEST;
    HttpStatus code = HttpStatus.BAD_GATEWAY;
    HttpStatus code = HttpStatus.NOT_FOUND;
    /**
     * 失败时的构造器
     * @param status：常用的有：
     *              HttpStatus.BAD_REQUEST 400
     *              HttpStatus.NOT_FOUND 404
     *              HttpStatus.INTERNAL_SERVER_ERROR 500
     *              HttpStatus.BAD_GATEWAY 502
     * @param error
     */
    public Result(HttpStatus status, String error) {
        this.code = String.valueOf(status.value());
        this.error = error;

    }
    // 错误时的构造器


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "JsonResult [code=" + code + ", data=" + data + ", error=" + error + "]";
    }

}