package com.example.administrator.sometest.ServiceTest;

import android.content.Intent;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.sometest.R;

import java.util.Timer;

public class ServiceTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);
        startService(new Intent(getApplicationContext(), CountService.class));
    }
}
