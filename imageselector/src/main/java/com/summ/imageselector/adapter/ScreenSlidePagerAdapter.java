package com.summ.imageselector.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.summ.imageselector.entity.ImageEntity;
import com.summ.imageselector.view.ViewPagerFragment;

import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<ImageEntity> mImages;

    public ScreenSlidePagerAdapter(List<ImageEntity> list, FragmentManager fm) {
        super(fm);
        this.mImages = list;
    }

    @Override
    public Fragment getItem(int position) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setImageEntity(mImages.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return mImages == null ? 0 : mImages.size();
    }
}
