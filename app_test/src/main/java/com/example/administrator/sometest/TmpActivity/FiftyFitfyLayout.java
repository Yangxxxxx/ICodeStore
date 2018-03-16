package com.example.administrator.sometest.TmpActivity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.sometest.R;

/**
 * Created by Administrator on 2018/2/22 0022.
 */

public class FiftyFitfyLayout extends LinearLayout {
    public FiftyFitfyLayout(Context context) {
        this(context, null);
    }

    public FiftyFitfyLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FiftyFitfyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.tv1);
                textView.setText(textView.getText()+"a");
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("yang", "enter onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e("yang", "enter onLayout");

        super.onLayout(changed, l, t, r, b);
        setLayoutSize();

    }

    private void setLayoutSize(){
        ViewGroup leftView = findViewById(R.id.fl_left);
        ViewGroup rightView = findViewById(R.id.fl_right);

        int leftMeasuredWidth = leftView.getMeasuredWidth();
        int rightMeasuredWidth = rightView.getMeasuredWidth();

        if(leftMeasuredWidth > rightMeasuredWidth){
            rightView.getLayoutParams().width = leftMeasuredWidth;
        }else if(leftMeasuredWidth < rightMeasuredWidth){
            leftView.getLayoutParams().width = rightMeasuredWidth;
        }
    }
}
