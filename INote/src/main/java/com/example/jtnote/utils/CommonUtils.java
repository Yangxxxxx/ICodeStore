package com.example.jtnote.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import com.example.jtnote.Constants;
import com.example.jtnote.INoteApplication;

/**
 * Created by Administrator on 2018/7/12 0012.
 */

public class CommonUtils {

    public static boolean isScreenOn(Context context) {
        boolean isScreenOn = false;
//        try {
//            PowerManager pm = (PowerManager) INoteApplication.getInstance().getSystemService(Context.POWER_SERVICE);
//            if (Constants.ABOVE_KITKAT_WATCH) {
//                isScreenOn = pm.isInteractive();
//            } else {
//                isScreenOn = pm.isScreenOn(); //这里的语句会导致崩溃
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            Log.e("yang", "isScreenon error");
//        }
        return isScreenOn;
    }

    public static void turnOnScreen(Context context) {
//        PowerManager pm = (PowerManager) INoteApplication.getInstance().getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wl = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "turnOnScreen");
//        wl.acquire(1000);
//        wl.release();
    }
}
