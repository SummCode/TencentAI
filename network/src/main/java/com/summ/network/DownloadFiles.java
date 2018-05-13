package com.summ.network;


import com.summ.tool.EmptyUtils;
import com.summ.network.callback.HttpDownloadCallback;
import com.summ.network.request.RequestConfig;
import com.summ.network.response.ProgressInterceptor;

import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Describe：
 * <p>
 * <p>
 *
 * @author:summ.Chen. Create：2016/8/30.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */
public class DownloadFiles {

    private static DownloadFiles instance;

    private OkHttpClient mClient = RequestConfig.HTTPS_CLIENT;

    private int mMethod = RequestConfig.GET;

    public static DownloadFiles getInstance() {
        if (instance == null) {
            synchronized (DownloadFiles.class) {
                if (instance == null) {
                    instance = new DownloadFiles();
                }
            }
        }
        return instance;
    }

    public void setMethod(int methodType) {
        this.mMethod = methodType;
    }

    /**
     * 无参数，文件下载
     *
     * @param url      下载地址
     * @param callback 下载的回调
     * @return Call对象，用于取消请求
     */
    public Call downloadFile(String url, HttpDownloadCallback callback) {
        return downloadFile(url, null, callback);
    }

    /**
     * 有参数，文件下载
     *
     * @param url      下载地址
     * @param bodyMap  请求参数
     * @param callback 下载的回调
     * @return Call对象，用于取消请求
     */
    public Call downloadFile(String url, Map<String, Object> bodyMap, HttpDownloadCallback callback) {

        if (EmptyUtils.isEmpty(url)) {
            new NullPointerException("Request url is not null.");
        }


        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        RequestConfig requestConfig = new RequestConfig();
        switch (mMethod) {
            case RequestConfig.GET:
                url = requestConfig.getParamUrl(url, bodyMap);
                break;
            case RequestConfig.POST:
                FormBody.Builder temp = requestConfig.setBody(url, bodyMap);
                if (temp != null) {
                    formBodyBuilder = temp;
                }
                break;
            default:
        }

        RequestBody requestBody = formBodyBuilder.build();

        Request.Builder builder = new Request.Builder().url(url);

        Request request = requestConfig.getRequest(mMethod, builder, requestBody);

        ProgressInterceptor interceptor = new ProgressInterceptor(callback);
        Call call = mClient.newBuilder().addInterceptor(interceptor).build().newCall(request);

        call.enqueue(callback);

        return call;

    }


}
