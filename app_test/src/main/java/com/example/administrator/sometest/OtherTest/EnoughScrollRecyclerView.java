package com.example.administrator.sometest.OtherTest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 只有内容足够多才能滚动
 */

public class EnoughScrollRecyclerView extends RecyclerView {
    private  final int ENABLED_SCROLL_BEHAVIOR = AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;
    private  final int DISABLED_SCROLL_BEHAVIOR = 0;
    private View bindView;


    public EnoughScrollRecyclerView(Context context) {
        super(context);
        init();
    }

    public EnoughScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EnoughScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setBindView(View view){
        bindView = view;
    }

    private void init(){
        addOnLayoutChangeListener(onLayoutChangeListener);
        post(new Runnable() {
            @Override
            public void run() {
                Log.e("yang", "getRecyHeight: " + getHeight());
            }
        });
    }

    private OnLayoutChangeListener onLayoutChangeListener = new OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            updateScrollState();
        }
    };

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        updateScrollState();

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++){
            View view = getChildAt(i);
            Log.e("yang", "gettop: " + view.getTop() + " getBottom: " + view.getBottom() + " ziji: "+getHeight());
        }
    }

    public void updateScrollState(){
        post(new Runnable() {
            @Override
            public void run() {
//                ScrollToolbarTestActivity.ILayoutManager iLayoutManager = null;
//                if(getLayoutManager() instanceof ScrollToolbarTestActivity.ILayoutManager){
//                    iLayoutManager = (ScrollToolbarTestActivity.ILayoutManager)getLayoutManager();
//                }
//                iLayoutManager.setScroll(true);
//                boolean canScroll = canScrollVertically(1) || canScrollVertically(-1);
//                iLayoutManager.setScroll(canScroll);
//
//                int scrollFlags = canScroll ? ENABLED_SCROLL_BEHAVIOR : DISABLED_SCROLL_BEHAVIOR;
//                applyScrollBehavior(bindView, scrollFlags);
            }
        });

    }

    private void applyScrollBehavior(View appBarLayout, int scrollFlags) {
        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) appBarLayout.getLayoutParams();
        layoutParams.setScrollFlags(scrollFlags);
        appBarLayout.setLayoutParams(layoutParams);
    }
}
