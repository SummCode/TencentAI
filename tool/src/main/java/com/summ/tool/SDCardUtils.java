package com.summ.tool;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Describe：
 * <p>
 * SD Card工具类
 * </p>
 * @author:summ.Chen.
 * Create：2016/10/19.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */

public class SDCardUtils {
    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 获取SD卡公用文件夹
     *
     * @param type 类型 音乐、图片、视频等
     * @return
     */
    public static File getPublicDirectory(String type) {
        return Environment.getExternalStoragePublicDirectory(type);
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks;
            long freeBlocks;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                availableBlocks = stat.getAvailableBlocksLong() - 4;
                freeBlocks = stat.getAvailableBlocksLong();
            } else {
                availableBlocks = stat.getAvailableBlocks() - 4;
                freeBlocks = stat.getAvailableBlocks();
            }
            // 获取单个数据块的大小（byte）
            return freeBlocks * availableBlocks;

        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath 文件路径
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            long availableBlocks = stat.getAvailableBlocksLong() - 4;
            return stat.getBlockSizeLong() * availableBlocks;
        } else {
            long availableBlocks = stat.getAvailableBlocks() - 4;
            return stat.getBlockCount() * availableBlocks;
        }
    }

    /**
     * 获取系统存储路径
     *
     * @return sd根目录
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }


    /**
     * SD卡缓存路径
     *
     * @param context  上下文
     * @param cacheDir 文件目录
     * @return 文件夹
     */
    public static File getDiskCacheDir(Context context, String cacheDir) {

        String tempDir = context.getExternalCacheDir().getAbsolutePath();

        File file = new File(tempDir, cacheDir);

        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }


}
