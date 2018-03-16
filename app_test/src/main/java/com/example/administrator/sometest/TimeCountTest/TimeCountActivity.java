package com.example.administrator.sometest.TimeCountTest;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.administrator.sometest.R;

public class TimeCountActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_count);
        postTest();
    }

    //--------------------------------------------TimeCount测试------------------------------------V
    private CountDownTimer countDownTimer = new CountDownTimer(10*1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("yang", "millisUntilFinished: " + millisUntilFinished/1000);
        }

        @Override
        public void onFinish() {
            Log.e("yang", "onFinish");
        }
    };

    public void click2(View view) {
        countDownTimer.start();
    }

    public void click3(View view) {
        countDownTimer.cancel();
    }
    //---------------------------------------------------------------------------------------------^

   //-----------------------------------------------postdelay测试----------------------------------V
    Handler mhandler;
    private void postTest(){
        HandlerThread handlerThread = new HandlerThread("123456");
        handlerThread.start();
        mhandler = new Handler(handlerThread.getLooper());

        mhandler.post(new Runnable() {
            @Override
            public void run() {
                Log.e("yang", "sleep before");
            }
        });

        mhandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mhandler.post(new Runnable() {
            @Override
            public void run() {
                Log.e("yang", "sleep after");
            }
        });

    }

    public void click1(View view) {
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("yang", "deleay task");
            }
        }, 3000);
    }

    //---------------------------------------------------------------------------------------------^
}
