package com.example.administrator.sometest.ServiceTest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/6/15 0015.
 */

public class CountService extends Service{
    long i = 0;
    private SharedPreferences sharedPreferences;

    private String KEY_START = "key_start";
    private String KEY_I = "key_i";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("yang", "CountService onStartCommand");

        sharedPreferences = getSharedPreferences("count_service", Context.MODE_PRIVATE);

        String preStartHistoruy = sharedPreferences.getString(KEY_START, "");
        String currHistory = new SimpleDateFormat("mm-dd hh:mm", Locale.getDefault()).format(System.currentTimeMillis()) +
                "last i: " + sharedPreferences.getLong(KEY_I, 0);
        sharedPreferences.edit().putString(KEY_START, preStartHistoruy + "\n" + currHistory).apply();

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.e("yang", "i "+ i++);
                sharedPreferences.edit().putLong(KEY_I, i).apply();
            }
        };

        timer.schedule(timerTask, 0, 10*1000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("yang", "CountService onDestroy");
    }
}
