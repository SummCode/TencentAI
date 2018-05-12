package com.summ.tencentai.mvp.model.impl;


import com.summ.network.HttpsUtils;
import com.summ.network.callback.HttpDownloadCallback;
import com.summ.network.callback.HttpRequestCallback;
import com.summ.network.callback.HttpUploadCallback;
import com.summ.network.request.RequestConfig;
import com.summ.tencentai.mvp.model.IBaseModel;
import com.summ.tencentai.mvp.model.params.BaseRequestParams;
import com.summ.tool.EmptyUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Describe：
 * <p>
 * <p>
 * Author：summ.Chen.
 * Create：2017/4/25.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */
public abstract class BaseModelImpl implements IBaseModel {

    public final static int get = RequestConfig.GET;
    public final static int post = RequestConfig.POST;


    @Override
    public void httpsRequest(int requestType, String url, BaseRequestParams params, HttpRequestCallback callback) {
        onConfigCommonParams(params);
        switch (requestType) {
            case get:
                get(url, params, callback);
                break;
            case post:
                post(url, params, callback);
                break;
        }
    }

    @Override
    public void uploadFile(String url, List<File> files, HttpUploadCallback callback) {

    }

    @Override
    public void downloadFile(String url, BaseRequestParams params, HttpDownloadCallback callback) {

    }

    private Map<String, String> getHeadParams(BaseRequestParams params) {
        return params == null ? null : params.getHeadParams();
    }

    private Map<String, Object> getBodyParams(BaseRequestParams params) {
        return params == null ? null : params.getBodyParams();
    }

    private void get(String url, BaseRequestParams params, HttpRequestCallback callback) {
        Map<String, String> headParams = getHeadParams(params);
        Map<String, Object> bodyParams = getBodyParams(params);
        if (EmptyUtils.isEmpty(headParams) && EmptyUtils.isEmpty(bodyParams)) {
            HttpsUtils.getInstance().get(url, callback);
        } else {
            HttpsUtils.getInstance().get(url, headParams, bodyParams, callback);
        }
    }


    private void post(String url, BaseRequestParams params, HttpRequestCallback callback) {
        Map<String, String> headParams = getHeadParams(params);
        Map<String, Object> bodyParams = getBodyParams(params);
        if (EmptyUtils.isEmpty(headParams) && EmptyUtils.isEmpty(bodyParams)) {
            HttpsUtils.getInstance().post(url, callback);
        } else {
            HttpsUtils.getInstance().post(url, headParams, bodyParams, callback);
        }
    }

    public abstract void onConfigCommonParams(BaseRequestParams params);


}
