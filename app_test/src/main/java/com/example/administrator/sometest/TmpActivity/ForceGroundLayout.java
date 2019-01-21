package com.example.administrator.sometest.TmpActivity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ForceGroundLayout extends RelativeLayout {
    public ForceGroundLayout(Context context) {
        this(context, null);
    }

    public ForceGroundLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ForceGroundLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0x88000000);
    }
}
