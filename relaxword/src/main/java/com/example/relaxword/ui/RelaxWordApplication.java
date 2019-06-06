package com.example.relaxword.ui;

import android.app.Application;

public class RelaxWordApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Model.getInstance().init(this);
    }
}
