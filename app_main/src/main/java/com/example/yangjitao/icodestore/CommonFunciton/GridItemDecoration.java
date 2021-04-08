package com.example.yangjitao.icodestore.CommonFunciton;

import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

/** recyclerview分割线（井字形状的分割线）*/
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int offTop = 0;
    private int offBottom = 0;
    private int offLeft = 0;
    private int offRight = 0;
    int interval = 20;

    public int getOffTop(){
        return offTop;
    }

    public GridItemDecoration() {
    }

    /** 可以设置分割线的宽度*/
    public GridItemDecoration(int interval) {
        this.interval = interval;
    }

    /** 可以设置分割线的宽度，及recyclerview的padding值*/
    public GridItemDecoration(int offLeft, int offTop, int offRight, int offBottom, int interval) {
        this.offBottom = offBottom;
        this.offTop = offTop;
        this.offLeft = offLeft;
        this.offRight = offRight;
        this.interval = interval;
    }


    @Override
    public void getItemOffsets(Rect outRect, int itemPosition,
                               RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();

        int left = interval / 2;
        int top = interval / 2;
        int right = interval / 2;
        int bottom = interval / 2;

        if (isFirstRaw(itemPosition, spanCount)) {
            top = offTop;
        }
        if (isFirstColumn(itemPosition, spanCount)) {
            left = offLeft;
        }
        if (isLastRaw(itemPosition, spanCount, childCount)) {
            bottom = offBottom;
        }
        if (isLastColum(itemPosition, spanCount)) {
            right = offRight;
        }
        outRect.set(left, top, right, bottom);
    }

    private int getSpanCount(RecyclerView parent) {
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return  ((GridLayoutManager) layoutManager).getSpanCount();
        }else if(layoutManager instanceof LinearLayoutManager){
            return 1;
        }
        return 0;
    }


    private boolean isLastColum(int pos, int spanCount) {
        return (pos + 1) % spanCount == 0;
    }

    private boolean isLastRaw(int pos, int spanCount, int childCount) {
        int remainder = childCount % spanCount;

        /** 最后一行的起始位置*/
        int lastRawFirstPos;
        if (remainder == 0) {
            lastRawFirstPos = childCount - spanCount;
        } else {
            lastRawFirstPos = childCount - remainder;
        }
        return (pos >= lastRawFirstPos);
    }


    private boolean isFirstRaw(int itemPosition, int spanCount) {
        return itemPosition < spanCount;
    }

    private boolean isFirstColumn(int itemPosition, int spanCount) {
        return itemPosition % spanCount == 0;
    }
}