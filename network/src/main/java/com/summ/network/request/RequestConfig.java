package com.summ.network.request;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.summ.network.listener.RequestListener;
import com.summ.network.response.LogInterceptor;
import com.summ.tool.EmptyUtils;
import com.summ.tool.LogUtils;
import com.summ.tool.PropertiesUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Describe：
 * <p>
 * <p>
 *
 * @author:summ.Chen. Create：2017/2/5.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */

public class RequestConfig {

    /**
     * 证书类型
     */
    private static final String NETWORK_KEY_CONFIG = "network";

    /**
     * 证书类型
     */
    private static final String KEY_STORE_TYPE_BKS = "bks";

    /**
     * 证书类型
     */
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";

    /**
     * 安全策略等级
     */
    private static String configLevel = "0";

    /**
     * 证书密码（应该是客户端证书密码）
     */
    private static String keyStorePassword;

    /***
     * 授信证书密码（应该是服务端证书密码）
     */
    private static String keyStoreTrustPassword;


    /***
     * 证书认证校验服务器
     */
    private static String hostname;


    /**
     * 连接超时时间
     */
    public static final int CONNECT_TIME_OUT = 15;
    /**
     * IO写超时时间
     */
    public static final int WRITE_TIME_OUT = 20;

    /**
     * IO读超时时间
     */
    public static final int READ_TIME_OUT = 20;

    /**
     * 缓存大小
     */
    public static final int CACHE_SIZE = 10 * 1024 * 1024;

    /**
     * get请求方法
     */
    public static final int GET = 1;

    /**
     * Post请求方法
     */
    public static final int POST = 2;

    /**
     * Put请求方法
     */
    public static final int PUT = 3;

    /**
     * Delete请求方法
     */
    public static final int DELETE = 4;


    /**
     * 缓存目录
     * TODO  SD卡缓存保存目录
     */
    public static final File SD_CACHE = new File("");

    /**
     * 网络请求缓存
     */
    public static Cache mCache = new Cache(SD_CACHE.getAbsoluteFile(), CACHE_SIZE);

    /**
     * 网络请求缓存
     */
    private static Context mContext;

    /**
     * 网络请求初始化参数配置
     */
    private static OkHttpClient.Builder HTTPS_CLIENT_BUILDER = getHttpClient();

    @NonNull
    private static OkHttpClient.Builder getHttpClient() {
        X509TrustManager trustManager = new TrustAllCerts();

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .cache(mCache)
                .addInterceptor(new LogInterceptor())
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS);

        switch (configLevel) {
            case "0":
                SSLSocketFactory sslSocketFactory = getSSLSocketFactory(trustManager);
                builder.sslSocketFactory(sslSocketFactory, trustManager);
                builder.hostnameVerifier(new TrustAllHostnameVerifier());
                break;
            case "1":
                break;
            case "2":
                SSLSocketFactory sslSocketFactoryForOneWay = getSSLSocketFactoryForOneWay(mContext);
                builder.sslSocketFactory(sslSocketFactoryForOneWay, trustManager);
                builder.hostnameVerifier(new SafeHostnameVerifier());
                break;
            case "3":
                SSLSocketFactory sslSocketFactoryForTwoWay = getSSLSocketFactoryForTwoWay(mContext);
                builder.sslSocketFactory(sslSocketFactoryForTwoWay, trustManager);
                builder.hostnameVerifier(new SafeHostnameVerifier());
                break;
            default:

        }


        return builder;
    }

    /**
     * 网络请求客户端
     */
    public static final OkHttpClient HTTPS_CLIENT = HTTPS_CLIENT_BUILDER.build();

    /**
     * json方式提交数据
     */
    public static final MediaType CONTENT_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 表单方式提交数据
     */
    public static final MediaType CONTENT_TYPE_FORM = MediaType.parse("multipart/form-data; charset=utf-8");

    /**
     * 上传图片
     */
    public static final MediaType CONTENT_TYPE_PNG = MediaType.parse("image/png");

    /**
     * 文本方式提交数据
     */
    public static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/x-markdown; charset=utf-8");

    /**
     * 任意类型数据
     */
    public static final MediaType CONTENT_TYPE_FILE = MediaType.parse("*/*; charset=utf-8");

    /**
     * 请求头数据
     */
    private Map<String, String> mHeadersMap;


    /**
     * 构造函数
     */
    public RequestConfig() {

        initHeaders(null);
    }

    /**
     * 构造函数
     */
    public RequestConfig(Map<String, String> headMap) {
        initHeaders(headMap);
    }

    public void initConfig(Context context) {
        mContext = context;
        configLevel = getConfigLevel(context);
        hostname = getHostName(context);
        keyStorePassword = getKeyStorePassword(context);
        keyStoreTrustPassword = getKeyStoreTrustPassword(context);
        Log.i("NetworkConfig", "Level: " + configLevel + "   Host Name: " + hostname + "   Client Pwd: " + keyStorePassword + "   Server Pwd: " + keyStoreTrustPassword);
    }

    /**
     * 获取安全策略配置
     *
     * @param context 上下文
     * @return 安全等级
     */
    private String getConfigLevel(Context context) {
        return PropertiesUtils.getProperties(context, NETWORK_KEY_CONFIG).getProperty("config_level");
    }

    /**
     * 获取服务器证书密码
     *
     * @param context 上下文
     * @return 密码
     */
    private String getKeyStoreTrustPassword(Context context) {
        return PropertiesUtils.getProperties(context, NETWORK_KEY_CONFIG).getProperty("key_store_trust_password");
    }

    /**
     * 获取客户端证书密码
     *
     * @param context 上下文
     * @return 密码
     */
    private String getKeyStorePassword(Context context) {
        return PropertiesUtils.getProperties(context, NETWORK_KEY_CONFIG).getProperty("key_store_password");
    }

    /**
     * @param context
     * @return
     */
    private String getHostName(Context context) {
        return PropertiesUtils.getProperties(context, NETWORK_KEY_CONFIG).getProperty("host_name", "");
    }

    /**
     * 初始化默认请求头
     */
    private void initHeaders(Map<String, String> headMap) {
        mHeadersMap = new HashMap<>();
        if (EmptyUtils.isEmpty(headMap)) {
            return;
        }
        mHeadersMap.putAll(headMap);
    }

    /**
     * 设置请求头
     *
     * @param headMap 添加的头部信息
     */
    public Headers setHeaders(Map<String, String> headMap) {

        Headers.Builder headers = new Headers.Builder();

        if (EmptyUtils.isEmpty(headMap)) {
            return headers.build();
        }

        mHeadersMap.putAll(headMap);

        Set<Map.Entry<String, String>> set = mHeadersMap.entrySet();

        for (Map.Entry<String, String> entry : set) {
            headers.add(entry.getKey(), entry.getValue());
        }

        return headers.build();

    }

    /**
     * 获取Request对象
     *
     * @return request对象
     */
    public Request.Builder getRequestBuilder() {
        return getRequestBuilder(null);
    }


    /**
     * 获取Request对象
     *
     * @return request对象
     */
    public Request.Builder getRequestBuilder(Map<String, String> headMap) {

        Request.Builder builder = new Request.Builder();

        Headers headers = setHeaders(headMap);

        if (headers != null) {
            builder.headers(headers);
        }

        return builder;

    }

    /**
     * 选择是否监听请求进度
     *
     * @param callback    请求的回调
     * @param builder     Request.Builder
     * @param requestBody 请求体
     * @return Request对象
     */
    public Request getRequestProgress(RequestListener callback, Request.Builder builder, RequestBody requestBody) {

        //监听上传进度的请求体
        RequestProgressBody requestProgressBody = new RequestProgressBody(requestBody, callback);

        return getRequest(POST, builder, requestProgressBody);

    }


    /**
     * 获取Request对象
     *
     * @param builder     Request.Builder
     * @param requestBody 请求体
     * @return Request对象
     */
    public Request getRequest(int method, Request.Builder builder, RequestBody requestBody) {

        switch (method) {
            case GET:
                builder.get();
                break;
            case POST:
                builder.post(requestBody);
                break;
            case PUT:
                builder.put(requestBody);
                break;
            case DELETE:
                builder.delete(requestBody);
                break;
            default:
        }

        Request request = builder.build();

        return request;
    }

    /**
     * 设置form-data,参数
     *
     * @param bodyMap 参数
     * @return FormBody.Builder对象
     */
    public FormBody.Builder setBody(String url, Map<String, Object> bodyMap) {

        if (EmptyUtils.isEmpty(bodyMap)) {
            LogUtils.i("Request Url: " + url + "  Form Body: ");
            return null;
        }

        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        Set<Map.Entry<String, Object>> set = bodyMap.entrySet();

        StringBuilder bodyBuilder = new StringBuilder();

        int pos = 0;

        for (Map.Entry<String, Object> entry : set) {

            if (entry.getValue() == null) {
                continue;
            }

            String param = entry.getValue().toString();

            if (EmptyUtils.isEmpty(param)) {
                continue;
            }

            formBodyBuilder.add(entry.getKey(), entry.getValue().toString());

            String temp = String.format("%s=%s", entry.getKey(), param);

            if (pos > 0) {
                bodyBuilder.append(", ");
            }

            bodyBuilder.append(temp);
            pos++;
        }

        LogUtils.i("Request Url: " + url + "  Form Body: " + bodyBuilder.toString());

        return formBodyBuilder;
    }

    /**
     * 带参数的get请求，拼接
     *
     * @param url     请求地址
     * @param bodyMap 参数
     * @return bodyMap不为空，Url地址有参数（url?key=value&key=value）
     */
    public String getParamUrl(String url, Map<String, Object> bodyMap) {


        if (EmptyUtils.isEmpty(bodyMap)) {
            LogUtils.i("Request Url: " + url);
            return url;
        }

        int pos = 0;

        Set<Map.Entry<String, Object>> set = bodyMap.entrySet();

        StringBuilder tempParams = null;

        for (Map.Entry<String, Object> entry : set) {
            //过滤参数为null
            if (entry.getValue() == null) {
                continue;
            }

            if (pos == 0) {
                tempParams = new StringBuilder(String.format("%s?", url));
            }

            if (pos > 0 && tempParams != null) {
                tempParams.append("&");
            }
            tempParams.append(String.format("%s=%s", entry.getKey(), entry.getValue().toString()));
            pos++;
        }

        String temp = tempParams == null ? url : tempParams.toString();

        LogUtils.i("Request Url: " + temp);

        return temp;
    }


    private static class TrustAllCerts implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                chain[0].checkValidity();
            } catch (Exception e) {
                throw new CertificateException("Certificate not valid or trusted.");
            }
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                chain[0].checkValidity();
            } catch (Exception e) {
                throw new CertificateException("Certificate not valid or trusted.");
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class SafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            //校验hostname是否正确，如果正确则建立连接
            if (hostname.equals(hostname)) {
                return true;
            }
            return false;
        }
    }


    /**
     * 获取SSLSocketFactory
     *
     * @return SSLSocketFactory
     */
    private static SSLSocketFactory getSSLSocketFactory(X509TrustManager trustManager) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }

    /**
     * 获取X509TrustManager
     *
     * @return X509TrustManager
     */
    private static X509TrustManager getX509TrustManager(KeyStore keyStore) {
        TrustManagerFactory trustManagerFactory = null;
        try {
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    /**
     * 单向认证SSLSocketFactory
     *
     * @return SSLSocketFactory
     */
    public static SSLSocketFactory getSSLSocketFactoryForOneWay(Context context) {
        KeyStore trustStore = getTrustStore(context);
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory factory = sslContext.getSocketFactory();
            return factory;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return null;

    }


    /**
     * 双向认证SSLSocketFactory
     *
     * @return SSLSocketFactory
     */
    public static SSLSocketFactory getSSLSocketFactoryForTwoWay(Context context) {

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyStore trustStore = getTrustStore(context);

            KeyStore keyStore = getClientStore(context);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory factory = sslContext.getSocketFactory();
            return factory;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 客户端证书
     *
     * @param context 上下文
     * @return 客户KeyStore
     */
    public static KeyStore getClientStore(Context context) {
        InputStream clientInput = null;
        try {
            clientInput = context.getAssets().open("outgoing.CertwithKey.pkcs12");
            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
            keyStore.load(clientInput, keyStorePassword.toCharArray());
            return keyStore;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } finally {
            try {
                clientInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;

    }

    /**
     * 服务器授信证书
     *
     * @param context 上下文
     * @return 客户KeyStore
     */
    public static KeyStore getTrustStore(Context context) {
        InputStream trustInput = null;
        try {
            trustInput = context.getAssets().open("srca.cer");
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null);
            return trustStore;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } finally {
            try {
                trustInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;

    }

}