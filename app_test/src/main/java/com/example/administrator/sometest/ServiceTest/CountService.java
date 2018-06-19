package com.example.administrator.sometest.ServiceTest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/6/15 0015.
 */

public class CountService extends Service{
    int i = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("yang", "onStartCommand");
        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.e("yang", "i "+ i++);
            }
        };

        timer.schedule(timerTask, 0, 1000);

        return START_STICKY;
    }
}
