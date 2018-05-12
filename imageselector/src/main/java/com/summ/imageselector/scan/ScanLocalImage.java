package com.summ.imageselector.scan;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.summ.imageselector.entity.ImageFolderEntity;
import com.summ.imageselector.listener.ScanLocalImageListener;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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
public class ScanLocalImage {


    private int mTotalCount = 0;
    private HashSet<String> mDirPaths = new HashSet<>();
    private List<ImageFolderEntity> mImageFloders = new ArrayList<>();
    private ScanLocalImageListener mListener;

    public List<ImageFolderEntity> getImageFloders() {
        return mImageFloders;
    }

    public void setImageFloders(List<ImageFolderEntity> imageFloders) {
        this.mImageFloders = imageFloders;
    }

    public ScanLocalImageListener getListener() {
        return mListener;
    }

    public void setListener(ScanLocalImageListener listener) {
        this.mListener = listener;
    }

    public ScanLocalImage(Context context) {
        if (context instanceof ScanLocalImageListener) {
            this.mListener = (ScanLocalImageListener) context;
        }
        this.onScanImage(context);
    }

    public ScanLocalImage(Context context, ScanLocalImageListener listener) {
        this.mListener = listener;
        this.onScanImage(context);
    }


    public void onScanImage(final Context context) {
        if (!isEmpty(mImageFloders)) {
            mImageFloders.clear();
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = context.getContentResolver();

                Cursor cursor = contentResolver.query(imageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED + " desc");

                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    File file = new File(path);
                    if (file.length() == 0 && file.exists()) {
                        file.delete();
                        continue;
                    }
                    File parentFile = file.getParentFile();
                    if (parentFile == null) {
                        continue;
                    }

                    String dirPath = parentFile.getAbsolutePath();
                    ImageFolderEntity imageFloder;
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        imageFloder = new ImageFolderEntity();
                        imageFloder.setParentDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }
                    File[] tempFiles = getFilterImage(parentFile);
                    if (tempFiles == null) {
                        continue;
                    }
                    int picSize = tempFiles.length;
                    mTotalCount += picSize;

                    imageFloder.setImageCount(picSize);
                    imageFloder.setFloderName(parentFile.getName());
                    mImageFloders.add(imageFloder);
                }
                cursor.close();
                mDirPaths.clear();
                mDirPaths = null;

                addDefaultImageFloder();

                if (mListener != null) {
                    mListener.onComplete(mTotalCount);
                }

            }
        };

        ScheduledExecutorService scanImage = new ScheduledThreadPoolExecutor(3);
        scanImage.execute(runnable);
    }


    private void addDefaultImageFloder() {

        ImageFolderEntity tempImageFolder = null;

        if (!isEmpty(mImageFloders)) {
            tempImageFolder = mImageFloders.get(0);
        }

        if (tempImageFolder != null) {
            ImageFolderEntity imageFolder = new ImageFolderEntity();
            imageFolder.setParentDir(tempImageFolder.getParentDir());
            imageFolder.setFloderName("所有图片");
            imageFolder.setFirstImagePath(tempImageFolder.getFirstImagePath());
            imageFolder.setImageCount(mTotalCount);
            imageFolder.setChecked(true);

            mImageFloders.add(0, imageFolder);
        }

    }


    private File[] getFilterImage(File parentFile) {
        return parentFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                filename = filename.toLowerCase();
                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) {
                    return true;
                }
                return false;
            }
        });
    }


    public List<File> getAllImagePath() {
        List<ImageFolderEntity> dirPaths = new ArrayList<>();
        dirPaths.addAll(getImageFloders());
        dirPaths.remove(0);
        if (isEmpty(dirPaths)) {
            return null;
        }

        List<File> allImagePath = new ArrayList<>();

        for (ImageFolderEntity floder : dirPaths) {

            List<File> temp = getFolderImageFiles(floder);

            if (isEmpty(temp)) {
                continue;
            }

            allImagePath.addAll(temp);
        }

        return allImagePath;
    }


    public List<File> getAllImagePath(List<ImageFolderEntity> dirPaths) {
        if (isEmpty(dirPaths)) {
            return null;
        }

        List<File> allImagePath = new ArrayList<>();

        for (ImageFolderEntity floder : dirPaths) {

            List<File> temp = getFolderImageFiles(floder);

            if (isEmpty(temp)) {
                continue;
            }

            allImagePath.addAll(temp);
        }

        return allImagePath;
    }


    public List<File> getFolderImageFiles(ImageFolderEntity floder) {
        String parent = floder.getParentDir();

        File file = new File(parent);

        File[] files = getFilterImage(file);

        if (files == null) {
            return null;
        }

        return Arrays.asList(files);
    }

    /**
     * 判断List集合是否为空
     *
     * @param list 要检查的对象
     * @return true: null or 对象大小为0
     */
    public boolean isEmpty(List list) {
        if (list != null && list.size() > 0) {
            return false;
        }
        return true;
    }


}
