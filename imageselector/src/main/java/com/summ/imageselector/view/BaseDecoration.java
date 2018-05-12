package com.summ.imageselector.view;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Project Name：PlayPoly
 * Package Name：com.jiejue.base.adapter.decoration
 * Author：summ.Chen
 * Create：2017/7/21
 * Email： summ_summ@163.com
 * Version：1.0.0
 * <p>
 * Describe：
 */

public abstract class BaseDecoration extends RecyclerView.ItemDecoration {

    public Drawable mDivider;
    public int mDividerHeight = 1;//分割线高度，默认为1px

    public static final int[] ATTRS = new int[]{android.R.attr.listDivider};


    public int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    public boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    public boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int total = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
        int curRow = (pos + 1) % spanCount == 0 ? (pos + 1) / spanCount : (pos + 1) / spanCount + 1;
        if (layoutManager instanceof GridLayoutManager) {
            if (curRow >= total)// 如果是最后一行，则不需要绘制底部
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是最后一行，则不需要绘制底部
                if (curRow >= total)
                    return true;
            } else {// StaggeredGridLayoutManager 且横向滚动
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
