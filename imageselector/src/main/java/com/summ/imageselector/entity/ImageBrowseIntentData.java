package com.summ.imageselector.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageBrowseIntentData implements Serializable {


    public final static String INTENT_DATA = "intent_data";

    private int mPosition;
    private List<ImageEntity> mImages;


    public ImageBrowseIntentData(List<ImageEntity> images) {
        this.mImages = images;
    }

    public ImageBrowseIntentData(int position, List<ImageEntity> images) {
        this.mPosition = position;
        this.mImages = images;
    }

    public List<ImageEntity> getImages() {
        return mImages == null ? new ArrayList<ImageEntity>() : mImages;
    }

    public void setImages(List<ImageEntity> mImages) {
        this.mImages = mImages;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    @Override
    public String toString() {
        return "ImageBrowseIntentData{" +
                "Position=" + mPosition +
                ", Images=" + mImages +
                '}';
    }
}
