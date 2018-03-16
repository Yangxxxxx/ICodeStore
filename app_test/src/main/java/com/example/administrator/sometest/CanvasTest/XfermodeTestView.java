package com.example.administrator.sometest.CanvasTest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/11/14 0014.
 */

public class XfermodeTestView extends View {
    public XfermodeTestView(Context context) {
        super(context);
    }

    public XfermodeTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public XfermodeTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private Paint paint = new Paint();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        paint.setColor(0xffff0000);
        canvas.drawRect(0, 0, canvas.getWidth(), 40, paint);

//        canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setColor(0xff00ff00);
        canvas.drawRect(0, 0, 40, canvas.getHeight(), paint);
        paint.setXfermode(null);

//        canvas.restore();
//        canvas.restore();
    }
}
