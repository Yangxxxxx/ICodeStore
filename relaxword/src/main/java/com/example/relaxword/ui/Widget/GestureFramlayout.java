package com.example.relaxword.ui.Widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class GestureFramlayout extends FrameLayout {
    float touchDownX;
    float touchDownY;
    boolean enableGesture = true;

    private View consumeView;
    private View aboveView;
    private View bottomView;

    private ScrollHorizontalListener scrollHorizontalListener;

    public GestureFramlayout(@NonNull Context context) {
        super(context);
    }

    public GestureFramlayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureFramlayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollHorizontalListener(ScrollHorizontalListener scrollHorizontalListener) {
        this.scrollHorizontalListener = scrollHorizontalListener;
    }

    public void setGestureDetectEnable(boolean enable){
        enableGesture = enable;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        aboveView = getChildAt(1);
        bottomView = getChildAt(0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!enableGesture){
            consumeView = null;
            return super.dispatchTouchEvent(ev);
        }

        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            touchDownX = ev.getX();
            touchDownY = ev.getY();
            aboveView.dispatchTouchEvent(ev);
            bottomView.dispatchTouchEvent(ev);
        }else if(ev.getAction() == MotionEvent.ACTION_MOVE) {
            if(consumeView != null){
                consumeView.dispatchTouchEvent(ev);
            }else {
                float offsetX = Math.abs(ev.getX() - touchDownX);
                float offsety = Math.abs(ev.getY() - touchDownY);
                if(offsetX > offsety){
                    consumeView = aboveView;
                    if(scrollHorizontalListener != null) {
                        scrollHorizontalListener.onScrollHorizontalDetected();
                    }
                }else if(offsetX < offsety) {
                    consumeView = bottomView;
                }
            }
        }else if(ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL){
            aboveView.dispatchTouchEvent(ev);
            bottomView.dispatchTouchEvent(ev);
            consumeView = null;
        }
        return true;
    }

    public interface ScrollHorizontalListener{
        void onScrollHorizontalDetected();
    }
}
