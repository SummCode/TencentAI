package com.summ.tencentai.mvp.model.impl;

import com.summ.network.callback.HttpRequestCallback;
import com.summ.tencentai.api.TencentAiApi;
import com.summ.tencentai.entity.result.TencentAiBaseResult;

import java.util.Map;

public class TencentAiModleImpl extends ModelImpl {


    public void parseVoice(Map<String, Object> params, HttpRequestCallback<TencentAiBaseResult> callback) {
        String url = TencentAiApi.TENCENT_AI_AAI_ASR;
        sendRequest(params, callback, url);
    }



    public void parseIdentityCard(Map<String, Object> params, HttpRequestCallback<TencentAiBaseResult> callback) {
        String url = TencentAiApi.TENCENT_AI_OCR_IDCARDOCR;
        sendRequest(params, callback, url);
    }


    public void parseVehicleLicense(Map<String, Object> params, HttpRequestCallback<TencentAiBaseResult> callback) {
        String url = TencentAiApi.TENCENT_AI_OCR_DRIVER_LICENSE;
        sendRequest(params, callback, url);
    }


    public void parseBusinessLicense(Map<String, Object> params, HttpRequestCallback<TencentAiBaseResult> callback) {
        String url = TencentAiApi.TENCENT_AI_OCR_BIZ_LICENSE;
        sendRequest(params, callback, url);
    }

    public void parseCreditCard(Map<String, Object> params, HttpRequestCallback<TencentAiBaseResult> callback) {
        String url = TencentAiApi.TENCENT_AI_OCR_CREDIT_CARD;
        sendRequest(params, callback, url);
    }


}
