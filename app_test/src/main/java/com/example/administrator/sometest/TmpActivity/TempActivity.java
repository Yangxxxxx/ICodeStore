package com.example.administrator.sometest.TmpActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.sometest.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class TempActivity extends AppCompatActivity {
    private static final String TAG = "TempActivity";

    private Handler handler = new Handler(Looper.getMainLooper());
    public final String PHONE_PERMISSIONS[] = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setDarkStatusIcon(false);
        setContentView(R.layout.activity_temp);
//        shellExec("wm density 240");
//        saveScreenBrightness(255);
        setScreenBrightness(255);

        findViewById(R.id.fl_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                v.setVisibility(v.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
                sendEmail(TempActivity.this);
            }
        });

        findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                v.setVisibility(v.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }


    public static void sendEmail(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
//        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"940350851@qq.com"});
//        intent.putExtra(Intent.EXTRA_SUBJECT, "github");
//        intent.putExtra(Intent.EXTRA_TEXT, "coding for fun!");
        try {
//            if (!(context instanceof Activity)) {
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "未安装邮箱应用！", Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendEmail2(Context context){
        /* Create the Intent */
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        /* Fill it with Data */
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"to@email.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");

        /* Send it off to the Activity-Chooser */
        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }


    private void saveScreenBrightness(int paramInt){
        try{
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        }
        catch (Exception localException){
            localException.printStackTrace();
        }
    }

    private void setScreenBrightness(int paramInt){
        Window localWindow = getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        float f = paramInt / 255.0F;
        localLayoutParams.screenBrightness = f;
        localLayoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        localWindow.setAttributes(localLayoutParams);
    }

    public void shellExec() {
        shellExec("top -m 5 -n 1 -s cpu");
    }

    private void shellExec(String cmd){
        Runtime mRuntime = Runtime.getRuntime();
        try {
            //Process中封装了返回的结果和执行错误的结果
            Process mProcess = mRuntime.exec(cmd);
            BufferedReader mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            StringBuffer mRespBuff = new StringBuffer();
            char[] buff = new char[1024];
            int ch = 0;
            while ((ch = mReader.read(buff)) != -1) {
                Log.e("yang", "cmd: " + new String(buff));
            }
            mReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean isTopActivity(Class activity) {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        boolean isTop = cn.getClassName().contains(activity.getName());

        Log.e("yang", "cn.getClassName() " + cn.getClassName() + " " + isTop);

        return isTop;
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