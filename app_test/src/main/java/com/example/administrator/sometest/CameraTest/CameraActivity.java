package com.example.administrator.sometest.CameraTest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.administrator.sometest.R;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.fl_camera_container);
        frameLayout.addView(new CameraPreview(this));
    }
}
