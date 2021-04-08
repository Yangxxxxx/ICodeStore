package com.example.administrator.sometest.BrodcastReceiverTest;

import android.content.Intent;
import android.content.IntentFilter;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.sometest.R;

public class ReceiverActivity extends AppCompatActivity {
    public final static String Action1 = "com.haha.action1";
    private DynReceivr dynReceivr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Action1);

        dynReceivr = new DynReceivr();
        registerReceiver(dynReceivr, intentFilter);

        findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcast(new Intent(Action1));
            }
        });

        findViewById(R.id.tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcast(new Intent("com.haha.action2"));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dynReceivr);
    }
}
