package com.example.administrator.sometest.OverlayGuideTest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.sometest.R;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View view = findViewById(R.id.iv1);
        new NewbieGuideManager(this, NewbieGuideManager.TYPE_COLLECT).addView(view, HoleBean.TYPE_CIRCLE).show();
    }
}
