package com.example.yangjitao.icodestore.Utils;

import android.os.Handler;
import android.os.HandlerThread;

/**
 *  数字计时类（从startNum每隔特定时间加一或减一直到等于endNum）
 */

public abstract class NumberCount {
    final private int startNum;
    final private int endNum;
    private int tmpNum;
    private long intervalTime;
    private Handler countHander;
    private Handler bizHandler;
    private int countStep;
    private boolean isCancle;
    private long preOnTickTime;
    private long pauseIntervalTime;

    public NumberCount(int startNum, int endNum, long intervalTime){
        this.startNum = startNum;
        this.endNum = endNum;
        this.intervalTime = intervalTime;
        countStep = endNum > startNum ? 1 : -1;
        if(endNum == startNum) throw new RuntimeException("error: startNum == endNum");

        HandlerThread handlerThread = new HandlerThread(System.currentTimeMillis()+"thread");
        handlerThread.start();
        countHander = new Handler(handlerThread.getLooper());
        bizHandler = new Handler();
    }

    /** 开始计时*/
    public void start(){
        isCancle = false;
        tmpNum = startNum;
        bizHandler.post(new Runnable() {
            @Override
            public void run() {
                preOnTickTime = System.currentTimeMillis();
                onTick(startNum);
            }
        });
        countHander.removeCallbacks(countRunnable);//防止有正在运行的runnable
        countHander.postDelayed(countRunnable, intervalTime);
    }

    /** 取消计时*/
    public void cancel(){
        isCancle = true;
        countHander.removeCallbacks(countRunnable);
    }

    /** 暂停计时*/
    public void pause(){
        if(isCancle) return;
        cancel();
        pauseIntervalTime = System.currentTimeMillis() - preOnTickTime;
    }

    /** 恢复计时*/
    public void resume(){
        if(!isCancle) return;
        isCancle = false;
        countHander.postDelayed(countRunnable, intervalTime - pauseIntervalTime);
    }

    /** 每隔intervalTime调用一次，currNum：表示当前数到几了*/
    public abstract void onTick(int currNum);
    /** 数到endNum时调用*/
    public abstract void onFinish();

    /** 记时任务*/
    private Runnable countRunnable = new Runnable() {
        @Override
        public void run() {
            if (isCancle) return;
            bizHandler.post(new Runnable() {
                @Override
                public void run() {
                    tmpNum += countStep;
                    preOnTickTime = System.currentTimeMillis();
                    onTick(tmpNum);

                    boolean onfinishInc = startNum < endNum && tmpNum >= endNum;
                    boolean onfinishDec = startNum > endNum && tmpNum <= endNum;
                    if(onfinishInc || onfinishDec){
                        onFinish();
                    }else {
                        countHander.postDelayed(countRunnable, intervalTime);
                    }
                }
            });
        }
    };
}
