package com.example.administrator.sometest.RuntimePermissonTest;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.sometest.R;
import com.example.administrator.sometest.RuntimePermissionManager;

public class RuntimePermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_permission);
        RuntimePermissionManager.getInstance().setMainActivity(this);
    }

    public void click1(View view) {
        RuntimePermissionManager.getInstance().requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new RuntimePermissionManager.OnRequestListener() {
            @Override
            public void onRequest(boolean isAllGranted, String[] permissions, int[] results) {
                Toast.makeText(RuntimePermissionActivity.this, isAllGranted + results.toString(), Toast.LENGTH_SHORT).show();
            }
        });

//        startActivity(new Intent(RuntimePermissionActivity.this, CameraActivity.class));
    }

    public void click2(View view) {
        RuntimePermissionManager.getInstance().requestPermission(new String[]{Manifest.permission.CALL_PHONE}, new RuntimePermissionManager.OnRequestListener() {
            @Override
            public void onRequest(boolean isAllGranted, String[] permissions, int[] results) {
                Toast.makeText(RuntimePermissionActivity.this, isAllGranted + results.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void click3(View view) {
        RuntimePermissionManager.getInstance().startApplicationDetailPage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RuntimePermissionManager.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
