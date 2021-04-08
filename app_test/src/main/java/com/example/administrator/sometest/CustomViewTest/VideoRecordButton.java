package com.example.administrator.sometest.CustomViewTest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * 视频录制按钮
 * @author Yangjitao
 */
public class VideoRecordButton extends View {
    private static final String TAG = "VideoRecordButton";
    private int STROKE_WIDTH;

    /** 录制时间过段时的，强制录制标志*/
    private boolean isAutoRecording;

    /** 是否再按照上次的录制时间进行录制*/
    private boolean isRepeatRecording;

    private InnerCircle innerCircle;
    private OuterCircle outerCircle;
    private InnerDot innerDot;

    private OnStateListener onStateListener;

    public VideoRecordButton(Context context) {
        super(context);
        init(context);
    }

    public VideoRecordButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoRecordButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void setRepeatRecording(boolean repeatRecording) {
        isRepeatRecording = repeatRecording;
    }

    public void setOnStateListener(OnStateListener onStateListener){
        this.onStateListener = onStateListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        innerCircle.draw(canvas);
        outerCircle.draw(canvas);
        innerDot.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isAutoRecording) return true;
//        if(isRepeatRecording) return true;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                innerCircle.sizeUP(changeEndTask);
                isAutoRecording = isRepeatRecording;
                break;

            case MotionEvent.ACTION_UP:
                int autoCountTime = outerCircle.getAutoTime();
                isAutoRecording = autoCountTime > 0;
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        motionEnd();
                    }
                }, autoCountTime);
                break;
        }
        return true;
    }

    class InnerCircle{
        private ValueAnimator sizeChangeAnim;
        private int animDuration = 500;
        private Paint mPaint;
        private int mainColor = 0xEEDCDCDC;
        private Runnable afterBigTask;
        private boolean maxState;
        private int tmpRadius;

        private int smallRadius;
        private int largeRadius;

        public InnerCircle(Context context){
            mPaint = new Paint();
            mPaint.setColor(mainColor);
            mPaint.setAntiAlias(true);

            smallRadius= tmpRadius = dp2px(context, 30);
            post(new Runnable() {
                @Override
                public void run() {
                    int halfStroke = STROKE_WIDTH / 2;
                    largeRadius = getWidth() / 2 - halfStroke;
                }
            });
        }

        public void sizeUP(Runnable changeEndTask){
            maxState = false;
            cancelAnim();
            sizeChangeAnim = ValueAnimator.ofInt(tmpRadius, largeRadius);
            sizeChangeAnim.addUpdateListener(animatorUpdateListener);
            sizeChangeAnim.addListener(animatorListenerAdapter);
            sizeChangeAnim.setDuration(animDuration);
            sizeChangeAnim.start();
            afterBigTask = changeEndTask;
        }
        public void sizeDown(){
            cancelAnim();
            sizeChangeAnim = ValueAnimator.ofInt(tmpRadius, smallRadius);
            sizeChangeAnim.addUpdateListener(animatorUpdateListener);
            sizeChangeAnim.setDuration(animDuration);
            sizeChangeAnim.start();
        }
        public void draw(Canvas canvas){
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(STROKE_WIDTH);
            canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, tmpRadius, mPaint);
        }
        public boolean isMaxSize(){return maxState;}

        private ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tmpRadius = (int)animation.getAnimatedValue();
                invalidate();
            }
        };

        private void cancelAnim(){
            if(sizeChangeAnim != null && sizeChangeAnim.isStarted()){
                sizeChangeAnim.removeAllUpdateListeners();
                sizeChangeAnim.removeAllListeners();
                sizeChangeAnim.cancel();
            }
        }

        private AnimatorListenerAdapter animatorListenerAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //进入到录制状态
                maxState = true;
                if(onStateListener != null) onStateListener.onLongPressStart();
                if(afterBigTask != null) afterBigTask.run();
            }
        };
    }

    class OuterCircle{
        private final int MIN_TIME = 1 * 1000;
        private final int MAX_TIME = 10 * 1000;
        private ValueAnimator countDownAnim;
        private Paint mPaint;
        private int mainColor = 0xEEF5A623;
        private Runnable afterAnimTask;
        private int duration = MAX_TIME;
        private int preAnimDuration;
        private int processValue;
        private RectF bgRect;



        public OuterCircle(){
            post(new Runnable() {
                @Override
                public void run() {
                    int halfStroke = STROKE_WIDTH / 2;
                    bgRect = new RectF(halfStroke, halfStroke, getWidth() - halfStroke, getWidth() - halfStroke);
                }
            });

            mPaint = new Paint();
            mPaint.setStrokeWidth(STROKE_WIDTH);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mainColor);
            mPaint.setAntiAlias(true);
        }

        public void repeat(final Runnable countEndTask){
            startCount(preAnimDuration, countEndTask);
        }

        public void startCount(final Runnable countEndTask){
            startCount(MAX_TIME, countEndTask);
        }

        public void startCount(int duration, final Runnable countEndTask){
            this.duration = duration;
            afterAnimTask = countEndTask;
            countDownAnim = new ValueAnimator().ofInt(0, 360);
            countDownAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    processValue = (int)animation.getAnimatedValue();
                    if(processValue == 0) processValue = 1;//该值后续用来判断动画是否开始（该回掉一旦被调用就表示动画开始啦）
                    preAnimDuration = getAnimStartTime(processValue);
                    invalidate();
                }
            });
            countDownAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    countEndTask.run();
                }
            });
            countDownAnim.setInterpolator(new LinearInterpolator());
            countDownAnim.setDuration(duration);
            countDownAnim.start();
        }

        public void dissmiss(){
            if(countDownAnim != null) {
                countDownAnim.removeAllUpdateListeners();
                countDownAnim.removeAllListeners();
                countDownAnim.cancel();
            }
            processValue = 0;
        }

        public void draw(Canvas canvas){
            canvas.drawArc(bgRect, -90, processValue, false, mPaint);
        }

        public boolean isAnimEnd(){
            return preAnimDuration == duration;
        }

        public int getAutoTime(){
            if(processValue == 0) return 0;
            return MIN_TIME - getAnimStartTime(processValue);
        }

        private int getAnimStartTime(int process){
            float processRatio = processValue *1f / 360;
            return Math.round(duration*processRatio);
        }
    }

    class InnerDot{
        private int Inner_Radius;
        private Paint mPaint;
        private int mainColor = 0xEEDCDCDC;
        private boolean showable;
        public InnerDot(Context context){
            Inner_Radius = dp2px(context, 30);
            mPaint = new Paint();
            mPaint.setColor(mainColor);
            mPaint.setAntiAlias(true);
        }

        public void showSelf(boolean isShow){
            showable = isShow;
        }
        public void draw(Canvas canvas){
            if(!showable) return;
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, Inner_Radius, mPaint);
        }
    }


    private void init(Context context){
        STROKE_WIDTH = dp2px(context, 3);
        innerCircle = new InnerCircle(context);
        outerCircle = new OuterCircle();
        innerDot = new InnerDot(context);
    }


    private Runnable changeEndTask = new Runnable() {
        @Override
        public void run() {
            innerDot.showSelf(true);
            if(isRepeatRecording){
                outerCircle.repeat(countEndTask);
            }else {
                outerCircle.startCount(countEndTask);
            }
        }
    };

    private Runnable countEndTask = new Runnable() {
        @Override
        public void run() {
            if(onStateListener != null) onStateListener.onLongPressEnd();
            isAutoRecording = false;
            if(isRepeatRecording) {
                motionEnd();
            }
        }
    };

    private void motionEnd(){
        innerCircle.sizeDown();
        outerCircle.dissmiss();
        innerDot.showSelf(false);
        isAutoRecording = false;
        if(onStateListener != null){
            if(!innerCircle.isMaxSize()) {
                onStateListener.onclicked();
            }else if(!outerCircle.isAnimEnd()){
                onStateListener.onLongPressEnd();
            }

        }
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
