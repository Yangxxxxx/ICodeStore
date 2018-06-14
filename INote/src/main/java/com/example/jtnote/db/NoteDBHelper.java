package com.example.jtnote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/6/14 0014.
 */

public class NoteDBHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "INote_DB";
    private static final int DB_VERSION = 1;

    public NoteDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createNoteItemTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createNoteItemTable(SQLiteDatabase db){
        String sql =
                "create table " + DBTables.NoteItemTable.TABLE_NAME +
                "(" +
                DBTables.NoteItemTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DBTables.NoteItemTable.TEXT_CONTENT + " TEXT,"+
                DBTables.NoteItemTable.AUDIO_CONTENT + " TEXT,"+
                DBTables.NoteItemTable.IMAGE_CONTENT + " TEXT,"+
                DBTables.NoteItemTable.VIDEO_CONTENT + " TEXT,"+
                DBTables.NoteItemTable.ALARM_TIME + " LONG,"+
                DBTables.NoteItemTable.CREATE_TIME + " LONG"+
                ")";
        db.execSQL(sql);
    }
}
