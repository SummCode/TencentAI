package com.summ.imageselector.listener;

import android.widget.CompoundButton;

import com.summ.imageselector.entity.ImageEntity;

import java.util.List;

public interface ImageSelectorListener {


    void onImageSelect(ImageEntity imageEntity, CompoundButton buttonView, boolean isChecked);

    void onBrowseImage(int position, List<ImageEntity> list);


}
