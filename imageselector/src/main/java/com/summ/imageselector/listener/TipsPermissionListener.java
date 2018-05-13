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

public interface TipsPermissionListener {

    /**
     * 打开系统应用权限管理
     *
     * @param permissionCode 权限码
     */
    void onConfirm(int permissionCode);

    /**
     * 关闭授权提示
     *
     * @param permissionCode 权限码
     */
    void onCancel(int permissionCode);

}
