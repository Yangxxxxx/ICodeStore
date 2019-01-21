package com.example.administrator.sometest.TmpActivity.RoundRect;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RoundFrameLayout extends FrameLayout {
   private RoundViewHelper roundViewHelper;

    public RoundFrameLayout(Context context) {
        this(context, null);
    }

    public RoundFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        roundViewHelper = new RoundViewHelper(context, attrs);
        setWillNotDraw(false);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        roundViewHelper.onMeasure(getMeasuredWidth(), getMeasuredHeight());
    }


    @Override
    public void draw(Canvas canvas) {
        roundViewHelper.drawBefore(canvas);
        super.draw(canvas);
        roundViewHelper.drawAfter(canvas);
    }
}
