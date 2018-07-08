package com.example.jtnote.utils;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.HashMap;

public class HandlerTimer extends ITimer{
    private Handler handler;
    private HashMap<Integer, Runnable> taskMap = new HashMap<>();

    public HandlerTimer(){
        HandlerThread handlerThread = new HandlerThread("HanderTimer");
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    public int scheduleTask(Runnable task, long delayTime) {
        return scheduleTask(genTaskID(), task, delayTime);
    }

    @Override
    public int scheduleTask(int taskID, Runnable task, long delayTime) {
        if(delayTime < 0) return INVALIDATE_ID;
        handler.postDelayed(task, delayTime);

        taskMap.put(taskID, task);
        return taskID;
    }

    @Override
    public void cancleTask(int taskId) {
        Runnable task = taskMap.get(taskId);
        if(task == null) return;
        handler.removeCallbacks(task);
    }
}
