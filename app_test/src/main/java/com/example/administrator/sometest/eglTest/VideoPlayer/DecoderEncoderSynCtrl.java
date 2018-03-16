package com.example.administrator.sometest.eglTest.VideoPlayer;

/**
 * Created by Administrator on 2017/10/11 0011.
 */

public class DecoderEncoderSynCtrl {
    private static final DecoderEncoderSynCtrl ourInstance = new DecoderEncoderSynCtrl();

    public static DecoderEncoderSynCtrl getInstance() {
        return ourInstance;
    }

    private DecoderEncoderSynCtrl() {
    }

    private Object lock = new Object();

    public void waitMinute(){
        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyEveryOne(){
        synchronized (lock){
            lock.notifyAll();
        }
    }
}
