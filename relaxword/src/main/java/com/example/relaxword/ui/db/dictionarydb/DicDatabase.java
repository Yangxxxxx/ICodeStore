package com.example.relaxword.ui.db.dictionarydb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.relaxword.ui.bean.Translation;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DicDatabase {
    private DicDatabaseHelper dicDatabaseHelper;

    public DicDatabase(Context context) {
        dicDatabaseHelper = new DicDatabaseHelper(context);
    }

    public void insertWord(Translation wordBean) {
        SQLiteDatabase database = dicDatabaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DicDatabaseHelper.DicTable.WORD_INFO, new Gson().toJson(wordBean));
        contentValues.put(DicDatabaseHelper.DicTable.WORD, wordBean.getWord());
        database.insert(DicDatabaseHelper.DicTable.TABLE_NAME, null, contentValues);

        database.close();
    }

    public List<Translation> qureyAllTranslation() {
        List<Translation> wordBeanList = new ArrayList<>();
        SQLiteDatabase database = dicDatabaseHelper.getReadableDatabase();
        Cursor cursor = database.query(DicDatabaseHelper.DicTable.TABLE_NAME,
                null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int wordInfoIndex = cursor.getColumnIndex(DicDatabaseHelper.DicTable.WORD_INFO);
                String wordInfo = cursor.getString(wordInfoIndex);
                wordBeanList.add(new Gson().fromJson(wordInfo, Translation.class));
            }
        }

        cursor.close();
        database.close();

        return wordBeanList;
    }
}
