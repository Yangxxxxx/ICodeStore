package com.example.administrator.sometest.ViewDrawProcessTest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2018/6/8 0008.
 */

public class RelativeLayoutTest extends LinearLayout{
    Paint paint;
    public RelativeLayoutTest(Context context) {
        this(context, null);
    }

    public RelativeLayoutTest(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelativeLayoutTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);

        setPadding(0, 80, 0, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("yang", this.hashCode()+ " onMeasure: " + MeasureSpec.getSize(widthMeasureSpec) + "  " + MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e("yang", this.hashCode()+" onLayout: " + l + " " + t + " " + r + " " + b);
//        View view = getChildAt(0);
//        if(view != null){
//            view.layout(80, 80, r/2, b/2);
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(this.hashCode()+"", 50, 50, paint);
    }
}
