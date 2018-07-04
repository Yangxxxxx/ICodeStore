package com.example.administrator.sometest.LayoutChangeTest;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/11/16 0016.
 */

public class InsetLayout extends RelativeLayout {
    public InsetLayout(Context context) {
        super(context);
        init();
    }

    public InsetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InsetLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    private void init() {
        if(Build.VERSION.SDK_INT >= 20) {
            setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                    Log.e("yang", "insets: " + insets.getSystemWindowInsetLeft() + " " +
                            insets.getSystemWindowInsetTop() + " " +
                            insets.getSystemWindowInsetRight() + " " +
                            insets.getSystemWindowInsetBottom());
                    return insets;
                }
            });
        }
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        Log.e("yang", "fitSystemWindows: " + insets.left + " " +
                insets.right + " " +
                insets.top + " " +
                insets.bottom);
        return super.fitSystemWindows(insets);
    }
}
