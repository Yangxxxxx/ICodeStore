package com.example.administrator.sometest.fragmentTest;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.sometest.R;
import com.example.administrator.sometest.RuntimePermissionManager;

public class Fragment1 extends Fragment implements View.OnClickListener{

    public static Fragment1 newInstance() {
        Fragment1 fragment = new Fragment1();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.bt1){
            click1(v);
        }if(id == R.id.bt2){
            click2(v);
        }if(id == R.id.bt3){
            click3(v);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_runtime_permission, container, false);
        rootView.findViewById(R.id.bt1).setOnClickListener(this);
        rootView.findViewById(R.id.bt2).setOnClickListener(this);
        rootView.findViewById(R.id.bt3).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView textView = view.findViewById(R.id.tv_content);
        textView.setText(Fragment1.this.toString());
    }

    public void click1(View view) {
        RuntimePermissionManager.getInstance().requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new RuntimePermissionManager.OnRequestListener() {
            @Override
            public void onRequest(boolean isAllGranted, String[] permissions, int[] results) {
                Toast.makeText(getContext(), isAllGranted + results.toString(), Toast.LENGTH_SHORT).show();
            }
        });

//        startActivity(new Intent(RuntimePermissionActivity.this, CameraActivity.class));
    }

    public void click2(View view) {
        RuntimePermissionManager.getInstance().requestPermission(new String[]{Manifest.permission.CALL_PHONE}, new RuntimePermissionManager.OnRequestListener() {
            @Override
            public void onRequest(boolean isAllGranted, String[] permissions, int[] results) {
                Toast.makeText(getContext(), isAllGranted + results.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void click3(View view) {
        RuntimePermissionManager.getInstance().startApplicationDetailPage();
    }

}
