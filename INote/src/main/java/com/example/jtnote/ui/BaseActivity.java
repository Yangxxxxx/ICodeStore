package com.example.jtnote.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jtnote.UsageInterface.InoteService;
import com.example.jtnote.service.NoteService;

public class BaseActivity extends AppCompatActivity{


    public void bindNoteService(){
        bindService(new Intent(this, NoteService.class), serviceConnection, BIND_AUTO_CREATE);
    }


    private void onNoteServiceConnected(InoteService inoteService){}
    private void onNoteServiceDisconnected(){}



    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            onNoteServiceConnected((InoteService)service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            onNoteServiceDisconnected();
        }
    };
}
