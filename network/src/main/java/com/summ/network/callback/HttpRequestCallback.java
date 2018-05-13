package com.summ.network.callback;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.summ.network.listener.RequestListener;
import com.summ.network.response.body.ResponseResult;
import com.summ.tool.JsonUtils;
import com.summ.tool.LogUtils;
import com.summ.network.response.body.RequestResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

import javax.net.ssl.SSLException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Describe：
 * <p>
 * 请求结果回调基类
 * <p>
 *
 * @author:summ.Chen.
 * Create：2016/9/2.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */
public class HttpRequestCallback<T> implements RequestListener<T>, Callback {

    static final int RESPONSE_RESULT_TYPE_LOADING = 0;
    static final int RESPONSE_RESULT_TYPE_FAILED = -1;

    private static final int RESPONSE_RESULT_TYPE_SUCCESS = 200;

    private static final int RESPONSE_RESULT_ERROR_TYPE_FILE = -100;
    private static final int RESPONSE_RESULT_ERROR_TYPE_NETWORK = -200;
    private static final int RESPONSE_RESULT_ERROR_TYPE_JSON = -300;

    NetworkHandler mNetworkHandler = new NetworkHandler(this);
    RequestResult mResult;

    Class<T> mClz;


    public HttpRequestCallback() {

        Class c = getClass();

        Type type = c.getGenericSuperclass();

        if (type instanceof ParameterizedType) {

            Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();

            this.mClz = (Class<T>) parameterizedType[0];
        }

    }

    /**
     * 获取请求的地址
     *
     * @param call 请求的执行
     * @return url 请求的地址
     */
    public String getRequestUrl(Call call) {
        return call.request().url().toString();
    }

    /**
     * 请求响应的处理
     *
     * @param msg 消息
     */
    public void onResponseHandler(Message msg) {
        int type = msg.what;
        switch (type) {
            case RESPONSE_RESULT_TYPE_SUCCESS:
                onSuccessHandler();
                break;
            case RESPONSE_RESULT_TYPE_LOADING:
                break;
            default:
                onFailed(mResult);
                break;
        }

        onFinished();
        LogUtils.i("Response Result[ Url: " + mResult.getUrl() + "       Code: " + mResult.getCode() + "       Meagess:" + mResult.getMessage() + "       Response Body: " + mResult.getResponseBody() + " ]");

    }


    @Override
    public void onFailure(Call call, IOException e) {
        String url = getRequestUrl(call);
        int code = RESPONSE_RESULT_TYPE_FAILED;
        String message = e.getMessage();
        if (e instanceof FileNotFoundException) {
            message = "文件不存在";
            code = RESPONSE_RESULT_ERROR_TYPE_FILE;
        }

        if (e instanceof SocketException) {
            message = "网络连接重置，稍后再试";
            code = RESPONSE_RESULT_ERROR_TYPE_NETWORK;
        }

        if (e instanceof UnknownHostException) {
            message = "网络连接失败，检查网络";
            code = RESPONSE_RESULT_ERROR_TYPE_NETWORK;
        }

        if (e instanceof SocketTimeoutException) {
            message = "网络连接超时，稍后再试";
            code = RESPONSE_RESULT_ERROR_TYPE_NETWORK;
        }

        if (e instanceof ConnectException) {
            message = "网络连接失败，检查网络";
            code = RESPONSE_RESULT_ERROR_TYPE_NETWORK;
        } 

		if (e instanceof SSLException) {
            message = "网络连接失败，检查证书配置";
            code = RESPONSE_RESULT_ERROR_TYPE_NETWORK;
        }

        // TODO: ……

        mResult = new RequestResult(url, code, message, message);
//        mResult = new RequestResult(url, code, message, Log.getStackTraceString(e));

        LogUtils.e("Exception info: " + Log.getStackTraceString(e));

        mNetworkHandler.sendEmptyMessage(code);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        String url = getRequestUrl(call);
        mResult = new RequestResult(url);

        int code = response.code();

        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            mResult.setResponseBody(responseBody);
        } else {
            String meagess = onSystemErrorCode(code);
            mResult.setCode(code);
            mResult.setMessage(meagess);
        }

        mNetworkHandler.sendEmptyMessage(code);


    }

    @Override
    public void onProgress(long currentProgress, long totalProgress, boolean isLoading) {

    }

    @Override
    public void onSuccess(T t) {

    }

    @Override
    public void onFailed(ResponseResult result) {

    }

    @Override
    public void onFinished() {

    }

    /**
     * 进度格式化
     * @param currentProgress  当前进度
     * @param totalProgress  总大小
     * @return  float
     */
    public float formatProgress(float currentProgress, long totalProgress) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        String progressStr = decimalFormat.format(currentProgress / totalProgress * 100);
        return Float.parseFloat(progressStr);
    }

    /**
     * http\https协议错误码的处理
     *
     * @param code 错误码
     * @return 错误信息
     */
    private String onSystemErrorCode(int code) {
        String meagess = "";
        switch (code) {
            //请求出错
            case 400:
                meagess = "请求出错，稍后再试";
                break;
            //资源不可用
            case 403:
                meagess = "该内容暂时不能访问";
                break;
            //无法找到指定位置的资源
            case 404:
                meagess = "请求出错，稍后再试";
                break;
            //系统出错
            case 500:
                meagess = "系统出错，稍后再试";
                break;
            //请求超时
            case 502:
                meagess = "网络连接超时";
                break;
            // TODO: 2016/12/3
            default:
        }
        return meagess;
    }

    /**
     * 请求成功的处理
     */
    private void onSuccessHandler() {
        T successResult = getSuccessResult();
        if (successResult == null) {
            mResult.setMessage("Json 解析异常");
            mResult.setCode(RESPONSE_RESULT_ERROR_TYPE_JSON);
            onFailed(mResult);
        } else {
            onSuccess(successResult);
        }
    }

    /**
     * 返回结果解析对象
     *
     * @return Object
     */
    private T getSuccessResult() {
        String jsonStr = mResult.getResponseBody();
        T successResult;
        if (this instanceof HttpDownloadCallback) {
            successResult = (T) new File(jsonStr);
        } else {
            successResult = JsonUtils.fromJson(jsonStr, mClz);
        }
        return successResult;
    }


    static class NetworkHandler extends Handler {
        /**
         * WeakReference to the outer class's instance.
         */
        private WeakReference<HttpRequestCallback> mCallback;

        /**
         * 网络异步处理
         *
         * @param callback 请求的回调
         */
        private NetworkHandler(HttpRequestCallback callback) {
            mCallback = new WeakReference(callback);
        }

        @Override
        public void handleMessage(Message msg) {
            HttpRequestCallback callback = mCallback.get();
            if (callback != null) {
                callback.onResponseHandler(msg);
            }
        }
    }

}
