package com.example.jtnote.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.jtnote.Model;
import com.example.jtnote.UsageInterface.InoteService;
import com.example.jtnote.bean.NoteItem;
import com.example.jtnote.ui.AlarmRingPage.AlarmRingActivity;
import com.example.jtnote.utils.HandlerTimer;
import com.example.jtnote.utils.ITimer;

import java.util.List;

public class NoteService extends Service{
    private ITimer timer = new HandlerTimer();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
            timer.cancleTask(noteItem.getId());
        }
    }

    private void startAlarmTask(NoteItem noteItem){
        long delayTime = noteItem.getAlarmTime() - System.currentTimeMillis();
        timer.cancleTask(noteItem.getId());
        timer.scheduleTask(noteItem.getId(), genAlarmTask(noteItem), delayTime);
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

    private void startTotalAlarm(){
        List<NoteItem> noteItemList  = Model.getInstance().getAllNotes();
        for (NoteItem item: noteItemList){
            if(item.getAlarmTime() > System.currentTimeMillis()) {
                startAlarmTask(item);
            }
        }
    }
}
