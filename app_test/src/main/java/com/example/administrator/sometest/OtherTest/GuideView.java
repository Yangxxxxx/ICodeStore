package com.example.administrator.sometest.OtherTest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class GuideView extends RelativeLayout {
    private Paint mPaint;
    public GuideView(Context context) {
        this(context, null);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //关闭当前view的硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xb2000000);
        canvas.drawRect(new RectF(0, 0, 150, 150), mPaint);
    }
}
