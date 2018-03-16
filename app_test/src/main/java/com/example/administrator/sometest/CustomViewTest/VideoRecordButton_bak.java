package com.example.administrator.sometest.CustomViewTest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * 视频录制按钮
 * @author Yangjitao
 */
public class VideoRecordButton_bak extends View {
    private static final String TAG = "VideoRecordButton";
    private Paint mPaint;
    private int progressColor = 0xEEF5A623;
    private int bgColor = 0xEEDCDCDC;
    private int strokeWidth;
    private int smallRadius;
    private int largeRadius;
    private int tmpRadius;
    private int processValue;
    private ValueAnimator sizeChangeAnim;
    private ValueAnimator countDownAnim;
    private int tmpInnerRadius;
    private int InnerRadius;
    private RectF bgRect;
    private OnStateListener onStateListener;

    private enum ButtonState{CLICK, LOGNPRESS}
    private ButtonState buttonState = ButtonState.CLICK;
    /** 录制时间过段时的，强制录制标志*/
    private boolean isAutoRecording;

    private int MIN_RECORD_TIME = 1;
    private int MAX_RECORD_TIME = 10;

    public VideoRecordButton_bak(Context context) {
        super(context);
        init(context);
    }

    public VideoRecordButton_bak(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoRecordButton_bak(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setOnStateListener(OnStateListener onStateListener){
        this.onStateListener = onStateListener;
    }

    private void init(Context context){
        InnerRadius = dp2px(context, 30);
        smallRadius = tmpRadius = dp2px(context, 30);
        strokeWidth = dp2px(context, 3);
        mPaint = new Paint();
        mPaint.setColor(bgColor);
        mPaint.setAntiAlias(true);

        post(new Runnable() {
            @Override
            public void run() {
                int halfStroke = strokeWidth / 2;
                largeRadius = getWidth() / 2 - halfStroke;
                bgRect = new RectF(halfStroke, halfStroke, getWidth() - halfStroke, getHeight() - halfStroke);
            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int pivotX = canvas.getWidth()/2;
        int pivotY = canvas.getHeight()/2;

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(bgColor);
        canvas.drawCircle(pivotX, pivotY, tmpRadius, mPaint);

        mPaint.setColor(progressColor);
        canvas.drawArc(bgRect, -90, processValue, false, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(pivotX, pivotY, tmpInnerRadius, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isAutoRecording) return true;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                buttonState = ButtonState.CLICK;
                processValue = 0;
                playSizeChangeAnim(smallRadius, largeRadius, true);
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                fingerUp();
                break;
        }
        return true;
    }

    /** 手指抬起时的处理*/
    private void fingerUp(){
        float hasRecordTime = processValue / 36f;
        float needTime = 0;
        if(buttonState == ButtonState.LOGNPRESS && hasRecordTime < MIN_RECORD_TIME){
                //已录制时间少于最小录制时间时，强制录制到达到最小时间
            needTime = MIN_RECORD_TIME - hasRecordTime;
            isAutoRecording = true;
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                if(buttonState == ButtonState.CLICK){
                    Log.e(TAG, "clicked");
                    if(onStateListener != null) onStateListener.onclicked();
                }else {
                    Log.e(TAG, "long press end");
                    isAutoRecording = false;
                    if(onStateListener != null) onStateListener.onLongPressEnd();
                }
                playSizeChangeAnim(tmpRadius, smallRadius, true);
                playCountAnim(false);
                tmpInnerRadius = 0;
            }
        }, (int)(needTime * 1000));
    }

    /** 开始圆圈按钮尺寸变化的动画*/
    private void playSizeChangeAnim(final int startRadius, final int endRadius, boolean isForce){
        if(sizeChangeAnim != null && sizeChangeAnim.isStarted() && isForce){
            sizeChangeAnim.removeAllUpdateListeners();
            sizeChangeAnim.removeAllListeners();
            sizeChangeAnim.cancel();
        }

        sizeChangeAnim = ValueAnimator.ofInt(startRadius, endRadius);
        sizeChangeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tmpRadius = (int)animation.getAnimatedValue();
                invalidate();
            }
        });
        sizeChangeAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(endRadius > startRadius) {
                    //进入到录制状态
                    tmpInnerRadius = InnerRadius;
                    playCountAnim(true);
                    buttonState = ButtonState.LOGNPRESS;
                    Log.e(TAG, "long press start");
                    if(onStateListener != null) onStateListener.onLongPressStart();
                }
            }

        });
        sizeChangeAnim.setDuration(500);
        sizeChangeAnim.start();
    }

    /** 开始倒计时倒数动画*/
    private void playCountAnim(boolean showable){
        if(!showable){
            if(countDownAnim != null) {
                countDownAnim.removeAllUpdateListeners();
                countDownAnim.removeAllListeners();
                countDownAnim.cancel();
            }
            processValue = 0;
            return;
        }

        countDownAnim = new ValueAnimator().ofInt(0, 360);
        countDownAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                processValue = (int)animation.getAnimatedValue();
                invalidate();
            }
        });
        countDownAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e(TAG, "long press end 10s");
                if(onStateListener != null) onStateListener.onLongPressEnd();
            }
        });
        countDownAnim.setInterpolator(new LinearInterpolator());
        countDownAnim.setDuration(1000 * MAX_RECORD_TIME);
        countDownAnim.start();
    }

    public  int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public interface OnStateListener{
        void onclicked();
        void onLongPressStart();
        void onLongPressEnd();
    }
}
