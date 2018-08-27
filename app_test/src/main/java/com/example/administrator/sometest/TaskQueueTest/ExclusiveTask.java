package com.example.administrator.sometest.TaskQueueTest;

/**
 * Created by Administrator on 2018/8/22 0022.
 */

public abstract class ExclusiveTask implements Runnable{
    private boolean stayRunning;

    public abstract void excute();

    @Override
    public void run() {
        excute();
        if(!stayRunning) {
            finishTask();
        }
    }

    public void stayTaskRunning(){
        stayRunning = true;
    }

    public void waitTaskFinish(){
        try {
            synchronized (this) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void finishTask(){
        stayRunning = false;
        synchronized (this) {
            notifyAll();
        }
    }
}
