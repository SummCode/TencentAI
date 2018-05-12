package com.summ.imageselector.entity;

import java.io.Serializable;

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
public class ImageFolderEntity implements Serializable {


    private String parentDir;
    private String firstImagePath;
    private String floderName;
    private int imageCount;
    private boolean isChecked = false;


    public String getParentDir() {
        return parentDir;
    }

    public void setParentDir(String parentDir) {
        this.parentDir = parentDir;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getFloderName() {
        return floderName;
    }

    public void setFloderName(String floderName) {
        this.floderName = floderName;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "ImageFolderEntity{" +
                "parentDir='" + parentDir + '\'' +
                ", firstImagePath='" + firstImagePath + '\'' +
                ", floderName='" + floderName + '\'' +
                ", imageCount=" + imageCount +
                ", isChecked=" + isChecked +
                '}';
    }
}
