package com.example.administrator.sometest.TmpActivity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.example.administrator.sometest.R;

import java.lang.ref.SoftReference;


public class TempActivity extends AppCompatActivity {
    private static final String TAG = "TempActivity";

    private Handler handler = new Handler(Looper.getMainLooper());
    public final String PHONE_PERMISSIONS[] = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(new AccelerateInterpolator());

        TextView tvContactUsEmpty = (TextView) findViewById(R.id.tv);

        tvContactUsEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TempActivity.this).setTitle("fsljffsegsgjlsejgoisejglsjeglsjeglsiejglsejgl").show();
            }
        });
//        tvContactUsEmpty.setText("123456789abcedfg");
//        tvContactUsEmpty.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textView1 = (TextView) findViewById(R.id.tv2);
        textView1.setText(Html.fromHtml(getString(R.string.test2)));
        textView1.setMovementMethod(LinkMovementMethod.getInstance());


        String text = "fjslkdfj";
        SoftReference<String> textSoft = new SoftReference<String>(text);
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