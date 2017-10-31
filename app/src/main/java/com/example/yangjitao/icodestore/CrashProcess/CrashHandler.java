package com.example.yangjitao.icodestore.CrashProcess;

import android.content.Context;
import android.os.Looper;
import android.text.ClipboardManager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler instance = new CrashHandler();
    private CrashHandler() {
    }
    public static CrashHandler getInstance() {
        return instance;
    }
    //------------------------------------------------------

    public static final String TAG = "CrashHandler";
    private Context gContext;
    private Thread.UncaughtExceptionHandler defaultHandler;

    public void init(Context context) {
        gContext = context;
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        //把崩溃信息放到系统粘贴板里
        try {
            if(Looper.getMainLooper().getThread().getId() != thread.getId()) {
                Looper.prepare();
            }
            ClipboardManager c= (ClipboardManager)gContext.getSystemService(CLIPBOARD_SERVICE);
            c.setText(getCrashText(ex));
        }catch (Exception e){
            e.printStackTrace();
        }

        defaultHandler.uncaughtException(thread, ex);
    }

    private String getCrashText(Throwable ex) {
        if (ex == null) return null;

        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        return sb.toString();
    }
}
