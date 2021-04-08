package com.example.administrator.sometest.liveUTest;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;

import com.example.administrator.sometest.R;

public class SelectReasonActivity extends AppCompatActivity {
    ScrollView scrollView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_reason);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scrollView = (ScrollView)findViewById(R.id.scrollview);
        editText = (EditText)findViewById(R.id.edittext);
        editText.setFocusable(false);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            scrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    Log.e("yang", "onlayoutchange： "+ bottom + " " + oldBottom);

                    //根据view的layout的变化判断软键盘是否隐藏
                    boolean isSoftBoardHide = bottom > oldBottom && oldBottom != 0;
                    if(isSoftBoardHide){
                        showScrollView();
                    }

                }
            });
//            scrollView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
//                @Override
//                public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
//                    Log.e("yang", "insets: " + insets);
//                    return insets;
//                }
//            });
        }
    }

    protected void clickOther(View v){
        hideScrollView();
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    private void hideScrollView(){
        Animation animation = new TranslateAnimation(0, 0, 0, -1*scrollView.getHeight());
        animation.setDuration(500);
        scrollView.startAnimation(animation);
        scrollView.setVisibility(View.INVISIBLE);
    }

    private void showScrollView(){
        Animation animation = new TranslateAnimation(0, 0, -1*scrollView.getHeight(), 0);
        animation.setDuration(500);
        scrollView.startAnimation(animation);
        scrollView.setVisibility(View.VISIBLE);

        Animation animationEditText = new TranslateAnimation(0, 0, 0, editText.getHeight());
        animationEditText.setDuration(500);
        editText.startAnimation(animationEditText);
    }
}
