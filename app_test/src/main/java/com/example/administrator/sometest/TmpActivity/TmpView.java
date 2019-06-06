package com.example.administrator.sometest.TmpActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TmpView extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "TmpView";
    public TmpView(Context context) {
        super(context);
    }

    public TmpView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TmpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        Log.e(TAG, "onVisibilityChanged: " + visibility);
    }
}
