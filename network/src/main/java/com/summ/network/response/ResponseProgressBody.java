package com.summ.network.response;


import com.summ.network.listener.RequestListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Describe：
 * <p>
 * 进度监听的响应体
 * <p>
 * @author:summ.Chen.
 * Create：2016/8/30.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */
public class ResponseProgressBody extends ResponseBody {

    /**
     * 实际的待包装响应体
     */
    private final ResponseBody mResponseBody;

    /**
     * 进度回调接口
     */

    private final RequestListener mProgressCallback;

    /**
     * 包装完成的BufferedSource
     */
    private BufferedSource mBufferedSource;

    /**
     * 构造函数，赋值
     *
     * @param responseBody 待包装的响应体
     * @param callback     回调接口
     */
    public ResponseProgressBody(ResponseBody responseBody, RequestListener callback) {
        this.mResponseBody = responseBody;
        this.mProgressCallback = callback;
    }


    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    /**
     * 重写进行包装source
     *
     * @return BufferedSource
     * @throws IOException 异常
     */
    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            //包装
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    /**
     * 读取，回调进度接口
     *
     * @param source Source
     * @return Source
     */
    private Source source(Source source) {

        return new ForwardingSource(source) {
            //当前读取字节数
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                //回调，如果contentLength()不知道长度，会返回-1
                if (mProgressCallback != null) {
                    mProgressCallback.onProgress(totalBytesRead, mResponseBody.contentLength(), bytesRead == -1);
                }
                return bytesRead;
            }
        };
    }


}
