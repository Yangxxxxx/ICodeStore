package com.example.jtnote;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;

import com.example.jtnote.service.NoteService;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class INoteApplication extends Application{
    private static Handler mainHandler;
    private static INoteApplication iNoteApplication;

    public static INoteApplication getInstance(){
        return iNoteApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        iNoteApplication = this;

        mainHandler = new Handler(getMainLooper());

        Model.getInstance().init(this);
        startService(new Intent(this, NoteService.class));
    }

    public void runOnUiThread(Runnable runnable){
        mainHandler.post(runnable);
    }
}
