package com.summ.network.response.body;

import java.io.Serializable;

/**
 * Describe：
 * <p>
 * 自定义请求结果
 * <p>
 * @author:summ.Chen.
 * Create：2016/9/1.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */
public class ResponseResult implements Serializable {

    private String url;

    private int code = 200;

    private String message = "请求成功";


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseResult() {
    }

    public ResponseResult(String url) {
        this.url = url;
    }

    public ResponseResult(String url, int code, String message) {
        this.url = url;
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "url='" + url + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
