package com.example.administrator.sometest.BrodcastReceiverTest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import static com.example.administrator.sometest.BrodcastReceiverTest.ReceiverActivity.Action1;

/**
 * Created by Administrator on 2018/3/29 0029.
 */

public class DynReceivr extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Action1)){
            Toast.makeText(context, "receive action1", Toast.LENGTH_SHORT).show();
            Log.e("yang", "receive action1");
        }
    }
}
