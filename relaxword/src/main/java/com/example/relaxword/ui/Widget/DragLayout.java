package com.example.relaxword.ui.Widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.example.relaxword.R;

public class DragLayout extends FrameLayout {
    private CardRecyclerView cardRecyclerView;
    private View contentView;
    private View movePointView;
    private Rect moveRect = new Rect();

    private DragRemoveListener dragRemoveListener;

    public DragLayout(Context context) {
        super(context);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = findViewById(R.id.rl_content_layout);
        movePointView = findViewById(R.id.tv_move_point);
        post(new Runnable() {
            @Override
            public void run() {
                moveRect.set(movePointView.getLeft(), movePointView.getTop(),
                        movePointView.getRight(), movePointView.getBottom());
                contentView.setPivotX(getWidth() / 2);
                contentView.setPivotY(getHeight());
            }
        });
    }


    private float preX;
    private float preY;

    private boolean canDrag;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(moveRect.contains((int) event.getX(), (int) event.getY()) && event.getAction() == MotionEvent.ACTION_DOWN) {
            canDrag = true;
        }else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
            if(canRemove(event) && dragRemoveListener != null){
                showRemoveAnim(new Runnable() {
                    @Override
                    public void run() {
                        dragRemoveListener.onDragRemove(DragLayout.this);
                    }
                });
            }else {
                reset();
            }
            canDrag = false;
        }
        cardRecyclerView.notInterceptTouchEvent(canDrag);


        if (canDrag) {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                preX = event.getX();
                preY = event.getY();
            }
            Log.e("yang", "draglayout: " + event.getAction() + " " + event.getY() + " " + event.hashCode());

            int offsetX = (int) (event.getX() - preX);
            int offsetY = (int) (event.getY() - preY);
            contentView.setTranslationX(offsetX);
            contentView.setTranslationY(offsetY);

            double offsetDistance = Math.sqrt(offsetX * offsetX + offsetY * offsetY);
            float scaleFactor = (float) (1 - (offsetDistance / getHeight()));
            contentView.setScaleX(scaleFactor);
            contentView.setScaleY(scaleFactor);
            contentView.setAlpha(scaleFactor);

            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    public void reset(){
        contentView.setVisibility(VISIBLE);
        contentView.setTranslationX(0);
        contentView.setTranslationY(0);
        contentView.setScaleX(1);
        contentView.setScaleY(1);
        contentView.setAlpha(1);
    }

    private void showRemoveAnim(final Runnable animEndTask){
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.5f, 1f, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -200);
        animationSet.addAnimation(alphaAnimation);
//        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setDuration(200);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.e("an", "fsegesg");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                contentView.setVisibility(INVISIBLE);
                if(animEndTask != null) {
                    animEndTask.run();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        contentView.startAnimation(animationSet);
    }

    private boolean canRemove(MotionEvent event){
        int offsetX = (int) (event.getX() - preX);
        int offsetY = (int) (event.getY() - preY);
        int offsetDistance = (int) Math.sqrt(offsetX * offsetX + offsetY * offsetY);
        return offsetDistance > getHeight() / 3;
    }



    public void setCardRecyclerView(CardRecyclerView cardRecyclerView) {
        this.cardRecyclerView = cardRecyclerView;
    }

    public void setDragRemoveListener(DragRemoveListener dragRemoveListener) {
        this.dragRemoveListener = dragRemoveListener;
    }

    public interface DragRemoveListener{
        void onDragRemove(View view);
    }
}
