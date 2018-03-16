package com.example.administrator.sometest.OtherTest;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.administrator.sometest.R;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

public class ICooLayout extends CoordinatorLayout {
    private  final int ENABLED_SCROLL_BEHAVIOR = AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;
    private  final int DISABLED_SCROLL_BEHAVIOR = 0;
    private  final int SCROLL_DOWN = 1;

    public ICooLayout(Context context) {
        super(context);
    }

    public ICooLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(final boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
//               checkScroll();
//            }
//        }, 1000);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

//                postDelayed(new Runnable() {
//            @Override
//            public void run() {
//               checkScroll();
//            }
//        }, 1000);
    }

    private void checkScroll(){
        FrameLayout appBarLayout = (FrameLayout) findViewById(R.id.rl_title);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_friends);

//        for(View view: allView){
//            if(view instanceof AppBarLayout) appBarLayout = (AppBarLayout)view;
//            if(view instanceof  RecyclerView) recyclerView = (RecyclerView)view;
//        }

        if(appBarLayout != null && recyclerView != null){
            applyScrollBehavior(appBarLayout, DISABLED_SCROLL_BEHAVIOR);
            if (recyclerView.canScrollVertically(SCROLL_DOWN)) {
                applyScrollBehavior(appBarLayout, ENABLED_SCROLL_BEHAVIOR);
            }
        }
    }

//    private List<View> allView = new ArrayList<>();
//    private void getAllChild(){
//        int childCount = getChildCount();
//
//        for(int i = 0; i < childCount; i++){
//            View view = getChildAt(i);
//            if(view instanceof ViewGroup){
//                ViewGroup viewGroup = (ViewGroup)view;
//                if(viewGroup.getChildCount() > 0) {
//                    getAllChild();
//                }
//            }
//            allView.add(view);
//        }
//    }

    private void applyScrollBehavior(FrameLayout appBarLayout, int scrollFlags) {
        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) appBarLayout.getLayoutParams();
        layoutParams.setScrollFlags(scrollFlags);
        appBarLayout.setLayoutParams(layoutParams);
    }
}
