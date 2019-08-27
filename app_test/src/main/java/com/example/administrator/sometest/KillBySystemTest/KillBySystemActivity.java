package com.example.administrator.sometest.KillBySystemTest;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.administrator.sometest.R;

public class KillBySystemActivity extends AppCompatActivity {
    private byte[] storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kill_by_system);
        storage = new byte[1024 * 1024 * 150];

        TextView textView = findViewById(R.id.tv_content);
        textView.setText(String.valueOf(CountGlobal.countGlobal.getCount()));

        Log.e("yang", "onCreate");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("yang", "onRestoreInstanceState");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.e("yang", "onSaveInstanceState 1");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("yang", "onSaveInstanceState 2");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        storage = null;
    }
}
