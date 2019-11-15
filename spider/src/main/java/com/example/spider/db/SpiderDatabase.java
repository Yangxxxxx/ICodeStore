package com.example.spider.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.spider.bean.WordState;

import java.util.ArrayList;

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



    public ArrayList<String> queryAllWord(){
        ArrayList<String> words = new ArrayList<>();
        SQLiteDatabase db = wordBDHelper.getReadableDatabase();
        Cursor cursor = db.query(WordTable.TABLE_NAME, null, WordTable.STATE + "=?", new String[]{String.valueOf(WordState.BLANK)}, null, null, null);
        int wordIndex = cursor.getColumnIndex(WordTable.NAME);
        while (cursor.moveToNext()){
            String item = cursor.getString(wordIndex);
            words.add(item);
        }
        cursor.close();
        db.close();
        return words;
    }

    public void updateWord(String word, String explain, int state){
        SQLiteDatabase db = wordBDHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WordTable.EXPLAIN, explain);
        values.put(WordTable.STATE, state);
        db.update(WordTable.TABLE_NAME, values, WordTable.NAME + "=?", new String[]{word});
        db.close();
    }
}
