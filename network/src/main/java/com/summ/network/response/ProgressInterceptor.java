package com.summ.network.response;


import android.util.Log;

import com.summ.network.listener.RequestListener;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.content.ContentValues.TAG;

/**
 * Describe：
 * <p>
 * 响应进度的拦截器
 * <p>
 *
 * @author:summ.Chen. Create：2016/8/31.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */
public class ProgressInterceptor extends LogInterceptor {

    private RequestListener mCallback;

    public ProgressInterceptor(RequestListener callback) {
        this.mCallback = callback;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        super.intercept(chain);

        //拦截
        Response originalResponse = chain.proceed(chain.request());
        //包装响应体并返回
        return originalResponse.newBuilder().body(new ResponseProgressBody(originalResponse.body(), mCallback)).build();
    }


}
