package com.example.jtnote.utils;

/**
 * 不建议 {@link #scheduleTask(int, Runnable, long)} 和 {@link #scheduleTask(Runnable, long)} 混着用！（容易导致ID重复）
 */
public abstract class ITimer {
    public static final int INVALIDATE_ID = -1;
    private int taskId = 0;

    protected int genTaskID(){
        return ++taskId;
    }

    /**
     * @param taskID 自己传入的ID
     * @param task
     * @param delayTime
     * @return
     */
    public abstract int scheduleTask(int taskID, Runnable task, long delayTime);

    /**
     * @return taskID（可以通过该ID取消还没执行的任务）
     */
    public abstract int scheduleTask(Runnable task, long delayTime);

    /**
     * @see #scheduleTask(int, Runnable, long)
     * @see #scheduleTask(Runnable, long)
     * @param taskId taskID
     */
    public abstract void cancleTask(int taskId);
}
