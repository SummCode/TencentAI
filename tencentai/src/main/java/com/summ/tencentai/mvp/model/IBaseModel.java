package com.summ.tencentai.mvp.model;

import com.summ.network.callback.HttpDownloadCallback;
import com.summ.network.callback.HttpRequestCallback;
import com.summ.network.callback.HttpUploadCallback;
import com.summ.tencentai.mvp.model.params.BaseRequestParams;

import java.io.File;
import java.util.List;

/**
 * Describe：
 * <p>
 * <p>
 * Author：summ.Chen.
 * Create：2017/4/25.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */
public interface IBaseModel {

    void httpsRequest(int requestType, String url, BaseRequestParams params, HttpRequestCallback callback);

    void uploadFile(String url, List<File> files, HttpUploadCallback callback);

    void downloadFile(String url, BaseRequestParams params, HttpDownloadCallback callback);

}
