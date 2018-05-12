package com.summ.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Project Name：Common
 * Package Name：com.summ.base.tools
 * @author:summ.Chen
 * Create：2017/9/5
 * Email： summ_summ@163.com
 * Version：1.0.0
 * <p>
 * Describe：
 */

public class ImageUtils {


    public final static int IMAGE_MAX_SIZE = (int) (0.5f * 1024 * 1024);


    /**
     * 批量压缩图片
     *
     * @param picPaths 文件路径数组
     * @return List<File>
     */
    public List<File> compress(List<String> picPaths) {
        if (EmptyUtils.isEmpty(picPaths)) {
            return null;
        }

        List<File> compressImages = new ArrayList<>();

        for (String path : picPaths) {
            File file = compress(path);
            if (file == null) {
                continue;
            }
            compressImages.add(file);
        }


        return compressImages;
    }


    /**
     * 批量压缩图片
     *
     * @param fileMaxSize 压缩后文件最大值
     * @param picPaths    文件路径数组
     * @return List<File>
     */
    public List<File> compress(int fileMaxSize, List<String> picPaths) {
        if (EmptyUtils.isEmpty(picPaths)) {
            return null;
        }

        List<File> compressImages = new ArrayList<>();

        for (String path : picPaths) {
            File file = compress(path, fileMaxSize);
            if (file == null) {
                continue;
            }
            compressImages.add(file);
        }


        return compressImages;
    }

    /**
     * 压缩图片
     *
     * @param picPath 原始的路径
     * @return
     */
    public File compress(String picPath) {
        return compress(picPath, IMAGE_MAX_SIZE);
    }


    /**
     * 压缩图片
     *
     * @param picPath     原始的路径
     * @param fileMaxSize 设置压缩后的最大值
     * @return
     */
    public File compress(String picPath, int fileMaxSize) {
        File outputFile = new File(picPath);
        if (!fileExists(outputFile)) {
            return null;
        }
        long fileSize = outputFile.length();
        LogUtils.i("Compress before:" + fileSize);
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picPath, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
            outputFile = new File(FileUtils.crateTempFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            } else {
                File tempFile = outputFile;
                outputFile = new File(FileUtils.crateTempFile().getPath());
                copyFileUsingFileChannels(tempFile, outputFile);
            }
        }
        LogUtils.i("Compress  after:" + outputFile.length());
        return outputFile;

    }

    /**
     * 复制文件
     *
     * @param source 源文件
     * @param dest   目标文件
     */
    private void copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    /**
     * 检查文件是否存在
     *
     * @param file 文件
     * @return Boolean true：文件存在，false：文件不存，并提示用户
     */
    private boolean fileExists(File file) {
        if (file.exists()) {
            return true;
        }
        LogUtils.e(file.getName() + "文件不存");
        return false;
    }

    /**
     * 放大缩小Bitmap
     *
     * @param bitmap     Bitmap
     * @param widthScale 压缩比
     * @return 放大  or 缩小的Bitmap
     */
    public Bitmap scaleBitmap(Bitmap bitmap, float widthScale, float heightScale) {
        Matrix matrix = new Matrix();
        //长和宽放大缩小的比例
        matrix.postScale(widthScale, heightScale);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }


    /**
     * bitmap 保存文件
     *
     * @param file   存储文件图片
     * @param bitmap bitmap
     */
    public static File saveBitmap(File file, Bitmap bitmap) {
        if (file == null) {
            file = FileUtils.imageFile();
        }
        if (SDCardUtils.isSDCardEnable()) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (FileNotFoundException e) {
                LogUtils.e(e);
            } finally {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    LogUtils.e(e);
                }
            }
            return file;
        } else {
            return null;
        }

    }

    /**
     * bitmap转为base64
     *
     * @param bitmap Bitmap对象
     * @return 字符串
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data  base64图片
     * @return Bitmap
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


}
