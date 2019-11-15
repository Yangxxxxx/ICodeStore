package com.example.spider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.example.spider.bean.WordState;
import com.example.spider.db.SpiderDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class GenOriginalDataView extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener{
    private int count;
    private SpiderDatabase database;
    public GenOriginalDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        database = new SpiderDatabase(context);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        setOnClickListener(null);
        start();
        showCount();
    }

    private void start(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(getResources().getAssets().open("google-10000-english-usa.txt"));
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null){
                        if(line.length() > 1){
                            database.insertWord(line, "", WordState.BLANK);
                        }
                        count ++;
                    }
                    bufferedReader.close();
                    inputStreamReader.close();

                    Utils.copyFileUsingFileStreams(getContext().getDatabasePath("dictionary.db"), new File("/sdcard/spider_dic.db"));

                    post(new Runnable() {
                        @Override
                        public void run() {
                            setText("complete");
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void showCount(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        setText("count: " + count);
                    }
                });
            }
        }, 1000, 1000);
    }
}
