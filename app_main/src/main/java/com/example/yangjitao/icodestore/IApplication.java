package com.example.yangjitao.icodestore;

import android.app.Application;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public class IApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppInitialWork.getInstance().init(this);
    }
}
