package com.example.administrator.sometest.OtherTest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * 能够显示进度的圆环
 * @author Yangjitao
 */
public class ProcessRing extends View {
    private static final String TAG = "ProcessRing";
    private final int COVER_COLOR = 0xFFFECF1F;
    private final int BG_COLOR = 0xFFF2F2F2;
    private final int BG_LINE_WIDTH_DEFALUT = 3;
    private final int COVER_LINE_WIDTH_DEFALUT = 6;
    private int bgLineWidth;
    private int coverLineWidth;
    private Paint mPaint;
    private RectF bgRect;
    private float processValue;
    private int bgRingRadius;
    private ValueAnimator processUpdateAnim;


    public ProcessRing(Context context) {
        super(context);
        init(context);
    }

    public ProcessRing(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProcessRing(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /** 设置进度。范围[0, 1]*/
    public void setProcess(float processValue){
        startProcessAnim(processValue);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(BG_COLOR);
        mPaint.setStrokeWidth(bgLineWidth);
        canvas.drawCircle(getWidth()/2, getHeight()/2, bgRingRadius, mPaint);
        if(bgRect == null) return;
        mPaint.setColor(COVER_COLOR);
        mPaint.setStrokeWidth(coverLineWidth);
        canvas.drawArc(bgRect, -90, processValue*360, false, mPaint);
    }

    /** 动画更新进度*/
    private void startProcessAnim(float toValue){
        if(processUpdateAnim != null) processUpdateAnim.cancel();
        processUpdateAnim = ValueAnimator.ofFloat(processValue, toValue);
        processUpdateAnim.setDuration(200);
        processUpdateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                processValue = (float)animation.getAnimatedValue();
                invalidate();
            }
        });
        processUpdateAnim.start();
    }

    private void init(Context context){
        bgLineWidth = dp2px(getContext(), BG_LINE_WIDTH_DEFALUT);
        coverLineWidth = dp2px(getContext(), COVER_LINE_WIDTH_DEFALUT);

        post(new Runnable() {
            @Override
            public void run() {
                int halfStroke = coverLineWidth / 2;
                bgRect = new RectF(halfStroke, halfStroke, getWidth() - halfStroke, getWidth() - halfStroke);
                bgRingRadius = getWidth()/2 - coverLineWidth/2;
            }
        });

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);

        //debug code
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                testValue += 0.1;
                setProcess(testValue);
            }
        });
    }

    float testValue;

    private   int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}
