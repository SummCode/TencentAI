package com.summ.network.response.body;

/**
 * Describe：
 * <p>
 * <p>
 * @author:summ.Chen.
 * Create：2017/2/5.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */

public class RequestResult extends ResponseResult{

    private String responseBody;

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public RequestResult() {
    }

    public RequestResult(String url) {
        super(url);
    }

    public RequestResult(String url, String responseBody) {
        super(url);
        this.responseBody = responseBody;
    }

    public RequestResult(String url, int code, String message, String responseBody) {
        super(url, code, message);
        this.responseBody = responseBody;
    }


    @Override
    public String toString() {
        return "RequestResult{" +
                "responseBody='" + responseBody + '\'' +
                '}';
    }
}
