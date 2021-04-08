package com.example.relaxword.ui.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.example.relaxword.R;
import com.example.relaxword.ui.Widget.GestureFramlayout;
import com.example.relaxword.ui.fragment.ScrollHorizontalFragment;
import com.example.relaxword.ui.fragment.WordCardFragment;

public class MainActivity extends AppCompatActivity implements GestureFramlayout.ScrollHorizontalListener, ScrollHorizontalFragment.BlankPageVisibilityListener {
    private WordCardFragment wordCardFragment;
    private ScrollHorizontalFragment scrollHorizontalFragment;
    private GestureFramlayout gestureFramlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        wordCardFragment = (WordCardFragment) getSupportFragmentManager().findFragmentById(R.id.Word_Card_Fragment);
        scrollHorizontalFragment = (ScrollHorizontalFragment)getSupportFragmentManager().findFragmentById(R.id.Scroll_Horizontal_Fragment);
        gestureFramlayout = findViewById(R.id.gesture_layout);
        gestureFramlayout.setScrollHorizontalListener(this);
    }

    @Override
    public void onScrollHorizontalDetected() {
        scrollHorizontalFragment.updateWord(wordCardFragment.getShowingWord());
        Log.e("yang", "onScrollHorizontalDetected");
    }

    @Override
    public void onBlankPageVisibilityChange(boolean isVisible) {
        gestureFramlayout.setGestureDetectEnable(isVisible);
    }

    @Override
    public void onBackPressed() {
        if(scrollHorizontalFragment.backPress()) return;
        super.onBackPressed();
    }
}
