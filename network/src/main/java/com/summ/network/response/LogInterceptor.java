package com.summ.network.response;


import android.util.Log;

import com.summ.network.listener.RequestListener;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

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
public class LogInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.i("Network", String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.i("Network", String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }


}
