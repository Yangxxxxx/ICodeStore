package com.example.jtnote;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.jtnote.bean.NoteItem;

import java.util.List;

/**
 * Created by Administrator on 2018/7/9 0009.
 */

public class INoteSharePreference{
    private static final String KEY_NOTE_LIST_ORDER = "key_note_list_order";

    private static final String ORDER_STR_SEPARATOR = ":";

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

    public void setNoteListOrder(List<NoteItem> noteItemList){
        StringBuilder stringBuilder = new StringBuilder();
        for (NoteItem item: noteItemList){
            stringBuilder.append(item.getId()).append(ORDER_STR_SEPARATOR);
        }
        setString(KEY_NOTE_LIST_ORDER, stringBuilder.toString());
    }

    public String[] getNoteListOrder(){
        return getString(KEY_NOTE_LIST_ORDER).split(ORDER_STR_SEPARATOR);
    }
}
