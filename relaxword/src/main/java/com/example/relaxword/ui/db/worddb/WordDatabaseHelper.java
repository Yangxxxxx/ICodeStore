package com.example.relaxword.ui.db.worddb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import androidx.annotation.Nullable;

public class WordDatabaseHelper extends SQLiteOpenHelper {
    public static final String dbName = "wordList.db";
    private static final int dbVersion = 1;


    public WordDatabaseHelper(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}


    public static class WordTable implements BaseColumns{
        public static final String TABLE_NAME = "table_name";
        public static String WORD = "word";
        public static String WORD_STATE = "word_state";
    }
}
