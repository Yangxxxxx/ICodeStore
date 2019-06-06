package com.youdao.sdk.ydtranslatedemo.worddb;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class WordDatabase {
    private WordDatabaseHelper wordDatabaseHelper;

    public WordDatabase(Context context){
        wordDatabaseHelper = new WordDatabaseHelper(context);
    }

    public void insertWord(String word){
        SQLiteDatabase database = wordDatabaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WordDatabaseHelper.WordTable.WORD, word);
        contentValues.put(WordDatabaseHelper.WordTable.WORD_STATE, 0);
        database.insertOrThrow(WordDatabaseHelper.WordTable.TABLE_NAME, null, contentValues);

        database.close();
    }
}
