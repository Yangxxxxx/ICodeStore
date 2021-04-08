package com.youdao.sdk.ydtranslatedemo.dictionarydb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import androidx.annotation.Nullable;

public class DicDatabaseHelper extends SQLiteOpenHelper {
    public static String dbName = "dictionary_with_word.db";
    private static int dbVersion = 1;

    public DicDatabaseHelper(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createWordTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void createWordTable(SQLiteDatabase database){
        String sql = "CREATE TABLE " + DicTable.TABLE_NAME + " ( "+
                DicTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DicTable.WORD + " TEXT," +
                DicTable.WORD_INFO + " TEXT" +
//                WordTable.WORD_STATE + " INTEGER" +
                ")";

        database.execSQL(sql);
    }

    public static class DicTable implements BaseColumns {
        public static String TABLE_NAME = "table_name";
        public static String WORD_INFO = "word_info";
        public static String WORD = "word";
    }
}
