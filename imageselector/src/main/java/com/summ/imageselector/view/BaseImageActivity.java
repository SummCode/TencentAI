package com.summ.imageselector.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.summ.imageselector.entity.ImageEntity;
import com.summ.imageselector.listener.PermissionManager;
import com.summ.imageselector.listener.PermissionResultListener;
import com.summ.imageselector.listener.TipsPermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author：Summ
 * @date：2018/3/23
 * @email： summ_summ@163.com
 * version：1.0.0
 * <p>
 * describe：
 * <p>
 * <p>
 */
public abstract class BaseImageActivity extends FragmentActivity implements PermissionManager, PermissionResultListener, TipsPermissionListener {

    public final static int READ_EXTERNAL_STORAGE = 90001;
    public final static int WRITE_EXTERNAL_STORAGE = 90002;

    public final static String IMAGE_FROM_TYPE = "image_from_type";

    public final static int IMAGE_FROM_PHONE = 0;
    public final static int IMAGE_FROM_INTENT = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(putContentView());
        initView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SINGLE_AUTHORIZATION_REQUEST_CODE:
            case MULTINOMIAL_AUTHORIZATION_REQUEST_CODE:
                authorizationCallback(permissions, grantResults);
                break;
            default:
        }


    }

    /**
     * 添加layout文件
     *
     * @return layoutId
     */
    public abstract int putContentView();

    /**
     * 初始View
     */
    public abstract void initView();

    /**
     * 获取Bundle
     *
     * @return 传递数据bundle对象
     */
    private Bundle getBundle() {
        return getIntent().getExtras();
    }

    /**
     * 获取传入数据
     *
     * @return 泛型数据
     */
    public <T> T getIntentData(String key, T defaultValue) {

        if (getBundle() != null) {
            return (T) getBundle().get(key);
        }

        return defaultValue;
    }


    /**
     * 判断字符串是否为空
     *
     * @param str 要检查的对象
     * @return true: null or 对象大小为0
     */
    public boolean isEmpty(String str) {
        if (str != null && !str.isEmpty() && !str.equals("null")) {
            return false;
        }
        return true;
    }

    /**
     * File转换ImageEntity
     *
     * @param files 文件集合
     * @return List<ImageEntity>
     */
    public List<ImageEntity> constructionItemImage(List<File> files) {
        List<ImageEntity> tempList = new ArrayList<>();
        int count = files.size();
        for (int i = 0; i < count; i++) {
            String path = files.get(i).getAbsolutePath();
            tempList.add(new ImageEntity(i, path));
        }
        return tempList;
    }


    /**
     * 打开系统设置权限
     *
     * @param permissionCode 权限码
     */
    @Override
    public void openSystemAppSetting(int permissionCode) {
        String permission = getPermissionName(permissionCode);
        if (!isEmpty(permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, permissionCode);
            } else {
                openAppDetailSetting(this);
            }
        }
    }

    /**
     * 打开App详情设置
     *
     * @param context 上下文
     */
    @Override
    public void openAppDetailSetting(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        startActivity(localIntent);
    }


    /**
     * 申请授权
     *
     * @param permissionCode 权限码
     */
    @Override
    public void applyPermission(int permissionCode) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onPermissionSuccess(permissionCode);
            return;
        }

        String permissionStr = getPermissionName(permissionCode);
        if (ContextCompat.checkSelfPermission(this, permissionStr) == PackageManager.PERMISSION_DENIED) {
            if (!isEmpty(permissionStr)) {
                ActivityCompat.requestPermissions(this, new String[]{permissionStr}, SINGLE_AUTHORIZATION_REQUEST_CODE);
            }
        } else {
            onPermissionSuccess(permissionCode);
        }

    }

    /**
     * 批量申请授权
     *
     * @param permissionCode 权限组
     */
    @Override
    public void requestPermission(int... permissionCode) {

        int[] codes = permissionCode;
        int size = codes.length;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            for (Integer integer : codes) {
                onPermissionSuccess(integer);
            }

        } else {

            ArrayList<String> permissionList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                String permissionStr = getPermissionName(codes[i]);
                if (ContextCompat.checkSelfPermission(this, permissionStr) != PackageManager.PERMISSION_GRANTED) {
                    if (!isEmpty(permissionStr)) {
                        permissionList.add(permissionStr);
                    }
                } else {
                    onPermissionSuccess(codes[i]);
                }
            }

            int requestPermissionCount = permissionList.size();
            if (requestPermissionCount > 0) {
                String[] permissions = permissionList.toArray(new String[requestPermissionCount]);
                //向系统批量申请权限
                ActivityCompat.requestPermissions(this, permissions, MULTINOMIAL_AUTHORIZATION_REQUEST_CODE);
            }

        }


    }


    /**
     * 授权结果的回调
     *
     * @param permissions  请求的权限组
     * @param grantResults 授权结果
     */
    @Override
    public void authorizationCallback(String[] permissions, int[] grantResults) {
        int size = grantResults.length;
        if (size == 0) {
            return;
        }

        for (int i = 0; i < size; i++) {
            String permission = permissions[0];
            int permissionCode = getPermissionCode(permission);
//            LogUtils.i("apply permission result: " + grantResults[i] + "   " + permission);
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                //权限已获取
                onPermissionSuccess(permissionCode);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        //false 勾选了不再询问并拒接授权，需要引导用户手动设置授权
                        onTipsUserPermission(permissionCode);
//                        LogUtils.w("tips user author" + permission);
                    } else {
                        onPermissionFailed(permissionCode);
//                        LogUtils.w("app need this\" " + permission + " \"author,user denied");
                    }

                } else {
                    onPermissionFailed(permissionCode);
//                    LogUtils.w("app need this\" " + permission + " \"author,user denied");
                }

            }
        }
    }


    @Override
    public void onPermissionSuccess(int permissionCode) {

    }

    @Override
    public void onPermissionFailed(int permissionCode) {

    }

    @Override
    public void onTipsUserPermission(int permissionCode) {

    }

    @Override
    public void onConfirm(int permissionCode) {

    }

    @Override
    public void onCancel(int permissionCode) {

    }


    /**
     * 根据权限名，获取权限码
     *
     * @param permissionName 权限名
     * @return Permission Code
     */
    public int getPermissionCode(String permissionName) {
        switch (permissionName) {
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return READ_EXTERNAL_STORAGE;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return WRITE_EXTERNAL_STORAGE;
            default:
        }

        return 0;
    }

    /**
     * 根据权限码，获取权限名
     *
     * @param permissionCode 权限码
     * @return Permission Name
     */
    public String getPermissionName(int permissionCode) {
        switch (permissionCode) {
            case READ_EXTERNAL_STORAGE:
                return Manifest.permission.READ_EXTERNAL_STORAGE;
            case WRITE_EXTERNAL_STORAGE:
                return Manifest.permission.WRITE_EXTERNAL_STORAGE;
            default:
        }

        return "";
    }

    /**
     * 根据权限码，获取权限名
     *
     * @param permissionCode 权限码
     * @return
     */
    public String getTitle(int permissionCode) {
        StringBuilder title = new StringBuilder();
        switch (permissionCode) {
            case READ_EXTERNAL_STORAGE:
                title.append("");
                break;
            case WRITE_EXTERNAL_STORAGE:
                title.append("");
                break;
            default:
        }

        return title.append("授权提示").toString();
    }

    /**
     * 提示用户授权内容
     *
     * @param permissionCode 权限码
     * @return 提示内容
     */
    public String getTips(int permissionCode) {
        String permissionName = "";
        switch (permissionCode) {
            case READ_EXTERNAL_STORAGE:
                permissionName = "外部设备存储";
                break;
            case WRITE_EXTERNAL_STORAGE:
                permissionName = "外部设备存储";
                break;
            default:
        }

        return "使用这功能，需要授予“" + permissionName + "”的权限。\n设置 --> 应用信息 --> 权限，打开相关权限。";
    }


}
