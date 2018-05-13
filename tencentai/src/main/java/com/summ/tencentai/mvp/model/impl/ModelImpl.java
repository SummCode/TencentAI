package com.summ.tencentai.mvp.model.impl;

import android.support.annotation.NonNull;

import com.summ.network.callback.HttpRequestCallback;
import com.summ.tencentai.entity.result.TencentAiBaseResult;
import com.summ.tencentai.mvp.model.params.BaseRequestParams;
import com.summ.tencentai.mvp.model.params.RequestParams;

import java.util.Map;

public class ModelImpl extends BaseModelImpl {
    @Override
    public void onConfigCommonParams(BaseRequestParams params) {

    }

    @NonNull
    public RequestParams getRequestParams(Map<String, Object> params) {
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParams(params);
        return requestParams;
    }


    @NonNull
    public RequestParams getRequestParams(String key, Object vaule) {
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParam(key, vaule);
        return requestParams;
    }


    public void sendRequest(Map<String, Object> params, HttpRequestCallback<TencentAiBaseResult> callback, String url) {
        RequestParams requestParams = getRequestParams(params);
        httpsRequest(post, url, requestParams, callback);
    }

}
