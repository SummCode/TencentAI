package com.summ.tencentai.mvp.presenter;


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
public interface IBasePresenter<T> {

    ResponseResult onConvert(T t);

}
