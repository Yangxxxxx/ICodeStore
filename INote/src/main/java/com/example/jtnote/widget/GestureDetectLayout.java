package com.example.jtnote.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.jtnote.utils.CommonUtils;

public class GestureDetectLayout extends RelativeLayout implements GestureDetector.OnGestureListener {
    private final static int BODER_WIDTH_DP = 23;

    private float boderWidth;
    private boolean pressOnBorder;
    private GestureDetector gestureDetector;
    private OnGestureListener onGestureListener;

    public GestureDetectLayout(Context context) {
        this(context, null);
    }

    public GestureDetectLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureDetectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }


    public void setOnGestureListener(OnGestureListener onGestureListener) {
        this.onGestureListener = onGestureListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pressOnBorder = (event.getX() >= getRight() - boderWidth) || (event.getX() <= boderWidth);
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(onGestureListener != null) {
            onGestureListener.onClick(this);
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if(onGestureListener != null) {
            onGestureListener.onLongClick(this, pressOnBorder);
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private void init(){
        boderWidth = CommonUtils.dp2Px(getContext(), BODER_WIDTH_DP);
        gestureDetector = new GestureDetector(getContext(), this);
    }


    public interface OnGestureListener{
        void onClick(View v);
        void onLongClick(View v, boolean pressOnBorder);
    }
}
