package com.example.administrator.sometest.ViewGragHelperTest;

import android.content.Context;
import androidx.customview.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.administrator.sometest.R;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public class DragLayout extends RelativeLayout {
    private ViewDragHelper viewDragHelper;

    public DragLayout(Context context) {
        super(context);
        init();
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        viewDragHelper = ViewDragHelper.create(this, 1, callback);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return viewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    private void getMove(){
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.rl_rootlayout);
        viewDragHelper = ViewDragHelper.create(relativeLayout, 1, callback);
    }

    float scaleUnit = 0.1f;
    float sdcaleValue = 1f;
    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            changedView.setScaleX(sdcaleValue);
            changedView.setScaleY(sdcaleValue);
            if(sdcaleValue > 3){
                sdcaleValue -= scaleUnit;
            }else{
                sdcaleValue += scaleUnit;
            }
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }
    };
}
