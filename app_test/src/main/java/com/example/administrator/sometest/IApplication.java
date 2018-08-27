package com.example.administrator.sometest;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Created by Administrator on 2017/8/22 0022.
 */

public class IApplication extends Application implements Application.ActivityLifecycleCallbacks{
    private static Handler mUiHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("yang", "enter IApplication onCreate");
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.e("yang", "onActivityCreated " + activity.toString());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.e("yang", "onActivityStarted " + activity.toString());

    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e("yang", "onActivityResumed " + activity.toString());

    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e("yang", "onActivityPaused " + activity.toString());

    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e("yang", "onActivityStopped " + activity.toString());

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e("yang", "onActivitySaveInstanceState " + activity.toString());

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e("yang", "onActivityDestroyed " + activity.toString());

    }

    public static Handler getMainHandler(){
        return mUiHandler;
    }
}
