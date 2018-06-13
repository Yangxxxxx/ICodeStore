package com.example.jtnote.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2018/6/14 0014.
 */

public class InsetableRelativeLayout extends RelativeLayout{
    private OnSystemWindowsChangeListener onSystemWindowsChangeListener;


    public InsetableRelativeLayout(Context context) {
        super(context);
    }

    public InsetableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InsetableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnSystemWindowsChangeListener(OnSystemWindowsChangeListener onSystemWindowsChangeListener){
        this.onSystemWindowsChangeListener = onSystemWindowsChangeListener;
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if(onSystemWindowsChangeListener != null){
            onSystemWindowsChangeListener.fitSystemWindows(insets);
        }
        return true;
    }

    public interface OnSystemWindowsChangeListener{
        void fitSystemWindows(Rect insets);
    }
}
