package com.example.jtnote;

import android.app.Application;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class INoteApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Model.getInstance().init(this);
    }
}
