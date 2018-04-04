package com.example.administrator.sometest.TouchEventTest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2018/4/4 0004.
 */

public class NonameLayout extends RelativeLayout{
    private Paint paint;

    public NonameLayout(Context context) {
        this(context, null);
    }

    public NonameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NonameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        setClickable(true);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("yang", "onInterceptTouchEvent: " + this.hashCode());
        boolean ret = super.onInterceptTouchEvent(ev);
//        Log.e("yang", "onInterceptTouchEvent: " + this.hashCode() + ret);
        return ret;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("yang", "dispatchTouchEvent: " + this.hashCode());
        boolean ret = super.dispatchTouchEvent(ev);
//        Log.e("yang", "dispatchTouchEvent: " + this.hashCode() + ret);
        return ret;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("yang", "onTouchEvent: " + this.hashCode());
        boolean ret = super.onTouchEvent(event);
//        Log.e("yang", "onTouchEvent: " + this.hashCode() + ret);
        return ret;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(this.hashCode()+"", 50, 50, paint);
    }
}
