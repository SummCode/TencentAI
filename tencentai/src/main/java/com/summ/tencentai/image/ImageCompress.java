package com.summ.tencentai.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Describe：
 * <p>
 * 压缩图片的工具类
 * <p>
 * Author：summ.Chen.
 * Create：2016/12/7.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */

public class ImageCompress {

    private final static String FILE_PREFIX = "temp" + date2Str(new Date(), "_yyyy_MM_dd_HHmm");
    private final static String IMAGE_PREFIX = date2Str(new Date(), "yyyyMMddHHmmssss");
    private final static String IMAGE_SUFFIX_JPG = ".jpg";
    private final static String FILE_SUFFIX = ".jpg";
    private static final String TEMPS = "Temps";

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public final static int IMAGE_MAX_SIZE = 512 * 1024;
    public final static String TAG = "LinAn";

    private static ImageCompress instance;

    public static ImageCompress getInstance() {
        if (instance == null) {
            synchronized (ImageCompress.class) {
                if (instance == null) {
                    instance = new ImageCompress();
                }
            }
        }
        return instance;
    }


    public List<File> compress(List<String> picPaths) {
        if (picPaths == null || picPaths.size() == 0) {
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


    public List<File> compress(int fileMaxSize, List<String> picPaths) {
        if (picPaths == null || picPaths.size() == 0) {
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


    public File compress(String picPath) {
        return compress(picPath, IMAGE_MAX_SIZE);
    }


    public File compress(String picPath, int fileMaxSize) {
        File outputFile = new File(picPath);
        if (!fileExists(outputFile)) {
            return null;
        }

        long fileSize = outputFile.length();
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
            outputFile = new File(crateTempFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            } else {
                File tempFile = outputFile;
                outputFile = new File(crateTempFile().getPath());
                copyFileUsingFileChannels(tempFile, outputFile);
            }
        }
        return outputFile;

    }


    private void copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean fileExists(File file) {
        if (file.exists()) {
            return true;
        }
        Log.i(TAG, "\"" + file.getAbsolutePath() + "\",文件不存.");
        return false;
    }


    public Bitmap scaleBitmap(Bitmap bitmap, float widthScale, float heightScale) {
        Matrix matrix = new Matrix();
        matrix.postScale(widthScale, heightScale);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    /**
     * 保存bitmap图片
     *
     * @param file   储存bitmap文件
     * @param bitmap bitmap
     * @return file
     */
    public File saveBitmap(File file, Bitmap bitmap) {

        if (file == null || !file.exists()) {
            file = imageFile();
        }

        if (isSDCardEnable()) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return file;
        } else {
            return null;
        }

    }

    /**
     * 创建图片文件
     *
     * @return URI
     */
    public File imageFile() {
        File tempParentDir = getTempParentFile();
        File image = new File(tempParentDir, FILE_PREFIX + System.currentTimeMillis() + IMAGE_SUFFIX_JPG);
        Log.i(TAG, "image path: " + image.getAbsolutePath());
        return image;
    }

    /**
     * 创建临时文件
     *
     * @return 临时文件
     */
    public File crateTempFile() {
        File tempParentDir = getTempParentFile();
        File tempFile = null;
        try {
            tempFile = File.createTempFile(FILE_PREFIX, FILE_SUFFIX, tempParentDir);
            //在程序退出时删除临时文件
            tempFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "temp file path: " + tempFile.getAbsolutePath());
        return tempFile;
    }

    /**
     * 获取临时文件保存目录
     *
     * @return file
     */
    private File getTempParentFile() {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String tempDir = storageDir.getAbsolutePath() + File.separator + TEMPS + File.separator;
        File tempParentDir = new File(tempDir);

        if (!tempParentDir.exists()) {
            tempParentDir.mkdirs();
        }
        return tempParentDir;
    }


    /**
     * des：Date格式转为字符串
     *
     * @param date   时间
     * @param format yyyy-MM-dd HH:mm:ss
     * @return 格式化后时间字符串
     */
    public static String date2Str(Date date, String format) {
        if (date == null) return null;

        if (format == null || format.length() == 0) {
            format = DEFAULT_FORMAT;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(date);
        return s;
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

    }


}