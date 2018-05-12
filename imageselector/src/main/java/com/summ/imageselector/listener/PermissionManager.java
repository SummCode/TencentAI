package com.summ.imageselector.listener;

import android.content.Context;

/**
 * @@author:Summ
 * @date：2016/12/3
 * @email： summ_summ@163.com
 * <p>
 * project name：Common
 * package name：com.summ.frame.listener
 * version：1.0.0
 * <p>
 * describe：
 * <p>
 * <p>
 */
public interface PermissionManager {

    int SINGLE_AUTHORIZATION_REQUEST_CODE = 20001;
    int MULTINOMIAL_AUTHORIZATION_REQUEST_CODE = 20002;

    /**
     * 打开系统设置权限
     *
     * @param permissionCode 权限码
     */
    void openSystemAppSetting(int permissionCode);

    /**
     * 打开App详情设置
     *
     * @param context 上下文
     */
    void openAppDetailSetting(Context context);

    /**
     * 申请授权
     *
     * @param permissionCode 权限码
     */
    void applyPermission(int permissionCode);

    /**
     * 批量申请授权
     *
     * @param permissionCode 权限组
     */
    void requestPermission(int... permissionCode);

    /**
     * 授权结果的回调
     *
     * @param permissions  请求的权限组
     * @param grantResults 授权结果
     */
    void authorizationCallback(String[] permissions, int[] grantResults);
}
