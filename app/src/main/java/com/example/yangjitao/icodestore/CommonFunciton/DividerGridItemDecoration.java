package com.example.yangjitao.icodestore.CommonFunciton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

@Deprecated
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;

    private int offTop = 0;
    private int offBottom = 0;


    /**
     * gridview的分割线宽度
     */
    int interval;

    public DividerGridItemDecoration(Context context) {
        Log.e("yang", "brance merge test");
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();

//        interval = context.getResources().getDimensionPixelSize(R.dimen.new_collage_grid_divider_distance);
        interval = 20;//TypedValue.complexToDimensionPixelSize(5, context.getResources().getDisplayMetrics());
    }

    public DividerGridItemDecoration(Context context, int offTop, int offBottom) {
        this(context);
        this.offBottom = offBottom;
        this.offTop = offTop;
    }

    public DividerGridItemDecoration(Context context, int offTop, int offBottom, int interval) {
        this(context);
        this.offBottom = offBottom;
        this.offTop = offTop;

        this.interval = interval;
    }

//    @Override
//    public void onDraw(Canvas c, RecyclerView parent, State state)
//    {
//
//        drawHorizontal(c, parent);
//        drawVertical(c, parent);
//
//    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                int childCount) {
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
            {
                Log.e("yang", "pos: " + pos);
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
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

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int remainder = childCount % spanCount;

            /** 最后一行的起始位置*/
            int lastRawFirstPos;

            if (remainder == 0) {
                lastRawFirstPos = childCount - spanCount;
            } else {
                lastRawFirstPos = childCount - remainder;
            }

            if (pos >= lastRawFirstPos)// 如果是最后一行，则不需要绘制底部
                return true;
        }
        return false;
    }


    @Override
    public void getItemOffsets(Rect outRect, int itemPosition,
                               RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();

        int left = 0;
        int top = 0;
        int right = interval;
        int bottom = interval;

        if (isFirstRaw(itemPosition, spanCount)) {
            top = offTop;
        }

        if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
        {
//            outRect.set(0, top, interval, offBottom);
            bottom = offBottom;

        }

        if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
        {
//            outRect.set(0, top, 0, interval);
            right = 0;
        }

        outRect.set(left, top, right, bottom);
    }


//    @Override
//    public void getItemOffsets(Rect outRect, int itemPosition,
//                               RecyclerView parent)
//    {
//        int spanCount = getSpanCount(parent);
//        int childCount = parent.getAdapter().getItemCount();
//
//        int top = 0;
//        if(isFirstRaw(itemPosition, spanCount)){
//            top = offTop;
//        }
//
//        if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
//        {
//            outRect.set(0, top, interval, offBottom);
//
//        } else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
//        {
//            outRect.set(0, top, 0, interval);
//        } else
//        {
//            outRect.set(0, top, interval,
//                    interval);
//        }
//    }

    private boolean isFirstRaw(int itemPosition, int spanCount) {
        return itemPosition < spanCount;
    }
}