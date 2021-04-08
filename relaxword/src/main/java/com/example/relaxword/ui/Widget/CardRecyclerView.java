package com.example.relaxword.ui.Widget;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CardRecyclerView extends RecyclerView {
    private boolean notIntercept;

    public CardRecyclerView(Context context) {
        super(context);
    }

    public CardRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if(notIntercept){
            return false;
        }else {
            return super.onInterceptTouchEvent(e);
        }
    }

    public void notInterceptTouchEvent(boolean notIntercept) {
        this.notIntercept = notIntercept;
    }
}
