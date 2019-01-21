package com.example.administrator.sometest.ShellTopActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.administrator.sometest.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShelltopActivity extends AppCompatActivity {
    private TextView contentView;
    private boolean shouldStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelltop);
        contentView = findViewById(R.id.shell_content);

        new Thread(){
            @Override
            public void run() {
                super.run();
                shellExec();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        shouldStop = true;
        super.onDestroy();
    }

    public void shellExec() {
        if(shouldStop) return;
        Runtime mRuntime = Runtime.getRuntime();
        try {
            //Process中封装了返回的结果和执行错误的结果
            Process mProcess = mRuntime.exec("top -m 5 -n 1 -s cpu");
            BufferedReader mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            StringBuffer mRespBuff = new StringBuffer();
            char[] buff = new char[1024];
            int ch = 0;
            while ((ch = mReader.read(buff)) != -1) {
            }
            mReader.close();
            showContent(new String(buff));
            shellExec();
        } catch (IOException e) {
            e.printStackTrace();
            showContent("命令执行错误");
        }
    }

    private void showContent(final String content){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contentView.setText(content);
            }
        });
    }
}
