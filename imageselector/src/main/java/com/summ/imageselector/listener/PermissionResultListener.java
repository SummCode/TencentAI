package com.summ.imageselector.listener;

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

public interface PermissionResultListener {

    /**
     * 已授权回调
     *
     * @param permissionCode 权限码
     */
    void onPermissionSuccess(int permissionCode);

    /**
     * 未授权回调
     *
     * @param permissionCode 权限码
     */
    void onPermissionFailed(int permissionCode);

    /**
     * 提示用户手动授权
     *
     * @param permissionCode 权限码
     */
    void onTipsUserPermission(int permissionCode);


}
