package com.summ.tool;


import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Describe：
 * <p>
 * 文件相关操作的工具类
 * </p>
 * @author:summ.Chen.
 * Create：2016/7/11.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */
public class FileUtils {

    private final static String FILE_PREFIX = "temp" + DateUtils.date2Str(new Date(), "_yyyy_MM_dd_HHmmssss");
    private final static String IMAGE_PREFIX = DateUtils.date2Str(new Date(), "yyyyMMddHHmmssss");
    private final static String IMAGE_SUFFIX_JPG = ".jpg";
    //    private final static String FILE_SUFFIX = ".tmp";
    private final static String FILE_SUFFIX = ".jpg";
    private static final String TEMPS = "Temps";

    /**
     * 新建文件
     *
     * @param path 文件的绝对路径
     * @return file
     */
    public static File crateFile(String path) {

        File file = new File(path);

        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    /**
     * 新建文件
     *
     * @param dirPath  文件目录
     * @param fileName 文件名
     * @return file
     */
    public static File crateFile(String dirPath, String fileName) {

        if (EmptyUtils.isEmpty(dirPath)) {
            return null;
        }

        if (EmptyUtils.isEmpty(fileName)) {
            return null;
        }

        File file = new File(dirPath, fileName);

        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    /**
     * 删除指定路径的文件
     *
     * @param path 文件的绝对路径
     * @return 成功删除true，删除失败false
     */
    public static boolean deleteFile(String path) {
        if (EmptyUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return deleteFile(file);
    }

    /**
     * 删除文件
     *
     * @param file 要删除的文件
     * @return 成功删除true，删除失败false
     */
    public static boolean deleteFile(File file) {

        if (file == null) {
            return false;
        }

        if (file.isDirectory()) {
            if (!EmptyUtils.isEmpty(file.listFiles())) {
                return false;
            }
        }

        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 拷贝文件
     *
     * @param sourceFilePath 源文件路径
     * @param destFilePath   目标文件路径
     * @return 成功拷贝true，拷贝失败false
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        File file = new File(destFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return writeFile(file, inputStream, false);
    }


    /**
     * 写文件
     *
     * @param file   目标文件
     * @param stream 源文件流
     * @param append 目标文件存在，是否覆盖
     * @return 成功true，失败false
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException Occurred.", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException Occurred.", e);
        } finally {
            try {
                o.close();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件名
     *
     * @param filePath   路径：/aa/bb/3.mp   ,http://gqdy365.iteye.com/blog/2112471.txt
     * @param fileSuffix 文件类型 mp3,png,jpg,txt.文件后缀名
     * @return
     */
    public static String getFileName(String filePath, String fileSuffix) {

        int startIndex = filePath.lastIndexOf("/");

        if (startIndex == -1) {

            if (EmptyUtils.isEmpty(fileSuffix)) {
                fileSuffix = "tmp";
            }

            return System.currentTimeMillis() + "." + fileSuffix;
        }

        String fileName = filePath.substring(startIndex + 1, filePath.length());


        return fileName;
    }


    /**
     * 获取文件绝对路径的父目录
     *
     * @param filePath 文件路径
     * @return 父目录
     */
    public static String getFileDir(String filePath) {

        int endIndex = filePath.lastIndexOf("/");

        if (endIndex == -1) {
            return filePath;
        }

        return filePath.substring(0, endIndex + 1);
    }

    /**
     * 文件大小单位转换
     *
     * @param size 文件大小
     * @return 1.2Gb,  23.5MB
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

    /**
     * 重命名文件名
     *
     * @param file    老文件
     * @param newfile 新文件
     * @return true:重名成功  false:命名失败
     */
    public static boolean rename(File file, File newfile) {

        if (file == null) {
            return false;
        }

        if (newfile == null) {
            return false;
        }

        return file.renameTo(newfile);
    }

    /**
     * 解压缩功能.
     * 将ZIP_FILENAME文件解压到ZIP_DIR目录下.
     *
     * @param zipFile   压缩文件
     * @param parentDir 解压到指定的目录
     * @return true: 解压成功， false:解压出错
     * @throws ZipException
     * @throws IOException
     */
    public static boolean upZipFile(File zipFile, String parentDir) {
        boolean result = false;
        ZipFile zfile = null;
        try {
            zfile = new ZipFile(zipFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                String dirstr = ze.getName();
                dirstr.trim();
                dirstr = getCodeStr(dirstr, "UTF-8");
                File f = new File(dirstr);
                f.mkdirs();
                LogUtils.i("Up Zip File is Dir: " + f.getAbsolutePath());
                continue;
            }

            OutputStream os = null;
            InputStream is = null;
            try {
                os = new BufferedOutputStream(new FileOutputStream(getRealFileName(parentDir, ze)));
                is = new BufferedInputStream(zfile.getInputStream(ze));
                int readLen = 0;
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
                result = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        try {
            zfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 解压文件保留子目录
     *
     * @param parentDir 指定根目录
     * @param zipEntry  压缩文件
     * @return java.io.File 实际的文件
     */
    public static File getRealFileName(String parentDir, ZipEntry zipEntry) {
        LogUtils.i("File Name  " + zipEntry.getName());
        String absFileName = zipEntry.getName();
        String[] dirs = absFileName.split("/");
        File ret = new File(parentDir);
        String substr;
        if (dirs.length > 1) {

            for (int i = 0; i < dirs.length - 1; i++) {
                substr = dirs[i];
                substr = getCodeStr(substr, "UTF-8");
                ret = new File(ret, substr);
            }

            LogUtils.i("Up Zip File Parent: " + ret.getAbsolutePath());

            if (!ret.exists()) ret.mkdirs();

            substr = dirs[dirs.length - 1];
            substr = getCodeStr(substr, "UTF-8");

            LogUtils.i("Up Zip File Name: " + substr);

            ret = new File(ret, substr);
            LogUtils.i("Up Zip File: " + ret.getAbsolutePath());
            return ret;
        }

        ret = new File(ret, absFileName);

        return ret;
    }


    /**
     * 解压文件，不保留子目录
     *
     * @param parentDir        指定根目录
     * @param relativeFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    public static File getRealFileName(String parentDir, String relativeFileName) {
        int start = relativeFileName.lastIndexOf("/") + 1;
        String fileName = relativeFileName.substring(start);
        fileName = getCodeStr(fileName, "UTF-8");

        File file = new File(parentDir, fileName);
        return file;
    }

    /**
     * 字符串编码转换
     *
     * @param str      字符串
     * @param codeType 转换的类型
     * @return 指定类型的字符串编码
     */
    private static String getCodeStr(String str, String codeType) {
        try {
            str.trim();
            str = new String(str.getBytes("8859_1"), codeType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 创建临时文件
     *
     * @return 临时文件
     */
    public static File crateTempFile() {
        File tempParentDir = getTempParentFile();
        File tempFile = null;
        try {
            tempFile = File.createTempFile(FILE_PREFIX, FILE_SUFFIX, tempParentDir);
            //在程序退出时删除临时文件
            tempFile.deleteOnExit();
        } catch (IOException e) {
            LogUtils.e(Log.getStackTraceString(e));
        } catch (NullPointerException n) {
            LogUtils.e(Log.getStackTraceString(n));
        }
        LogUtils.i("temp file path: " + tempFile.getAbsolutePath());
        return tempFile;
    }

    /**
     * 删除临时文件
     *
     * @return true：删除成功；false：删除失败
     */
    public static boolean deleteTempFile() {
        File fileDir = getTempParentFile();
        if (fileDir.exists()) {
            File[] files = fileDir.listFiles();
            for (File file : files) {
                FileUtils.deleteFile(file);
            }
            FileUtils.deleteFile(fileDir);
            return true;
        }
        return false;
    }

    /**
     * 获取临时文件保存目录
     *
     * @return file
     */
    private static File getTempParentFile() {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String tempDir = storageDir.getAbsolutePath() + File.separator + TEMPS + File.separator;
        File tempParentDir = new File(tempDir);

        if (!tempParentDir.exists()) {
            tempParentDir.mkdirs();
        }
        return tempParentDir;
    }

    /**
     * 获取临时文件保存目录
     *
     * @return file
     */
    private static File getPictureParentFile() {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        return storageDir;
    }

    /**
     * 文件转换成Uri
     *
     * @param context 上下文
     * @param file    文件
     * @return Uri
     */
    public static Uri getUriForFile(Context context, File file,String authority) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), authority, file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    /**
     * 创建图片文件
     *
     * @return URI
     */
    public static File imageFile() {
        File parentFile = FileUtils.getTempParentFile();
        File image = new File(parentFile, FILE_PREFIX + IMAGE_SUFFIX_JPG);
        LogUtils.i("image path: " + image.getAbsolutePath());
        return image;
    }

    /**
     * 创建图片文件
     *
     * @return URI
     */
    public static File saveImage() {
        File parentFile = FileUtils.getPictureParentFile();
        File image = new File(parentFile, IMAGE_PREFIX + IMAGE_SUFFIX_JPG);
        LogUtils.i("image path: " + image.getAbsolutePath());
        return image;
    }

}
