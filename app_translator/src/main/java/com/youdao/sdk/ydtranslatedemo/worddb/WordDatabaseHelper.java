package com.youdao.sdk.ydtranslatedemo.worddb;

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
    public void onCreate(SQLiteDatabase db) {
        createWordTabe(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createWordTabe(SQLiteDatabase database){
        String sqlStr = "create table " + WordTable.TABLE_NAME + " ( "+
                WordTable._ID + " INTEGER primary key autoincrement," +
                WordTable.WORD + " TEXT," +
                WordTable.WORD_STATE + " INTEGER"
                +" )";
        database.execSQL(sqlStr);
    }


    public static class WordTable implements BaseColumns{
        public static final String TABLE_NAME = "table_name";
        public static String WORD = "word";
        public static String WORD_STATE = "word_state";
    }
}
