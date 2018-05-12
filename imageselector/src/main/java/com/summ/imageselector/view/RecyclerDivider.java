package com.summ.imageselector.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;



/**
 * Describe：
 * <p>
 * <p>
 * Author：summ.Chen.
 * Create：2016/7/31.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */
public class RecyclerDivider extends BaseDecoration {

    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;

    //    private Paint mPaint;
    private Drawable mDivider;
    private int mOrientation;//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
    private int mDividerHeight = 1;//分割线高度，默认为1px


    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public RecyclerDivider(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param resColor 分割线图片
     */
    public RecyclerDivider(Context context, int orientation, int resColor) {
//        this(context, orientation);
        setOrientation(orientation);
        mDivider = new ColorDrawable(context.getResources().getColor(resColor));
        mDividerHeight = mDivider.getIntrinsicHeight();
    }


    /**
     * 自定义分割线
     *
     * @param context
     * @param dividerHeight 分割线高度
     * @param resColor      分割线颜色
     */
    public RecyclerDivider(Context context, int orientation, int dividerHeight, int resColor) {
//        this(context, orientation);
        mDivider = new ColorDrawable(context.getResources().getColor(resColor));
        setOrientation(orientation);
        mDividerHeight = dividerHeight;
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setColor(resColor);
//        mPaint.setStyle(Paint.Style.FILL);
    }


    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        this.mOrientation = orientation;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }

    }


    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int itemCount = parent.getAdapter().getItemCount();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(child);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom;
            if ((position + 1) == itemCount) {
                bottom = top;
            } else {
                bottom = top + mDividerHeight;
            }

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
//            if (mPaint != null) {
//                c.drawRect(left, top, right, bottom, mPaint);
//            }
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int itemCount = parent.getAdapter().getItemCount();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(child);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right;
            if ((position + 1) == itemCount) {
                right = left;
            } else {
                right = left + mDividerHeight;
            }

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
//            if (mPaint != null) {
//                c.drawRect(left, top, right, bottom, mPaint);
//            }
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //获取layoutParams参数
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        //当前位置
        int itemPosition = layoutParams.getViewLayoutPosition();
        //ItemView数量
        int childCount = parent.getAdapter().getItemCount();
        if (mOrientation == VERTICAL) {
            childCount -= 1;
            //垂直布局底部留边，最后一行不留
            outRect.set(0, 0, 0, (itemPosition != childCount) ? mDividerHeight : 0);
        } else {
            childCount -= 1;
            //水平布局右侧留Margin,如果是最后一列,就不要留Margin了
            outRect.set(0, 0, (itemPosition != childCount) ? mDividerHeight : 0, 0);
        }
    }


}