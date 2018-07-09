package com.example.jtnote;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/7/9 0009.
 */

public class INoteSharePreference{
    private static final String PREF_NAME = "I_note_pref";
    private static final INoteSharePreference iNoteSharePreference = new INoteSharePreference();
    private SharedPreferences pref;

    public static INoteSharePreference getInstance(){
        return iNoteSharePreference;
    }
    private INoteSharePreference(){
    }

    public void init(Context context){
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void appendString(String key, String appendString){
        pref.edit().putString(key, getString(key) + "\n" + appendString).apply();
    }

    public void setString(String key, String content){
        pref.edit().putString(key, content).apply();
    }

    public String getString(String key){
        return pref.getString(key, "");
    }

    public String getString(String key, String defValue){
        return pref.getString(key, defValue);
    }
}
