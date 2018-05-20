package com.summ.imageselector.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.summ.imageselector.R;
import com.summ.imageselector.entity.ImageEntity;
import com.summ.imageselector.listener.ImageSelectorListener;

import java.util.List;


/**
 * @author：Summ
 * @date：2018/3/30
 * @email： summ_summ@163.com
 * version：1.0.0
 * <p>
 * describe：
 * <p>
 * <p>
 */
public class ImageSelectorAdapter extends RecyclerView.Adapter<ImageSelectorAdapter.ImageSelectorHolder> {

    private List<ImageEntity> mImageEntities;
    private ImageSelectorListener mImageSelectListener;
    private RecyclerView rvRecycler;
    private boolean isScroll;

    public ImageSelectorAdapter(RecyclerView recyclerView, List<ImageEntity> list) {
        this(recyclerView, list, null);
    }

    public ImageSelectorAdapter(RecyclerView recyclerView, List<ImageEntity> entities, ImageSelectorListener listener) {
        this.rvRecycler = recyclerView;
        this.mImageEntities = entities;
        this.mImageSelectListener = listener;
        init();
    }

    private void init() {
        rvRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case 0:
                        isScroll = false;
                        break;
                    default:
                        isScroll = true;
                }
            }
        });
    }

    public ImageSelectorListener getImageSelectListener() {
        return mImageSelectListener;
    }

    public void setImageSelectListener(ImageSelectorListener mImageSelectListener) {
        this.mImageSelectListener = mImageSelectListener;
    }

    @Override
    public ImageSelectorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_selector_item_layout, parent, false);
        ImageSelectorHolder imageSelectorHolder = new ImageSelectorHolder(view);
        return imageSelectorHolder;
    }

    @Override
    public void onBindViewHolder(ImageSelectorHolder holder, int position) {
        ImageEntity imageEntity = mImageEntities.get(position);

        holder.setChecked(imageEntity.isChecked());
        holder.loadImage(imageEntity.getPath());
        holder.setClickListener(position);
        holder.setCheckedChangeListener(imageEntity, position);
    }

    @Override
    public int getItemCount() {
        return mImageEntities == null ? 0 : mImageEntities.size();
    }


    class ImageSelectorHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private CheckBox checkBox;

        public ImageSelectorHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_selector_iv_image);
            checkBox = itemView.findViewById(R.id.image_selector_cb_check);
            setSize(imageView);
        }

        public void setChecked(boolean isChecked) {
            checkBox.setChecked(isChecked);
        }


        public void loadImage(String imagePath) {

            String tag = (String) imageView.getTag(R.string.image_view_tag);

            if (!imagePath.equals(tag)) {
                Glide.with(imageView.getContext())
                        .load(imagePath)
                        .apply(RequestOptions.centerCropTransform())
                        .into(imageView);
                imageView.setTag(R.string.image_view_tag, imagePath);
            }

        }

        public void setCheckedChangeListener(final ImageEntity imageEntity, final int position) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mImageSelectListener != null && !isScroll) {
                        mImageSelectListener.onImageSelect(imageEntity, buttonView, isChecked);
                    }
                }
            });
        }

        public void setClickListener(final int position) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mImageSelectListener != null) {
                        mImageSelectListener.onBrowseImage(position, mImageEntities);
                    }
                }
            });
        }

        /**
         * 根据屏幕设定图片大小
         */
        public void setSize(ImageView imageView) {
            int width = (getScreenWidth(imageView.getContext()) - 2 * 6) / 3;
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width, width);
            imageView.setLayoutParams(params);
        }


        /**
         * 获得屏幕宽度
         *
         * @param context 上下文
         * @return 屏幕宽度
         */
        public int getScreenWidth(Context context) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            return outMetrics.widthPixels;
        }
    }


}
