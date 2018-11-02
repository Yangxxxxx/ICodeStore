package com.example.administrator.sometest.TmpActivity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class TouchTestLayout extends LinearLayout {
    public TouchTestLayout(Context context) {
        super(context);
    }

    public TouchTestLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchTestLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("yag", "onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("yag", "dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("yag", "onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }
}
