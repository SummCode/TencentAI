package com.summ.network.callback;

import android.util.Log;


import com.summ.tool.FileUtils;
import com.summ.tool.LogUtils;
import com.summ.network.response.body.RequestResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Describe：
 * <p>
 * 进度回调
 * <p>
 *
 * @@author:summ.Chen
 * @email： summ_summ@163.com
 * Create：2016/9/2.
 * Version：1.0.0
 */
public class HttpDownloadCallback<T> extends HttpUploadCallback<T> {

    private File mFile;

    public File getFile() {
        return mFile;
    }

   public HttpDownloadCallback() {
    }




    /**
     * 文件下载监听
     *
     * @param savePath 文件保存路径
     */
    public HttpDownloadCallback(String savePath) {
        this.mFile = init(savePath);
    }


    /**
     * 文件下载监听
     *
     * @param saveFile 下载的文件
     */
    public HttpDownloadCallback(File saveFile) {
        this.mFile = saveFile;
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String url = getRequestUrl(call);
        mResult = new RequestResult(url);
        onWriteFile(response, mFile);
    }


    @Override
    public void onFailure(Call call, IOException e) {
        super.onFailure(call, e);
    }


    /**
     * 初始化
     *
     * @param filePath 文件绝对路劲
     * @return File
     */
    public File init(String filePath) {
        String fileDir = FileUtils.getFileDir(filePath);

        File fileParent = new File(fileDir);

        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }

        return new File(filePath);
    }

    /**
     * 写文件
     *
     * @param response 响应体
     */
    private void onWriteFile(Response response, File file) {

        InputStream inputStream = response.body().byteStream();
        int code = response.code();

        String fileName = "“" + file.getName() + "” ";

        String body = file.getAbsolutePath();
        String meagess = fileName + "文件下载成功";

        //创建buffer
        BufferedSource source = null;
        BufferedSink sink = null;
        try {
            source = Okio.buffer(Okio.source(inputStream));
            sink = Okio.buffer(Okio.sink(file));
            //写数据
            sink.writeAll(source);
        } catch (IOException e) {
            code = RESPONSE_RESULT_TYPE_FAILED;
            meagess = fileName + "文件下载失败";
            body = e.getMessage();
        } finally {
            closeSink(sink);
            closeSource(source);
            mResult.setCode(code);
            mResult.setMessage(meagess);
            mResult.setResponseBody(body);
            mNetworkHandler.sendEmptyMessage(code);
        }

    }


    /**
     * 写文件
     *
     * @param response 响应体
     */
    private void onWrite(Response response, File file) {

        InputStream inputStream = response.body().byteStream();
        FileOutputStream fileOutputStream = null;
        int code = response.code();

        String fileName = "“" + file.getName() + "” ";

        String body = "File save path " + file.getAbsolutePath();
        String meagess = fileName + "文件下载成功";
        try {
            fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[2048];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
        } catch (IOException e) {
            code = RESPONSE_RESULT_TYPE_FAILED;
            body = e.getMessage();
            meagess = fileName + "文件下载失败";
        } finally {
            closeInputStream(inputStream);
            closeFileOutputStream(fileOutputStream);

            mResult.setCode(code);
            mResult.setMessage(meagess);
            mResult.setResponseBody(body);
            mNetworkHandler.sendEmptyMessage(code);
        }

    }


    /**
     * 关闭Source
     *
     * @param source 要关闭的Source
     */
    private void closeSource(BufferedSource source) {
        if (source != null) {
            try {
                source.close();
            } catch (IOException e) {
                LogUtils.e(Log.getStackTraceString(e));
            }
        }
    }

    /**
     * 关闭Sink
     *
     * @param sink 要关闭的Sink
     */
    private void closeSink(BufferedSink sink) {
        if (sink != null) {
            try {
                sink.close();
            } catch (IOException e) {
                LogUtils.e(Log.getStackTraceString(e));
            }

        }
    }


    /**
     * 关闭输出流
     *
     * @param fileOutputStream 输出流
     */
    private void closeFileOutputStream(FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                fileOutputStream = null;
            }
        }
    }

    /**
     * 关闭输入流
     *
     * @param inputStream 输入流
     */
    private void closeInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                inputStream = null;
            }
        }
    }


}
