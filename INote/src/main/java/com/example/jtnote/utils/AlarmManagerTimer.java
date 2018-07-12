package com.example.jtnote.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import com.example.jtnote.Constants;
import com.example.jtnote.INoteApplication;

/**
 * Created by Administrator on 2018/7/12 0012.
 */

public class AlarmManagerTimer{
    private AlarmManager alarmManager;

    public AlarmManagerTimer(){
        alarmManager = (AlarmManager) INoteApplication.getInstance().getSystemService(Context.ALARM_SERVICE);
    }

    public void scheduleTask(PendingIntent intent, long delayTime) {
        alarmManager.cancel(intent);
        if(Constants.ABOVE_M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, delayTime, intent);
        }else if(Constants.ABOVE_KITKAT){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, delayTime, intent);
        }else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, delayTime, intent);
        }
    }

    public void cancleTask(PendingIntent intent) {
        alarmManager.cancel(intent);
    }
}
