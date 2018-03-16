package com.example.administrator.sometest;

import android.app.Application;
import android.util.Log;

/**
 * Created by Administrator on 2017/8/22 0022.
 */

public class IApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("yang", "enter IApplication onCreate");
    }
}
