package com.summ.tencentai.mvp.model.params;

import java.util.HashMap;
import java.util.Map;

/**
 * Describe：
 * <p>
 * <p>
 * Author：summ.Chen.
 * Create：2017/5/26.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */
public abstract class BaseRequestParams {

    private Map<String, String> mHeadParams;
    private Map<String, Object> mBodyParams;

    public BaseRequestParams() {
        this.mHeadParams = new HashMap<>();
        this.mBodyParams = new HashMap<>();
    }

    public Map<String, String> getHeadParams() {
        return mHeadParams;
    }

    public Map<String, Object> getBodyParams() {
        return mBodyParams;
    }

    public void addHeadParam(String key, String value) {
        this.mHeadParams.put(key, value);
    }

    public void addHeadParams(Map<String, String> headParams) {
        this.mHeadParams.putAll(headParams);
    }


    public void addBodyParam(String key, Object value) {
        this.mBodyParams.put(key, value);
    }

    public void addBodyParams(Map<String, Object> bodyParams) {
        this.mBodyParams.putAll(bodyParams);
    }

}
