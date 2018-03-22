package com.example.administrator.sometest.TmpActivity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.sometest.ActivityLaunchTest.Main2Activity;
import com.example.administrator.sometest.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class TempActivity extends AppCompatActivity {
    private static final String TAG = "TempActivity";

    private Handler handler = new Handler(Looper.getMainLooper());
    public final String PHONE_PERMISSIONS[] = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(new AccelerateInterpolator());


        TextView tvContactUsEmpty = (TextView) findViewById(R.id.tv);

        tvContactUsEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TempActivity.this).setTitle("fsljf").show();
            }
        });
//        tvContactUsEmpty.setText("123456789abcedfg");
//        tvContactUsEmpty.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textView1 = (TextView) findViewById(R.id.tv2);
        textView1.setText(Html.fromHtml(getString(R.string.test2)));
        textView1.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textView = findViewById(R.id.tv_name2);
        String text1 = String.format("abcd %1$d", 5);
        String text2 = String.format("nihao %1$s", 5);

    }
}
