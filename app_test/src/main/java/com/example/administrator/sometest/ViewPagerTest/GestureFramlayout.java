package com.example.administrator.sometest.ViewPagerTest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.administrator.sometest.R;

public class GestureFramlayout extends FrameLayout {
    private View aboveView;
    private View bottomView;

    public GestureFramlayout(@NonNull Context context) {
        super(context);
    }

    public GestureFramlayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureFramlayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        aboveView = findViewById(R.id.vp01);
        bottomView = findViewById(R.id.view02);
    }

    float touchDownX;
    float touchDownY;

    View consumeView;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("yang", "x y: " + ev.getX() + " " + ev.getY());
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
                }else if(offsetX < offsety) {
                    consumeView = bottomView;
                }
            }
        }else if(ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL){
            aboveView.dispatchTouchEvent(ev);
            bottomView.dispatchTouchEvent(ev);
            consumeView = null;
        }

//        super.dispatchTouchEvent(ev);
//        aboveView.dispatchTouchEvent(ev);
        return true;
    }
}
