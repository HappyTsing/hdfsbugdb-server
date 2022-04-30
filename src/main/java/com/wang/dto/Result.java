package com.wang.dto;

import org.springframework.http.HttpStatus;

/**
 * 封装json对象，Controller最终返回的都是Result对象
 *
 * @author happytsing
 */
public class Result<T> {
    /**
     * code:http status code
     * data:成功时返回的数据
     * error:错误时返回的信息
     */
    private String code;
    private T data;
    private String error;


    public Result() {
    }

    /**
     * 成功时的构造器
     *
     * @param status：HttpStatus.OK 200
     * @param data
     */
    public Result(HttpStatus status, T data) {
        this.code = String.valueOf(status.value());
        this.data = data;
    }

    /**
     * 失败时的构造器
     *
     * @param status：常用的有： HttpStatus.BAD_REQUEST 400
     *                     HttpStatus.NOT_FOUND 404
     *                     HttpStatus.INTERNAL_SERVER_ERROR 500
     *                     HttpStatus.BAD_GATEWAY 502
     * @param error
     */
    public Result(HttpStatus status, String error) {
        this.code = String.valueOf(status.value());
        this.error = error;

    }


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