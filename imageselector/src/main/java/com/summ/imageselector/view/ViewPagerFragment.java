package com.summ.imageselector.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.summ.imageselector.R;
import com.summ.imageselector.entity.ImageEntity;

import java.io.File;

public class ViewPagerFragment extends Fragment {

    private static final String BUNDLE_IMAGE_PATH = "image_path";

    private ImageEntity imageEntity;

    public ViewPagerFragment() {
    }

    public void setImageEntity(ImageEntity imageEntity) {
        this.imageEntity = imageEntity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_browse_item_layout, container, false);
        final SubsamplingScaleImageView imageView = rootView.findViewById(R.id.image_browse_item_picture);
        imageView.setZoomEnabled(true);
        imageView.setMaxScale(8f);
        imageView.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
        imageView.setDoubleTapZoomScale(8f);

        if (savedInstanceState != null) {
            if (imageEntity == null && savedInstanceState.containsKey(BUNDLE_IMAGE_PATH)) {
                imageEntity = (ImageEntity) savedInstanceState.getSerializable(BUNDLE_IMAGE_PATH);
            }
        }

        if (imageEntity != null) {
            String path = imageEntity.getPath();
            if (path.startsWith("http://") || path.startsWith("https://")) {
                Glide.with(getActivity()).asFile().load(path).into(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        imageView.setImage(ImageSource.uri(resource.getAbsolutePath()));
                    }
                });
            } else {
                imageView.setImage(ImageSource.uri(path));
            }

        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        View rootView = getView();
        if (rootView != null) {
            outState.putSerializable(BUNDLE_IMAGE_PATH, imageEntity);
        }
    }

}
