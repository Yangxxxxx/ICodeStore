package com.example.administrator.sometest.TmpActivity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.sometest.R;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;


public class TempActivity extends AppCompatActivity {
    private static final String TAG = "TempActivity";

    private Handler handler = new Handler(Looper.getMainLooper());
    public final String PHONE_PERMISSIONS[] = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setDarkStatusIcon(false);

        setContentView(R.layout.activity_temp);

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(new AccelerateInterpolator());

        TextView tvContactUsEmpty = (TextView) findViewById(R.id.tv);

        findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new AlertDialog.Builder(TempActivity.this).setTitle("fsljffsegsgjlsejgoisejglsjeglsjeglsiejglsejgl").show();
                Toast.makeText(TempActivity.this, "R.id.bt1", Toast.LENGTH_SHORT).show();
//                Log.e("yang", "bt1 clicked");
//                findViewById(R.id.v_layer).setVisibility(View.VISIBLE);
//                try {
//                    Thread.sleep(4*1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                findViewById(R.id.v_layer).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("yang", "bt2 clicked");
                Toast.makeText(TempActivity.this, "R.id.bt2", Toast.LENGTH_SHORT).show();
            }
        });

//        tvContactUsEmpty.setText("123456789abcedfg");
//        tvContactUsEmpty.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textView1 = (TextView) findViewById(R.id.tv2);
        textView1.setText(R.string.lan_test);
    }

    private void calulate(String[] args){
        int discountBefore = 0;
        int discountAfter = 0;
        int[] argInt = new int[args.length - 1];
        for (int i = 0; i < args.length - 1; i++){
            argInt[i] = Integer.parseInt(args[i]);
            discountBefore += argInt[i];
        }

        discountAfter = Integer.parseInt(args[args.length - 1]);

        for(Integer item: argInt){
            System.out.println(item + ": " +(item * 1f / discountBefore * discountAfter));
        }

    }

    @Override
    protected void onStart() {
        Log.e("yang", "onStart done");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.e("yang", "onResume done");
        super.onResume();
    }

    public void setDarkStatusIcon(boolean bDark) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            View decorView = getWindow().getDecorView();
            if(decorView != null){
                int vis = decorView.getSystemUiVisibility();
                if(bDark){
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else{
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }
}


//    HandlerThread handlerThread = new HandlerThread("fjslfj");
//        Handler handler2 = new Handler(handlerThread.getLooper());
//
//
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//
//                Looper.prepare();
//                Handler handler = new Handler(){
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                    }
//                };
//                Looper.loop();
//
//            }
//        }.start();