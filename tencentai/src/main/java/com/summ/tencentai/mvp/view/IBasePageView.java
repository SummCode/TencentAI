package com.summ.tencentai.mvp.view;


import com.summ.network.response.body.ResponseResult;

/**
 * Describe：
 * <p>
 * <p>
 * Author：summ.Chen.
 * Create：2017/4/25.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */
public interface IBasePageView {

    void onRefreshSuccess(String dataJsonStr);

    void onRefreshFailed(ResponseResult result);

    void onLoadMoreSuccess(String dataJsonStr);

    void onLoadMoreFailed(ResponseResult result);

}
