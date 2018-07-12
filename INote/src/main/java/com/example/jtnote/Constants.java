package com.example.jtnote;

import android.os.Build;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class Constants {
    public static final String KEY_INPUT_CONTENT = "key_input_content";
    public static final String KEY_NOTEITEM_PARAM = "key_noteitem_param";

    public static final SimpleDateFormat COMMON_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final boolean ABOVE_KITKAT_WATCH = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH;
    public static final boolean ABOVE_N = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    public static final boolean ABOVE_M = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    public static final boolean ABOVE_O = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    public static final boolean ABOVE_KITKAT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
}
