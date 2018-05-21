package com.summ.imageselector;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.summ.imageselector.adapter.ImageSelectorAdapter;
import com.summ.imageselector.adapter.SelectFolderAdapter;
import com.summ.imageselector.entity.ImageBrowseIntentData;
import com.summ.imageselector.entity.ImageEntity;
import com.summ.imageselector.entity.ImageFolderEntity;
import com.summ.imageselector.entity.ImageSelectorIntentData;
import com.summ.imageselector.listener.ImageSelectorListener;
import com.summ.imageselector.listener.SelectFolderListener;
import com.summ.imageselector.scan.ScanLocalImage;
import com.summ.imageselector.listener.ScanLocalImageListener;
import com.summ.imageselector.view.BaseImageActivity;
import com.summ.imageselector.view.GridItemDecoration;
import com.summ.imageselector.view.SelectFolderView;

import java.io.File;
import java.lang.ref.WeakReference;
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
public class ImageSelectorActivity extends BaseImageActivity implements ScanLocalImageListener, ImageSelectorListener, SelectFolderListener {

    public final static int SCAN_IMAGE_COMPLETE_CODE = 1;
    public final static int IMAGE_SELCT_CLICK_CODE = 2;
    private static final int RESULT_CODE = 1000;


    private FrameLayout flTitlebar;
    private FrameLayout flEmpty;
    private TextView tvTitle;
    private ImageView ivLefticon;
    private Button btnRighticon;
    private RecyclerView rvImages;
    private FrameLayout flBottomMenu;
    private TextView tvBrowse;
    private TextView tvFolder;

    private ScanLocalImage mScanLocalImage;
    private ScanImageHandler mScanImageHandler;

    private ImageSelectorAdapter mImageSelectorAdapter;

    private int mImageFromType;
    private int mMaxCount;
    private int mCheckedCount;
    private List<ImageEntity> mImages;
    private List<ImageEntity> mCheckedImages;

    private ImageSelectorIntentData mImageSelectorIntent;
    private SelectFolderView mSelectFolder;

    @LayoutRes
    @Override
    public int putContentView() {
        return R.layout.activity_image_selector;
    }

    @Override
    public void initView() {
        this.flTitlebar = (FrameLayout) findViewById(R.id.image_selector_fl_titlebar);
        this.flEmpty = (FrameLayout) findViewById(R.id.image_selector_fl_empty);
        this.btnRighticon = (Button) findViewById(R.id.image_selector_iv_right_icon);
        this.tvTitle = (TextView) findViewById(R.id.image_selector_tv_title);
        this.ivLefticon = (ImageView) findViewById(R.id.image_selector_iv_left_icon);
        this.rvImages = (RecyclerView) findViewById(R.id.image_selector_recycler_view_images);
        this.flBottomMenu = (FrameLayout) findViewById(R.id.image_selector_fl_bottom_menu);
        this.tvBrowse = (TextView) findViewById(R.id.image_selector_tv_browse);
        this.tvFolder = (TextView) findViewById(R.id.image_selector_tv_folder);

        //设置RecyclerView布局管理器为3列垂直排布
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvImages.setLayoutManager(layoutManager);
        rvImages.addItemDecoration(new GridItemDecoration(3, 6, false));

        initData();

        setListener();
    }

    private void setListener() {



        ivLefticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseImage(0, mCheckedImages);
            }
        });

        btnRighticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSelectorIntentData.setResultIntentData(ImageSelectorActivity.this, mCheckedImages);
            }
        });


        tvFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectFolder != null && mSelectFolder.isShowing()) {
                    mSelectFolder.dismiss();
                    mSelectFolder = null;
                } else {
                    SelectFolderAdapter adapter = new SelectFolderAdapter(mScanLocalImage.getImageFloders(), ImageSelectorActivity.this);
                    mSelectFolder = new SelectFolderView(ImageSelectorActivity.this, adapter);
                    mSelectFolder.show(flBottomMenu);
                }

            }
        });

    }

    private void browseImage(int position, List<ImageEntity> list) {
        Intent intent = new Intent(this, ImageBrowseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImageBrowseIntentData.INTENT_DATA, new ImageBrowseIntentData(position, list));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onPermissionSuccess(int permissionCode) {
        switch (permissionCode) {
            case WRITE_EXTERNAL_STORAGE:
                mScanLocalImage = new ScanLocalImage(this);
                mScanImageHandler = new ScanImageHandler(this);
                break;
            default:
        }
    }

    @Override
    public void onTipsUserPermission(int permissionCode) {
        onPermissionFailed(permissionCode);
    }

    @Override
    public void onPermissionFailed(int permissionCode) {
        switch (permissionCode) {
            case WRITE_EXTERNAL_STORAGE:
                sendHandlerMsg(SCAN_IMAGE_COMPLETE_CODE, 0);
                break;
            default:
        }
    }

    @Override
    public void onComplete(int count) {
        sendHandlerMsg(SCAN_IMAGE_COMPLETE_CODE, count);
    }


    @Override
    public void onImageSelect(ImageEntity imageEntity, CompoundButton checkBox, boolean isChecked) {

        if (mCheckedCount >= mMaxCount && isChecked) {
            Toast.makeText(this, "最多可选" + mMaxCount + "张", Toast.LENGTH_LONG).show();
            checkBox.setChecked(false);
            return;
        }

        imageEntity.setChecked(isChecked);
        if (isChecked) {
            mCheckedImages.add(imageEntity);
        } else {
            mCheckedImages.remove(imageEntity);
        }

        mCheckedCount = mCheckedImages.size();

        sendHandlerMsg(IMAGE_SELCT_CLICK_CODE, 0);


    }

    @Override
    public void onBrowseImage(int position, List<ImageEntity> list) {
        browseImage(position, list);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mImageSelectorIntent = getIntentData(ImageSelectorIntentData.INTENT_DATA, mImageSelectorIntent);
        if (mImageSelectorIntent != null) {
            mImageFromType = mImageSelectorIntent.getImageFromType();

            switch (mImageFromType) {
                case IMAGE_FROM_INTENT:
                    tvFolder.setVisibility(View.GONE);
                    break;
                case IMAGE_FROM_PHONE:
                    tvFolder.setVisibility(View.VISIBLE);
                    break;
                default:
            }
            mMaxCount = mImageSelectorIntent.getMaxCount();
            mCheckedCount = mImageSelectorIntent.getCheckedCount();
            List<ImageEntity> checkedList = mImageSelectorIntent.getCheckedImages();
            List<ImageEntity> imageData = mImageSelectorIntent.getImages();
            mImages = new ArrayList<>();
            mCheckedImages = new ArrayList<>();

            if (checkedList != null && checkedList.size() > 0) {
                mCheckedImages.addAll(checkedList);
            }

            if (imageData != null && imageData.size() > 0) {
                mImages.addAll(imageData);
            }

            mImageSelectorAdapter = new ImageSelectorAdapter(rvImages,mImages, this);
            rvImages.setAdapter(mImageSelectorAdapter);

            if (mImageFromType == IMAGE_FROM_PHONE) {
                applyPermission(WRITE_EXTERNAL_STORAGE);
            }

            sendHandlerMsg(IMAGE_SELCT_CLICK_CODE, 0);
        }


    }

    /**
     * 发送消息
     *
     * @param what 消息类型
     * @param arg1 图片总数
     */
    private void sendHandlerMsg(int what, int arg1) {
        if (mScanImageHandler == null) {
            mScanImageHandler = new ScanImageHandler(this);
        }
        Message msg = mScanImageHandler.obtainMessage();
        msg.what = what;
        msg.arg1 = arg1;
        mScanImageHandler.sendMessage(msg);
    }


    /**
     * 更新ImageSelectorAdapter
     *
     * @param msg 异步消息
     */
    private void updateView(Message msg) {
        switch (msg.what) {
            case SCAN_IMAGE_COMPLETE_CODE:
                int count = msg.arg1;
                if (count == 0) {
                    rvImages.setVisibility(View.GONE);
                    flEmpty.setVisibility(View.VISIBLE);
                } else {
                    flEmpty.setVisibility(View.GONE);
                    rvImages.setVisibility(View.VISIBLE);
                    List<File> files = mScanLocalImage.getAllImagePath();
                    addData(files);
                    mImageSelectorAdapter.notifyDataSetChanged();
                }
                break;
            case IMAGE_SELCT_CLICK_CODE:
                boolean isCheckedCount = mCheckedCount == 0;
                String rightStr = isCheckedCount == true ? "完成" : "完成(" + mCheckedCount + "/" + mMaxCount + ")";
                btnRighticon.setText(rightStr);
                String browseStr = isCheckedCount == true ? "预览" : "预览(" + mCheckedCount + ")";
                tvBrowse.setText(browseStr);
                btnRighticon.setEnabled(!isCheckedCount);
                tvBrowse.setEnabled(!isCheckedCount);
                break;
            default:
        }

    }

    private void addData(List<File> files) {
        List<ImageEntity> list = constructionItemImage(files);
        if (list.size() > 0) {
            mImages.clear();
            mImages.addAll(list);
        }
    }

    @Override
    public void onItemClick(int position, ImageFolderEntity folder) {

        List<File> imageFiles = mScanLocalImage.getFolderImageFiles(folder);

        if (imageFiles != null && imageFiles.size() > 0) {
            addData(imageFiles);
            mImageSelectorAdapter.notifyDataSetChanged();
            mSelectFolder.dismiss();
            tvFolder.setText(folder.getFloderName());
        }


    }


    static class ScanImageHandler extends Handler {
        /**
         * WeakReference to the outer class's instance.
         */
        private WeakReference<ImageSelectorActivity> mCallback;

        /**
         * 图片扫描异步处理
         *
         * @param callback 请求的回调
         */
        private ScanImageHandler(ImageSelectorActivity callback) {
            mCallback = new WeakReference(callback);
        }

        @Override
        public void handleMessage(Message msg) {
            ImageSelectorActivity callback = mCallback.get();
            if (callback != null) {
                callback.updateView(msg);
            }

        }
    }


}
