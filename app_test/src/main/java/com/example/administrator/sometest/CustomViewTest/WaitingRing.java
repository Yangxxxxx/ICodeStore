package com.example.administrator.sometest.CustomViewTest;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 等待动画view。
 *
 */

public class WaitingRing extends FrameLayout {
    private final int SMALL_LEN = 36/2;
    private final int BIG_LEN = 46/2;
    private final int CORNER_RADIUS = 9;
    private final int RING_LINE_WIDTH = 4;
    private Path bgPath = new Path();
    private ValueAnimator alphaAnim;
    private ValueAnimator scaleAnim;

    private float currAlpha;
    private float currRadius;

    private Paint mPaint;


    public WaitingRing(Context context) {
        super(context);
        init(context);
    }

    public WaitingRing(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WaitingRing(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(visibility == View.VISIBLE && !hasAnimRun()){
            startWatingAnim();
        }
    }

    public void setInvisibleWithSpread(){
        if(!hasAnimRun()) return;
        alphaAnim.cancel();
        scaleAnim.cancel();

        alphaAnim = ValueAnimator.ofFloat(currAlpha, 0f);
        alphaAnim.setDuration(700);
        alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                currAlpha = (float)animation.getAnimatedValue();
                invalidate();
            }
        });
        alphaAnim.addListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        setVisibility(INVISIBLE);
                    }
                });
            }
        });

        scaleAnim = ValueAnimator.ofFloat(currRadius, getWidth() / 2);
        scaleAnim.setDuration(700);
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currRadius = (float)animation.getAnimatedValue();
                invalidate();
            }
        });

        alphaAnim.start();
        scaleAnim.start();
        alphaAnim = null;
        scaleAnim = null;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        bgPath.reset();
        bgPath.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), dp2px(CORNER_RADIUS), dp2px(CORNER_RADIUS), Path.Direction.CCW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(0x88000000);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(bgPath, mPaint);

        mPaint.setColor(0xFFFFFFFF);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAlpha((int)(currAlpha*255));
        canvas.drawCircle(getWidth()/2, getHeight()/2, currRadius, mPaint);
    }


    private void init(Context context){
        setBackgroundColor(0x00000000);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(dp2px(RING_LINE_WIDTH));

        if(getVisibility() == View.VISIBLE){
            startWatingAnim();
        }

        //debug code
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setInvisibleWithSpread();
            }
        });
    }

    private void startWatingAnim(){
        post(new Runnable() {
            @Override
            public void run() {
                alphaAnim = ValueAnimator.ofFloat(1.0f, 0.65f);
                alphaAnim.setRepeatCount(ValueAnimator.INFINITE);
                alphaAnim.setRepeatMode(ValueAnimator.REVERSE);
                alphaAnim.setDuration(700);
                alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(final ValueAnimator animation) {
                        currAlpha = (float)animation.getAnimatedValue();
                        invalidate();
                    }
                });

                scaleAnim = ValueAnimator.ofInt(dp2px(SMALL_LEN), dp2px(BIG_LEN));
                scaleAnim.setRepeatMode(ValueAnimator.REVERSE);
                scaleAnim.setRepeatCount(ValueAnimator.INFINITE);
                scaleAnim.setDuration(700);
                scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        currRadius = (int)animation.getAnimatedValue();
                        invalidate();
                    }
                });

                alphaAnim.start();
                scaleAnim.start();
            }
        });
    }

    private int dp2px(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    private boolean hasAnimRun(){
        return alphaAnim != null && scaleAnim != null;
    }

    private class SimpleAnimationListener implements Animator.AnimatorListener{

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}
