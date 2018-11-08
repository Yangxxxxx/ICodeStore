package com.example.jtnote.utils;

import android.content.Context;
import android.util.DisplayMetrics;

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

    public static String getDuration(long timeMilli){
        final long milli_per_minute = 1000 * 60;
        final long milli_per_hour = milli_per_minute * 60;
        final long milli_per_day = milli_per_hour * 24;
        long surplusTime = timeMilli;

        long day = surplusTime / milli_per_day;
        surplusTime = surplusTime - (day * milli_per_day);
        long hour = surplusTime / milli_per_hour;
        surplusTime = surplusTime - (hour * milli_per_hour);
        long minute = surplusTime / milli_per_minute;

        String timeStr = "";
        if(day > 0){
            timeStr = day + "天";
        }
        if(hour > 0){
            timeStr += hour + "小时";
        }
        if(minute > 0){
            timeStr += minute + "分钟";
        }
        return timeStr;
    }

    public static float dp2Px(Context context, int dp){
        float density = context.getResources().getDisplayMetrics().density;
        return density * dp;
    }

}
