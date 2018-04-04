package com.example.administrator.sometest.BrodcastReceiverTest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/3/29 0029.
 */

public class StaticReceivr extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.haha.action2")){
            Toast.makeText(context, "receive action2", Toast.LENGTH_SHORT).show();
            Log.e("yang", "receive action2");
            context.startService(new Intent(context, SomeTestService.class));
        }
    }
}
