package com.example.spider.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SpiderDatabase {
    private WordBDHelper wordBDHelper;

    public SpiderDatabase(Context context){
        wordBDHelper = new WordBDHelper(context);
    }

    public void insertWord(String name, String explain, int state){
        SQLiteDatabase db = wordBDHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WordTable.NAME, name);
        values.put(WordTable.EXPLAIN, explain);
        values.put(WordTable.STATE, state);
        db.insert(WordTable.TABLE_NAME, null, values);
        db.close();
    }
}
