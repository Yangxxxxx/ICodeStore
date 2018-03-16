package com.example.administrator.sometest.MeasureLayoutTest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.sometest.R;

/**
 * Created by Administrator on 2018/2/9 0009.
 */

public class ProfileNameLayout2 extends RelativeLayout {
    public ProfileNameLayout2(Context context) {
        super(context);
    }

    public ProfileNameLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProfileNameLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


//        @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        View editView = findViewById(R.id.tv_name_edit2);
//        TextView nameView = findViewById(R.id.tv_name2);
//
//        super.onLayout(changed, l, t, r, b);
//
//        if(getMeasuredWidth() - nameView.getRight() < editView.getMinimumWidth()){
//            editView.layout(getMeasuredWidth() - editView.getMinimumWidth(), editView.getTop(), getMeasuredWidth(), editView.getBottom());
//            nameView.layout(editView.getMinimumWidth(),
//                    nameView.getTop(), getMeasuredWidth() - editView.getMinimumWidth(), nameView.getBottom());
//            nameView.setText(nameView.getText());
//
//        }
//
//
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        View editView = findViewById(R.id.tv_name_edit2);
        TextView nameView = findViewById(R.id.tv_name2);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        nameView.measure(MeasureSpec.makeMeasureSpec(600, MeasureSpec.AT_MOST), heightMeasureSpec);

//        if (getMeasuredWidth() - nameView.getMeasuredWidth() < editView.getMinimumWidth()) {
//            editView.measure(MeasureSpec.makeMeasureSpec(editView.getMinimumWidth(), MeasureSpec.AT_MOST), heightMeasureSpec);
//            nameView.measure(MeasureSpec.makeMeasureSpec(width - editView.getMinimumWidth()*2, MeasureSpec.AT_MOST), heightMeasureSpec);
//        }
    }
}
