package com.example.jtnote.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.jtnote.Constants;
import com.example.jtnote.INoteApplication;
import com.example.jtnote.INoteSharePreference;
import com.example.jtnote.Model;
import com.example.jtnote.UsageInterface.InoteService;
import com.example.jtnote.bean.NoteItem;
import com.example.jtnote.ui.AlarmRingPage.AlarmRingActivity;
import com.example.jtnote.utils.AlarmManagerTimer;
import com.example.jtnote.utils.HandlerTimer;
import com.example.jtnote.utils.ITimer;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NoteService extends Service{
//    private ITimer timer = new HandlerTimer();
    private AlarmManagerTimer alarmManagerTimer = new AlarmManagerTimer();
    private String debugInfoKey = "debugInfoKey";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null) {
            Log.e("yang", "intent == null: onstartcommand");
            INoteSharePreference.getInstance().appendString(debugInfoKey, "onStartCommand at: " + Constants.COMMON_DATE_FORMAT.format(System.currentTimeMillis()));
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    INoteSharePreference.getInstance().appendString(debugInfoKey, "i am alive at: "+ Constants.COMMON_DATE_FORMAT.format(System.currentTimeMillis()));
                }
            }, 0, 1000*60*30);
        }

        Log.e("yang", " enter onStartCommand");
        startTotalAlarm();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new NoteServiceBinder();
    }

    private class NoteServiceBinder extends Binder implements InoteService{

        @Override
        public void newAlarmtask(NoteItem noteItem) {
            startAlarmTask(noteItem);
        }

        @Override
        public void cancleAlarmtask(NoteItem noteItem) {
//            timer.cancleTask(noteItem.getId());
            alarmManagerTimer.cancleTask(getAlarmPendingIntent(noteItem));
        }
    }

    private void startAlarmTask(NoteItem noteItem){
//        long delayTime = noteItem.getAlarmTime() - System.currentTimeMillis();
//        timer.cancleTask(noteItem.getId());
//        timer.scheduleTask(noteItem.getId(), genAlarmTask(noteItem), delayTime);
        PendingIntent pendingIntent = getAlarmPendingIntent(noteItem);
        alarmManagerTimer.scheduleTask(pendingIntent, noteItem.getAlarmTime());
    }

    private Runnable genAlarmTask(final NoteItem noteItem){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.e("yang", "task done");
                AlarmRingActivity.start(getApplicationContext(), noteItem);
            }
        };
        return runnable;
    }

    private PendingIntent getAlarmPendingIntent(NoteItem noteItem){
        return PendingIntent.getActivity(INoteApplication.getInstance(), noteItem.getId(), AlarmRingActivity.getIntent(noteItem), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void startTotalAlarm(){
        List<NoteItem> noteItemList  = Model.getInstance().getAllNotes();
        for (NoteItem item: noteItemList){
            if(item.getAlarmTime() > System.currentTimeMillis()) {
                startAlarmTask(item);
            }
        }
    }
}
