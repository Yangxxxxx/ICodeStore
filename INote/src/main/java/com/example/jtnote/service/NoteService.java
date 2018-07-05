package com.example.jtnote.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.jtnote.Model;
import com.example.jtnote.UsageInterface.InoteService;
import com.example.jtnote.bean.NoteItem;
import com.example.jtnote.ui.AlarmRingPage.AlarmRingActivity;

import java.util.HashMap;
import java.util.List;

public class NoteService extends Service{
    private HashMap<Integer, Runnable> alarmTaskMap = new HashMap<>();
    private Handler handler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("yang", " enter onStartCommand");
        HandlerThread handlerThread = new HandlerThread("NoteServiceThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
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
            if(noteItem.getAlarmTime() <= System.currentTimeMillis()) return;
            if(alarmTaskMap.containsKey(noteItem.getId())){
                Runnable runnable = alarmTaskMap.get(noteItem);
                handler.removeCallbacks(runnable);
            }
            alarmTaskMap.put(noteItem.getId(), startAlarmTask(noteItem));
        }
    }

    private Runnable startAlarmTask(final NoteItem noteItem){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.e("yang", "task done");
                AlarmRingActivity.start(getApplicationContext(), noteItem);
            }
        };
        handler.postDelayed(runnable, noteItem.getAlarmTime() - System.currentTimeMillis());
        return runnable;
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
