package com.example.administrator.sometest.liveUTest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.administrator.sometest.R;

public class SelectReasonActivity2 extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    ScrollView scrollView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_reaso2);
        initView();
        listenLaoutChange();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if(id == R.id.cb_other){
            if(isChecked){
                editText.setVisibility(View.VISIBLE);
                focusOnEditText(editText);
            }else {
                editText.setVisibility(View.GONE);
            }
        }
    }

    private void listenLaoutChange() {
        findViewById(R.id.rl_rootlayout).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.e("yang", "onlayoutchange： " + bottom + " " + oldBottom);

                //根据view的layout的变化判断软键盘是否隐藏
                boolean isSoftBoardHide = bottom > oldBottom && oldBottom != 0;
                boolean isSoftBoardShow = oldBottom > bottom;
                if (isSoftBoardHide) {
                    setSelectionsVisibility(View.VISIBLE);
                }

                if (isSoftBoardShow) {
                    setSelectionsVisibility(View.GONE);
                    hideScrollView();
                }

            }
        });
    }

    private void initView(){
        scrollView = (ScrollView) findViewById(R.id.scrollview);
        editText = (EditText) findViewById(R.id.et_bottom);
        ((CheckBox)findViewById(R.id.cb_other)).setOnCheckedChangeListener(this);
    }

    private void setSelectionsVisibility(int visibility) {
        findViewById(R.id.ll_selections).setVisibility(visibility);
        ((LinearLayout) findViewById(R.id.ll_main_container)).requestLayout();
    }

    private void focusOnEditText(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    private void hideScrollView(){
//        int moveLen = scrollView.getHeight();// - findViewById(R.id.cb_other).getBottom();
//        Animation animation = new TranslateAnimation(0, 0, 0, -1*moveLen);
//        animation.setDuration(3000);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                setSelectionsVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        scrollView.startAnimation(animation);
    }

}


//
//scrollView.setOnTouchListener(new View.OnTouchListener() {
//
//@Override
//public boolean onTouch(View v, MotionEvent event) {
//        //重写onTouch()事件,在事件里通过requestDisallowInterceptTouchEvent(boolean)
//        //方法来设置父类的不可用,true表示父类的不可用
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//        scrollView.requestDisallowInterceptTouchEvent(false);
//        } else {
//        scrollView.requestDisallowInterceptTouchEvent(true);
//        }
//        return false;
//        }
//        });

