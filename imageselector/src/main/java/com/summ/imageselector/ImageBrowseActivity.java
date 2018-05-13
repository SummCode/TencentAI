package com.summ.imageselector;

import android.support.v4.view.ViewPager;

import com.summ.imageselector.adapter.ScreenSlidePagerAdapter;
import com.summ.imageselector.entity.ImageBrowseIntentData;
import com.summ.imageselector.entity.ImageEntity;
import com.summ.imageselector.view.BaseImageActivity;

import java.util.ArrayList;
import java.util.List;

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
public class ImageBrowseActivity extends BaseImageActivity {

    private ViewPager vpPager;
    private List<ImageEntity> mImages = new ArrayList<>();

    private ScreenSlidePagerAdapter mAdapter;
    private ImageBrowseIntentData mImageBrowseIntent;

    @Override
    public int putContentView() {
        return R.layout.activity_image_browse;
    }

    @Override
    public void initView() {
        this.vpPager = (ViewPager) findViewById(R.id.activity_images_browse_pager);
        init();
    }

    private void init() {

        mImageBrowseIntent = getIntentData(ImageBrowseIntentData.INTENT_DATA, mImageBrowseIntent);
        if (mImageBrowseIntent != null) {
            List<ImageEntity> imageData = mImageBrowseIntent.getImages();
            if (imageData != null && imageData.size() > 0) {
                mImages.addAll(imageData);
            }
            mAdapter = new ScreenSlidePagerAdapter(mImages, getSupportFragmentManager());
            vpPager.setAdapter(mAdapter);
            vpPager.setCurrentItem(mImageBrowseIntent.getPosition());
        }



    }


}
