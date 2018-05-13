package com.summ.network;


import com.summ.tool.EmptyUtils;
import com.summ.tool.LogUtils;
import com.summ.network.request.RequestConfig;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Describe：
 * <p>
 * Http请求，参数是form-data格式
 * <p>
 *
 * @author:summ.Chen. Create：2016/8/30.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */
public class HttpsUtils {

    private static HttpsUtils instance;

    public final static int GET = RequestConfig.GET;
    public final static int POST = RequestConfig.POST;
    public final static int PUT = RequestConfig.PUT;
    public final static int DELETE = RequestConfig.DELETE;

    private OkHttpClient mClient = RequestConfig.HTTPS_CLIENT;

    public static HttpsUtils getInstance() {
        if (instance == null) {
            synchronized (HttpsUtils.class) {
                if (instance == null)
                    instance = new HttpsUtils();
            }
        }
        return instance;
    }

    /**
     * get方式请求,无参数
     *
     * @param url      请求的地址
     * @param callback 请求的回调
     */
    public Call get(String url, Callback callback) {
        return get(url, null, callback);
    }


    /**
     * get方式请求，带参数
     *
     * @param url       请求的地址
     * @param bodyParam request对象
     * @param callback  请求的回调
     */
    public Call get(String url, Map<String, Object> bodyParam, Callback callback) {
        return get(url, null, bodyParam, callback);
    }

    /**
     * get方式请求，带参数
     *
     * @param url       请求的地址
     * @param headParam 请求头参数
     * @param bodyParam 请求体参数
     * @param callback  请求的回调
     */
    public Call get(String url, Map<String, String> headParam, Map<String, Object> bodyParam, Callback callback) {
        return request(url, headParam, bodyParam, GET, callback);
    }


    /**
     * Post请求，不带参数
     *
     * @param url      请求的地址
     * @param callback 请求的回调
     */
    public Call post(String url, Callback callback) {
        return post(url, null, callback);
    }

    /**
     * Post请求，不带参数
     *
     * @param url       请求的地址
     * @param bodyParam 请求参数
     * @param callback  请求的回调
     */
    public Call post(String url, Map<String, Object> bodyParam, Callback callback) {
        return post(url, null, bodyParam, callback);
    }

    /**
     * put请求，带参数
     *
     * @param url       请求的地址
     * @param headParam 请求头参数
     * @param bodyParam 请求参数
     * @param callback  请求的回调
     */
    public Call post(String url, Map<String, String> headParam, Map<String, Object> bodyParam, Callback callback) {
        return request(url, headParam, bodyParam, POST, callback);
    }


    /**
     * put请求，不带参数
     *
     * @param url      请求的地址
     * @param callback 请求的回调
     */
    public Call put(String url, Callback callback) {
        return put(url, null, callback);
    }

    /**
     * put请求，带参数
     *
     * @param url       请求的地址
     * @param bodyParam 请求参数
     * @param callback  请求的回调
     */
    public Call put(String url, Map<String, Object> bodyParam, Callback callback) {
        return put(url, null, bodyParam, callback);
    }

    /**
     * put请求，带参数
     *
     * @param url       请求的地址
     * @param headParam 请求头参数
     * @param bodyParam 请求参数
     * @param callback  请求的回调
     */
    public Call put(String url, Map<String, String> headParam, Map<String, Object> bodyParam, Callback callback) {
        return request(url, headParam, bodyParam, PUT, callback);
    }


    /**
     * delete请求，不带参数
     *
     * @param url      请求的地址
     * @param callback 请求的回调
     */
    public Call delete(String url, Callback callback) {
        return delete(url, null, callback);
    }

    /**
     * delete请求，带参数
     *
     * @param url       请求的地址
     * @param bodyParam 请求参数
     * @param callback  请求的回调
     */
    public Call delete(String url, Map<String, Object> bodyParam, Callback callback) {
        return delete(url, null, bodyParam, callback);
    }

    /**
     * delete请求，带参数
     *
     * @param url       请求的地址
     * @param headParam 请求头参数
     * @param bodyParam 请求参数
     * @param callback  请求的回调
     */
    public Call delete(String url, Map<String, String> headParam, Map<String, Object> bodyParam, Callback callback) {
        return request(url, headParam, bodyParam, DELETE, callback);
    }


    /**
     * 请求，带参数
     *
     * @param url        请求的地址
     * @param headParam  请求头参数
     * @param bodyParam  请求参数
     * @param methodType 请求方法
     * @param callback   请求的回调
     */
    public Call request(String url, Map<String, String> headParam, Map<String, Object> bodyParam, int methodType, Callback callback) {

        if (EmptyUtils.isEmpty(url)) {
            new NullPointerException("Request url is not null.");
        }

        RequestConfig requestConfig;

        if (EmptyUtils.isEmpty(headParam)) {
            requestConfig = new RequestConfig();
        } else {
            requestConfig = new RequestConfig(headParam);
        }

        FormBody.Builder formBodyBuilder = requestConfig.setBody(url, bodyParam);

        if (formBodyBuilder == null) {
            formBodyBuilder = new FormBody.Builder();
        }

        RequestBody requestBody = null;
        switch (methodType) {
            case GET:
                url = requestConfig.getParamUrl(url, bodyParam);
                break;
            case POST:
            case PUT:
            case DELETE:
                requestBody = formBodyBuilder.build();
                break;
            default:
        }

        Request.Builder builder = requestConfig.getRequestBuilder().url(url);
        Request request = requestConfig.getRequest(methodType, builder, requestBody);

        Call call = mClient.newCall(request);

        call.enqueue(callback);

        return call;
    }


    /**
     * Post请求，不带参数
     *
     * @param url      请求的地址
     * @param callback 请求的回调
     */
    public Call postJson(String url, Callback callback) {
        return postJson(url, "", callback);
    }

    /**
     * Post请求，不带参数
     *
     * @param url      请求的地址
     * @param jsonBody 请求参数
     * @param callback 请求的回调
     */
    public Call postJson(String url, String jsonBody, Callback callback) {

        if (EmptyUtils.isEmpty(url)) {
            new NullPointerException("Request url is not null.");
        }

        if (EmptyUtils.isEmpty(jsonBody)) {
            jsonBody = "{}";
        }
        RequestConfig requestConfig = new RequestConfig();
        Request.Builder builder = requestConfig.getRequestBuilder().url(url);

        RequestBody requestBody = RequestBody.create(RequestConfig.CONTENT_TYPE_JSON, jsonBody);

        Request request = requestConfig.getRequest(POST, builder, requestBody);

        Call call = mClient.newCall(request);

        call.enqueue(callback);

        LogUtils.i("Request Url: " + url + "  Json Body: " + jsonBody);

        return call;
    }


}
