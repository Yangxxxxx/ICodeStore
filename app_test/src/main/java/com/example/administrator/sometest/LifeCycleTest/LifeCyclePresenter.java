package com.example.administrator.sometest.LifeCycleTest;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import android.util.Log;

public class LifeCyclePresenter implements LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate(){
        Log.e("yang1", "oncreate LifeCyclePresenter");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onpause123(){
        Log.e("yang1", "onpause123 LifeCyclePresenter");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void ondestory(){
        Log.e("yang1", "ondestory 124");
    }
}
