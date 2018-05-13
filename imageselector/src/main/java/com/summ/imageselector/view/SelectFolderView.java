package com.summ.imageselector.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.summ.imageselector.R;
import com.summ.imageselector.adapter.SelectFolderAdapter;
import com.summ.imageselector.entity.ImageFolderEntity;

import java.util.List;

public class SelectFolderView extends PopupWindow {

    public final static int MATCH_PARENT = WindowManager.LayoutParams.MATCH_PARENT;
    public final static int WRAP_CONTENT = WindowManager.LayoutParams.WRAP_CONTENT;

    private Activity mContext;
    private View mRootLayout;
    private RecyclerView rvFolder;
    private SelectFolderAdapter mAdapter;

    public SelectFolderView(Activity context, SelectFolderAdapter adapter) {
        super(context);
        this.mContext = context;
        this.mAdapter = adapter;
        initContentView(MATCH_PARENT, WRAP_CONTENT);
    }

    /**
     * 加载popup window布局
     *
     * @return layout res id
     */
    public int putContentView() {
        return R.layout.select_folder_view_layout;
    }


    /**
     * 设置popup window样式
     */
    public void setPopupWindowStyle() {

    }

    /**
     * 初始化View
     *
     * @param view 布局
     */
    public void initView(View view) {
        rvFolder = view.findViewById(R.id.recycler_view_selector_folder);
        View bg = view.findViewById(R.id.view_selector_folder_top_bg);
        setViewSize(bg, MATCH_PARENT, getScreenHeight(mContext) - dp2px(100) - getStatusHeight(mContext));
        LinearLayoutManager linear = new LinearLayoutManager(mContext);
        rvFolder.setLayoutManager(linear);
        rvFolder.addItemDecoration(new RecyclerDivider(view.getContext(), RecyclerDivider.VERTICAL, 1, R.color.fontNormalColor));

        rvFolder.setAdapter(mAdapter);

        setViewSize(rvFolder, MATCH_PARENT, getScreenHeight(mContext) / 3 * 2);

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    /**
     * 初始Popup Window
     */
    public void initContentView(int width, int height) {
        setWidth(width);
        setHeight(height);
//        backgroudAlpha(0.2f);
        setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.diaphaneityColor))); // 设置背景颜色
        setTouchable(true);
        setFocusable(true);// 设置可以获取焦点
        setOutsideTouchable(true);// 设置可以触摸弹出框以外的区域
        setPopupWindowStyle();
        mRootLayout = loadContentView(putContentView());
        initView(mRootLayout);
        setContentView(mRootLayout);


//        setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                backgroudAlpha(1f);
//            }
//        });
    }

    //设置屏幕背景透明度
    private void backgroudAlpha(float alpha) {
        WindowManager.LayoutParams params = mContext.getWindow().getAttributes();
        params.alpha = alpha;
        mContext.getWindow().setAttributes(params);
    }


    /**
     * 加载popup window布局
     *
     * @param layoutResId 布局资源
     * @return View
     */
    public View loadContentView(int layoutResId) {
        return LayoutInflater.from(mContext).inflate(layoutResId, null);
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

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return 屏幕高度
     */
    public int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 动态设置View的大小
     *
     * @param view   view
     * @param width  宽度
     * @param height 高度
     */
    private void setViewSize(View view, int width, int height) {
        ViewGroup.LayoutParams linearParams = view.getLayoutParams(); //取控件View当前的布局参数
        linearParams.width = width;// 控件的宽强制设成 width
        linearParams.height = height;// 控件的高强制设成 height
        view.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
    }


    public int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, mContext.getResources().getDisplayMetrics());
    }

    public void show(View view) {
        this.showAsDropDown(view, 0, 0);
    }


}
