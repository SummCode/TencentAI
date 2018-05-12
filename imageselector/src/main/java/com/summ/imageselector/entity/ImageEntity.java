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
public class ImageEntity implements Serializable{

    private int imageId;
    private String path;
    private boolean isChecked;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public ImageEntity(String path) {
        this.path = path;
        this.isChecked = false;
    }

    public ImageEntity(int id, String path) {
        this.imageId = id;
        this.path = path;
        this.isChecked = false;
    }

    public ImageEntity(int id, String path, boolean isChecked) {
        this.imageId = id;
        this.path = path;
        this.isChecked = isChecked;
    }


    @Override
    public String toString() {
        return "ImageEntity{" +
                "imageId=" + imageId +
                ", path='" + path + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ImageEntity that = (ImageEntity) o;

        if (imageId != that.imageId) {
            return false;
        }

        if (isChecked != that.isChecked) {
            return false;
        }

        return path != null ? path.equals(that.path) : that.path == null;
    }

    @Override
    public int hashCode() {
        int result = imageId;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (isChecked ? 1 : 0);
        return result;
    }
}
