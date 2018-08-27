package com.example.administrator.sometest.TaskQueueTest;

import android.os.Handler;
import android.os.HandlerThread;

import com.example.administrator.sometest.IApplication;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Administrator on 2018/8/22 0022.
 */

public class ExclusiveTaskManager {
    private Queue<ExclusiveTask> taskList = new LinkedList<>();
    HandlerThread handlerThread;
    private Handler taskHandler;

    private static ExclusiveTaskManager exclusiveTaskManager = new ExclusiveTaskManager();
    private ExclusiveTaskManager(){
        init();
    }


    public static ExclusiveTaskManager getInstance(){
        return exclusiveTaskManager;
    }

    public void addTask(ExclusiveTask task){
        taskList.add(task);
    }

    public void start(){
        runNextTask();
    }

    public void finish(){
        handlerThread.quit();
        taskList.clear();
    }

    private void runNextTask(){
        if(taskList.isEmpty()) return;
        final ExclusiveTask task = taskList.poll();
        IApplication.getMainHandler().post(task);
        taskHandler.post(new Runnable() {
            @Override
            public void run() {
                task.waitTaskFinish();
                runNextTask();
            }
        });

    }

    private void init(){
        handlerThread = new HandlerThread("ExclusiveTaskManager");
        handlerThread.start();
        taskHandler = new Handler(handlerThread.getLooper());
    }
}
