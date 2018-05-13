package com.summ.imageselector.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.summ.imageselector.R;
import com.summ.imageselector.entity.ImageFolderEntity;
import com.summ.imageselector.listener.ImageSelectorListener;
import com.summ.imageselector.listener.SelectFolderListener;
import com.summ.imageselector.view.SelectFolderView;

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
public class SelectFolderAdapter extends RecyclerView.Adapter<SelectFolderAdapter.ImageSelectorHolder> {

    private List<ImageFolderEntity> mImageFolders;
    private SelectFolderListener mFolderListener;

    private ImageFolderEntity mCurrentItem;

    public SelectFolderAdapter(List<ImageFolderEntity> folders, SelectFolderListener listener) {
        this.mImageFolders = folders;
        this.mFolderListener = listener;
    }


    @Override
    public ImageSelectorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_folder_item_layout, parent, false);
        ImageSelectorHolder imageSelectorHolder = new ImageSelectorHolder(view);
        return imageSelectorHolder;
    }

    @Override
    public void onBindViewHolder(ImageSelectorHolder holder, int position) {
        ImageFolderEntity imageFolder = mImageFolders.get(position);
        if (mCurrentItem == null && imageFolder.isChecked()) {
            mCurrentItem = imageFolder;
        } else {
            imageFolder.setChecked(false);
        }
        holder.loadImage(imageFolder.getFirstImagePath());
        holder.setTitle(imageFolder.getFloderName());
        holder.setCount(imageFolder.getImageCount());
        holder.setChecked(imageFolder.isChecked());
        holder.setListener(position, imageFolder);
    }

    @Override
    public int getItemCount() {
        return mImageFolders == null ? 0 : mImageFolders.size();
    }


    class ImageSelectorHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout layout;
        private ImageView imageView;
        private TextView title;
        private TextView count;
        private RadioButton radio;

        public ImageSelectorHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.select_folder_cl_item);
            imageView = itemView.findViewById(R.id.select_folder_iv_image);
            title = itemView.findViewById(R.id.select_folder_tv_title);
            count = itemView.findViewById(R.id.select_folder_tv_count);
            radio = itemView.findViewById(R.id.select_folder_rb_checked);
        }

        public void setListener(final int position, final ImageFolderEntity folder) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    folder.setChecked(true);
                    if (mCurrentItem != null) {
                        mCurrentItem.setChecked(false);
                    }
                    if (mFolderListener != null) {
                        mFolderListener.onItemClick(position, folder);
                    }
                    mCurrentItem = folder;
                }
            });
        }

        public void setChecked(boolean isChecked) {
            radio.setChecked(isChecked);
            if (isChecked) {
                radio.setVisibility(View.VISIBLE);
            } else {
                radio.setVisibility(View.GONE);
            }
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

        public void setTitle(String name) {
            title.setText(name);
        }

        public void setCount(int number) {
            count.setText("" + number);
        }


        /**
         * 根据屏幕设定图片大小
         */
        public void setSize(ImageView imageView) {
            int width = (getScreenWidth(imageView.getContext()) - 2 * 6) / 4;
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
