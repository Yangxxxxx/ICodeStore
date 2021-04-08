package com.example.administrator.sometest.LifeCycleTest;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.sometest.R;

public class LifeCycleActivity extends AppCompatActivity {
    private static final String TAG = "LifeCycleActivity";
    LifeCyclePresenter lifeCyclePresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle);

        lifeCyclePresenter = new LifeCyclePresenter();
        getLifecycle().addObserver(lifeCyclePresenter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(lifeCyclePresenter);
    }

}
