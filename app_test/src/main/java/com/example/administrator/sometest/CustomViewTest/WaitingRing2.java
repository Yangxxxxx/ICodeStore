package com.example.administrator.sometest.CustomViewTest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.example.administrator.sometest.R;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public class WaitingRing2 extends FrameLayout {
    private final int SMALL_LEN = 36;
    private final int BIG_LEN = 42;
    private View ringView;
    private ValueAnimator alphaAnim;
    private ValueAnimator scaleAnim;

    private float currAlpha;
    private float currScale;



    public WaitingRing2(Context context) {
        super(context);
        init(context);
    }

    public WaitingRing2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WaitingRing2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setBackgroundColor(0x88000000);

        LayoutParams params = new LayoutParams(150, 150);
        params.gravity= Gravity.CENTER;
        ringView = new View(context);
        ringView.setLayoutParams(params);
        ringView.setBackgroundResource(R.drawable.white_ring);
        addView(ringView);

       startAnim2();

       setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               spreadDismiss();
           }
       });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void spreadDismiss(){
        alphaAnim.cancel();
        scaleAnim.cancel();

        alphaAnim = ValueAnimator.ofFloat(currAlpha, 0f);
        alphaAnim.setDuration(700);
        alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                setRingViewAlpha((float)animation.getAnimatedValue());

            }
        });

        scaleAnim = ValueAnimator.ofFloat(1, getWidth() * 1f / ringView.getWidth());
        scaleAnim.setDuration(700);
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setRingViewScale((float)animation.getAnimatedValue());
            }
        });

        alphaAnim.start();
        scaleAnim.start();
    }


    private void startAnim(final View view){
        post(new Runnable() {
            @Override
            public void run() {
                AnimationSet anim = new AnimationSet(getContext(), null);
                AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.65f);alpha.setRepeatCount(Animation.INFINITE);alpha.setRepeatMode(Animation.REVERSE);

                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.3f, 1, 1.3f, view.getWidth()/2, view.getHeight()/2);
                scaleAnimation.setRepeatCount(Animation.INFINITE);
                scaleAnimation.setRepeatMode(Animation.REVERSE);

                anim.setDuration(700);
                anim.addAnimation(alpha);
                anim.addAnimation(scaleAnimation);
                view.startAnimation(anim);
            }
        });
    }


    private void startAnim2(){
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
                        setRingViewAlpha(currAlpha);

                    }
                });

                scaleAnim = ValueAnimator.ofFloat(1, 1.3f);
                scaleAnim.setRepeatMode(ValueAnimator.REVERSE);
                scaleAnim.setRepeatCount(ValueAnimator.INFINITE);
                scaleAnim.setDuration(700);
                scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        currScale = (float)animation.getAnimatedValue();
                        setRingViewScale(currScale);
                    }
                });

                alphaAnim.start();
                scaleAnim.start();
            }
        });
    }


    private void setRingViewAlpha(final float alpha){
        post(new Runnable() {
            @Override
            public void run() {
                ringView.setAlpha(alpha);
            }
        });
    }

    private void setRingViewScale(final float scaleValue){
        post(new Runnable() {
            @Override
            public void run() {
                ringView.setScaleX(scaleValue);
                ringView.setScaleY(scaleValue);
            }
        });
    }


}
