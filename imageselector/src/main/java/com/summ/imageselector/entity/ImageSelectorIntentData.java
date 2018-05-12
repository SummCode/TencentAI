package com.summ.imageselector.entity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.summ.imageselector.ImageSelectorActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageSelectorIntentData implements Serializable {


    public final static String INTENT_DATA = "intent_data";

    public final static int IMAGE_FROM_PHONE = 0;
    public final static int IMAGE_FROM_INTENT = 1;
    public static final int IMAGE_SELECT_REQUEST_CODE = 100;


    private int mMaxCount;
    private int mCheckedCount;
    private int mImageFromType;
    private List<ImageEntity> mImages;
    private List<ImageEntity> mCheckedImages;

    public ImageSelectorIntentData(List<ImageEntity> mCheckedImages) {
        this.mCheckedImages = mCheckedImages;
    }

    public ImageSelectorIntentData(int maxCount, int checkedCount, List<ImageEntity> checkedImages) {
        this.mMaxCount = maxCount;
        this.mCheckedCount = checkedCount;
        this.mImageFromType = IMAGE_FROM_PHONE;
        this.mImages = null;
        this.mCheckedImages = checkedImages;
    }


    public ImageSelectorIntentData(int maxCount, int checkedCount, List<ImageEntity> images, List<ImageEntity> checkedImages) {
        this.mMaxCount = maxCount;
        this.mCheckedCount = checkedCount;
        this.mImageFromType = IMAGE_FROM_INTENT;
        this.mImages = images;
        this.mCheckedImages = checkedImages;
    }

    public int getMaxCount() {
        return mMaxCount;
    }

    public void setMaxCount(int mMaxCount) {
        this.mMaxCount = mMaxCount;
    }

    public int getCheckedCount() {
        return mCheckedCount;
    }

    public void setCheckedCount(int mCheckedCount) {
        this.mCheckedCount = mCheckedCount;
    }

    public int getImageFromType() {
        return mImageFromType;
    }

    public void setImageFromType(int mImageFromType) {
        this.mImageFromType = mImageFromType;
    }

    public List<ImageEntity> getImages() {
        return mImages == null ? new ArrayList<ImageEntity>() : mImages;
    }

    public void setImages(List<ImageEntity> mImages) {
        this.mImages = mImages;
    }

    public List<ImageEntity> getCheckedImages() {
        return mCheckedImages == null ? new ArrayList<ImageEntity>() : mCheckedImages;
    }

    public void setCheckedImages(List<ImageEntity> mCheckedImages) {
        this.mCheckedImages = mCheckedImages;
    }

    public static void setResultIntentData(Activity activity,List<ImageEntity> images){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        ImageSelectorIntentData data = new ImageSelectorIntentData(images);
        bundle.putSerializable(INTENT_DATA, data);
        intent.putExtras(bundle);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public static void setIntentData(Activity activity, ImageSelectorIntentData intentData) {
        Intent intent = new Intent(activity, ImageSelectorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(INTENT_DATA, intentData);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, IMAGE_SELECT_REQUEST_CODE);
    }

    public static ImageSelectorIntentData getIntentData(Intent intent) {
        return (ImageSelectorIntentData) intent.getExtras().getSerializable(INTENT_DATA);
    }

    @Override
    public String toString() {
        return "ImageSelectorIntentData{" +
                "mMaxCount=" + mMaxCount +
                ", mCheckedCount=" + mCheckedCount +
                ", mImageFromType=" + mImageFromType +
                ", mImages=" + mImages +
                ", mCheckedImages=" + mCheckedImages +
                '}';
    }
}
