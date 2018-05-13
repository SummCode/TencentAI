package com.summ.network.callback;


import com.summ.tool.LogUtils;

/**
 * Describe：
 * <p>
 * 进度回调
 * <p>
 *
 * @@author:summ.Chen. Create：2016/9/2.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */
public class HttpUploadCallback<T> extends HttpRequestCallback<T>  {

    @Override
    public void onProgress(long currentProgress, long totalProgress, boolean isLoading) {
        float progress = formatProgress(currentProgress, totalProgress);
        LogUtils.i("Loading progress " + progress + "%");
    }

}
