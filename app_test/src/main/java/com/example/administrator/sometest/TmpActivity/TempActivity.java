package com.example.administrator.sometest.TmpActivity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.sometest.R;
import com.example.administrator.sometest.TimeCountTest.TimeCountActivity;

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
//        textView1.setText(R.string.lan_test);
        textView1.setText(cutText("12345678901234567890123"));
//        textView1.setText(cutText("你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好"));


//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                while (true) {
//                    ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
//                    ComponentName cn = activityManager.getRunningTasks(1).get(0).topActivity;
//                    Log.e("yang", "running task: " + cn.getPackageName() + "::" + cn.getClassName());
//                    try {
//                        sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();

        TextView textView = findViewById(R.id.tv100);

        String test = "nihao#ma";
        int index = test.indexOf("#");
        SpannableString spannableString = new SpannableString(test);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        CenteredImageSpan imageSpan = new CenteredImageSpan(drawable);
        CenteredImageSpan3 imageSpan3 = new CenteredImageSpan3(this, R.drawable.ic_launcher);
//        CenteredImageSpan2 imageSpan = new CenteredImageSpan2(drawable);
//        ImageSpan imageSpan = new ImageSpan(drawable);

        ImageSpan imageSpan1 = new ImageSpan(this, R.drawable.ic_launcher, DynamicDrawableSpan.ALIGN_BOTTOM);

        spannableString.setSpan(imageSpan3, index, index + "#".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
//        val imageIndex = text.indexOf(iconTake)
//        val spannableString = SpannableString(text)
////            val drawable = resources.getDrawable(R.drawable.ic_goddess_price_icon)
////            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
////            val imageSpan = ImageSpan(drawable)
//        val imageSpan = ImageSpan(this, R.drawable.icon_incoming_call_coin, DynamicDrawableSpan.ALIGN_BOTTOM)
//        spannableString.setSpan(imageSpan, imageIndex, imageIndex + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
//        layoutEarning.text = spannableString


        TextView textView101 = findViewById(R.id.tv101);
        textView101.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                goBatterySettingPage();
                goHUAWEIPage();
            }
        });

//        findViewById(R.id.tv_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(TempActivity.this, "first click", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("yang", "dispatchTouchEvent : " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("yang", "onTouchEvent : " + event.getAction());
        return super.onTouchEvent(event);
    }

    private String cutText(String text) {
        final int MaxTextLen = 18;
        if (text.length() <= MaxTextLen) return text;
        return text.substring(0, MaxTextLen) + "...";
    }

    private void calulate(String[] args) {
        int discountBefore = 0;
        int discountAfter = 0;
        int[] argInt = new int[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            argInt[i] = Integer.parseInt(args[i]);
            discountBefore += argInt[i];
        }

        discountAfter = Integer.parseInt(args[args.length - 1]);

        for (Integer item : argInt) {
            System.out.println(item + ": " + (item * 1f / discountBefore * discountAfter));
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (bDark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }

    private void goBatterySettingPage(){
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goHUAWEIPage(){
        //华为手机上
        Intent  paramIntent = new Intent("android.intent.action.MAIN");
        paramIntent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
        paramIntent.addCategory("android.intent.category.DEFAULT");
        paramIntent.addCategory("android.intent.category.HOME");
        startActivity(paramIntent);

//        //大部分手机上
//        Intent  paramIntent = new Intent("android.intent.action.MAIN");
//        paramIntent.setComponent(new ComponentName("android", "com.android.internal.app.ResolverActivity"));
//        paramIntent.addCategory("android.intent.category.DEFAULT");
//        paramIntent.addCategory("android.intent.category.HOME");
//        startActivity(paramIntent);
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