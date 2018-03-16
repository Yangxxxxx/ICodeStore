package com.example.administrator.sometest.OtherTest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/11/29 0029.
 */

public class HalfLayout extends LinearLayout {
    public HalfLayout(Context context) {
        super(context);
    }

    public HalfLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HalfLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//
//        View leftView = findViewById(R.id.tv_1);
//        View rightView = findViewById(R.id.tv_2);
//
//        int halfWidth = (leftView.getWidth() + rightView.getWidth())/2;
//        leftView.getLayoutParams().width = halfWidth;
//        leftView.setLayoutParams(leftView.getLayoutParams());
//        rightView.getLayoutParams().width = halfWidth;
//        rightView.setLayoutParams(rightView.getLayoutParams());
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

//        View leftView = findViewById(R.id.lla);
//        View rightView = findViewById(R.id.llb);
//        int maxWidth = Math.max(leftView.getWidth(), rightView.getWidth());
//
//        if (leftView.getWidth() > rightView.getWidth()) {
//            rightView.getLayoutParams().width = maxWidth;
//            rightView.requestLayout();
//        } else if (leftView.getWidth() < rightView.getWidth()) {
//            leftView.getLayoutParams().width = maxWidth;
//            leftView.requestLayout();
//        }

    }
}
