package com.example.administrator.sometest.TouchEventTest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class NoNameTextView extends TextView {
    public NoNameTextView(Context context) {
        super(context);
    }

    public NoNameTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoNameTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("noname", "event: " + event.getAction() + " " +event.getX() + " " + event.getY());
        return true;
    }
}
