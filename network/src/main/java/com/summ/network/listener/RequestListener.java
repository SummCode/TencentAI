package com.summ.network.listener;

import com.summ.network.response.body.ResponseResult;

/**
 * @@author:Summ
 * @date：2017/12/3
 * @email： summ_summ@163.com
 * <p>
 * project name：Common
 * package name：com.summ.network.callback
 * version：1.0.0
 * <p>
 * describe：
 * <p>
 * <p>
 */
public interface RequestListener<T> {

    /**
     * 请求进度的监听
     *
     * @param current   进度
     * @param total     总大小
     * @param isloading 是否完成
     */
    void onProgress(long current, long total, boolean isloading);

    /**
     * 成功的回调
     *
     * @param t ResponseResult
     */
    void onSuccess(T t);

    /**
     * 失败的回调
     *
     * @param result ResponseResult
     */
    void onFailed(ResponseResult result);


    /**
     * 请求完成的回调
     */
    void onFinished();


}
