package com.summ.network.request;


import com.summ.network.listener.RequestListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Describe：
 * <p>
 * 进度监听的请求体
 * <p>
 *
 * @@author:summ.Chen. Create：2016/8/30.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */
public class RequestProgressBody extends RequestBody {

    /**
     * 实际的待包装请求体
     */
    private final RequestBody mRequestBody;

    /**
     * 进度回调接口
     */
    private final RequestListener mProgressCallback;

    /**
     * 包装完成的BufferedSink
     */
    private BufferedSink mBufferedSink;

    /**
     * 构造函数，赋值
     *
     * @param requestBody 待包装的请求体
     * @param callback    回调接口
     */
    public RequestProgressBody(RequestBody requestBody, RequestListener callback) {
        this.mRequestBody = requestBody;
        this.mProgressCallback = callback;
    }

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    /**
     * 重写进行写入
     *
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {

        if (mBufferedSink == null) {
            //包装
            mBufferedSink = Okio.buffer(sink(sink));
        }

        //写入
        mRequestBody.writeTo(mBufferedSink);

        //必须调用flush，否则最后一部分数据可能不会被写入
        mBufferedSink.flush();

    }

    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //回调
                if (mProgressCallback != null) {
                    mProgressCallback.onProgress(bytesWritten, contentLength, bytesWritten == contentLength);
                }
            }
        };
    }
}
