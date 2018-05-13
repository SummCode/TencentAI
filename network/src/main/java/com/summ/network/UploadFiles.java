package com.summ.network;


import com.summ.network.callback.HttpUploadCallback;
import com.summ.network.request.RequestConfig;
import com.summ.tool.EmptyUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Describe：
 * <p>
 * 文件上传
 * <p>
 *
 * @author:summ.Chen. Create：2016/8/30.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */
public class UploadFiles {

    private static UploadFiles instance;

    private OkHttpClient mClient = RequestConfig.HTTPS_CLIENT;

    public static UploadFiles getInstance() {
        if (instance == null) {
            synchronized (UploadFiles.class) {
                if (instance == null) {
                    instance = new UploadFiles();
                }
            }
        }
        return instance;
    }

    /**
     * 单个文件上传
     *
     * @param url      上传地址
     * @param filePath 文件路径
     * @param callback 请求的回调
     * @return Call对象
     */
    public Call uploadFile(String url, String filePath, HttpUploadCallback callback) {
        return uploadFile(url, "file", filePath, callback);
    }

    /**
     * 单个文件上传
     *
     * @param url      上传地址
     * @param file     文件
     * @param callback 请求的回调
     * @return Call对象
     */
    public Call uploadFile(String url, File file, HttpUploadCallback callback) {
        return uploadFile(url, "file", file, callback);
    }

    /**
     * 单个文件上传
     *
     * @param url      上传地址
     * @param fileKey  文件类型
     * @param file     文件路径
     * @param callback 请求的回调
     * @return Call对象
     */
    public Call uploadFile(String url, String fileKey, File file, HttpUploadCallback callback) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put(fileKey, file);
        return uploadFile(url, bodyMap, callback);
    }

    /**
     * 单个文件上传
     *
     * @param url      上传地址
     * @param fileKey  文件类型
     * @param filePath 文件路径
     * @param callback 请求的回调
     * @return Call对象
     */
    public Call uploadFile(String url, String fileKey, String filePath, HttpUploadCallback callback) {

        File file = new File(filePath);
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put(fileKey, file);
        return uploadFile(url, bodyMap, callback);
    }

    /**
     * 单个文件上传file,添加请求体bodyMap里
     *
     * @param url      上传地址
     * @param bodyMap  请求体
     * @param callback 请求的回调
     * @return Call对象
     */
    public Call uploadFile(String url, Map<String, Object> bodyMap, HttpUploadCallback callback) {


        if (EmptyUtils.isEmpty(url)) {
            new NullPointerException("Request url is not null.");
        }

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(RequestConfig.CONTENT_TYPE_FORM);

        if (!EmptyUtils.isEmpty(bodyMap)) {

            Set<String> set = bodyMap.keySet();

            for (String key : set) {

                if (bodyMap.get(key) == null) {
                    continue;
                }

                if (bodyMap.get(key) instanceof File) {
                    File file = (File) bodyMap.get(key);
                    RequestBody fileBody = RequestBody.create(RequestConfig.CONTENT_TYPE_FILE, file);
                    multipartBuilder.addFormDataPart(key, file.getName(), fileBody);
                    continue;
                }

                String param = bodyMap.get(key).toString();

                if (!EmptyUtils.isEmpty(param)) {
                    multipartBuilder.addFormDataPart(key, param);
                }

            }

        }


        RequestBody requestBody = multipartBuilder.build();

        Request.Builder builder = new Request.Builder().url(url);

        RequestConfig requestConfig = new RequestConfig();

        Request request = requestConfig.getRequestProgress(callback, builder, requestBody);

        Call call = mClient.newCall(request);

        call.enqueue(callback);

        return call;
    }

    /**
     * 多文件上传files
     *
     * @param url       上传地址
     * @param filePaths 上传文件集合
     * @param fileKey   key
     * @param callback  请求的回调
     * @return Call对象
     */
    public Call uploadFile(String url, List<String> filePaths, String fileKey, HttpUploadCallback callback) {

        if (EmptyUtils.isEmpty(filePaths)) {
            return null;
        }

        List<File> files = new ArrayList<>();

        for (String path : filePaths) {
            files.add(new File(path));
        }


        return uploadFile(url, null, fileKey, files, callback);
    }

    /**
     * 多文件上传files
     *
     * @param url      上传地址
     * @param fileKey  key
     * @param files    上传文件集合
     * @param callback 请求的回调
     * @return Call对象
     */
    public Call uploadFile(String url, String fileKey, List<File> files, HttpUploadCallback callback) {
        return uploadFile(url, null, fileKey, files, callback);
    }

    /**
     * 多文件上传files,添加请求参数 bodyMap
     *
     * @param url      上传地址
     * @param callback 请求的回调
     * @param bodyMap  请求体
     * @param files    上传文件
     * @param callback 回调
     * @return Call对象
     */
    public Call uploadFile(String url, Map<String, Object> bodyMap, String fileKey, List<File> files, HttpUploadCallback callback) {

        if (EmptyUtils.isEmpty(url)) {
            new NullPointerException("Request url is not null.");
        }

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(RequestConfig.CONTENT_TYPE_FORM);

        if (!EmptyUtils.isEmpty(bodyMap)) {

            Set<String> set = bodyMap.keySet();

            for (String key : set) {

                if (bodyMap.get(key) == null) {
                    continue;
                }
                String param = bodyMap.get(key).toString();
                if (!EmptyUtils.isEmpty(param)) {
                    multipartBuilder.addFormDataPart(key, param);
                }

            }

        }

        if (!EmptyUtils.isEmpty(files)) {
            for (File file : files) {
                RequestBody fileBody = RequestBody.create(RequestConfig.CONTENT_TYPE_FILE, file);
                multipartBuilder.addFormDataPart(fileKey, file.getName(), fileBody);
            }
        }

        RequestBody requestBody = multipartBuilder.build();

        Request.Builder builder = new Request.Builder().url(url);

        RequestConfig requestConfig = new RequestConfig();

        Request request = requestConfig.getRequestProgress(callback, builder, requestBody);

        Call call = mClient.newCall(request);

        call.enqueue(callback);

        return call;
    }


}
