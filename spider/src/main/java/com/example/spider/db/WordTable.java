package com.example.spider.db;

import android.provider.BaseColumns;

public class WordTable implements BaseColumns {
    public static String TABLE_NAME = "word";

    public static  String NAME = "name";
    public static String EXPLAIN = "explain";
    public static String STATE = "state";

    public static String CREATE_TABLE_SQL = "create table " + TABLE_NAME +
            " (" +
            _ID + " integer PRIMARY KEY AUTOINCREMENT," +
            NAME + " text," +
            EXPLAIN + " text," +
            STATE + " integer" +
            " )";
}
