package com.example.relaxword.ui.db.dictionarydb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

public class DicDatabaseHelper extends SQLiteOpenHelper {
    public static String dbName = "dictionary_with_word.db";
    private static int dbVersion = 1;

    public DicDatabaseHelper(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public static class DicTable implements BaseColumns {
        public static String TABLE_NAME = "table_name";
        public static String WORD_INFO = "word_info";
        public static String WORD = "word";
    }
}
