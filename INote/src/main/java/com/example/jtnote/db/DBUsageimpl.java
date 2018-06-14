package com.example.jtnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jtnote.bean.NoteItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/14 0014.
 */

public class DBUsageimpl implements DBUsageInterface{
    private NoteDBHelper noteDBHelper;

    public DBUsageimpl(Context context){
        noteDBHelper = new NoteDBHelper(context);
    }

    @Override
    public List<NoteItem> queryAllNotes() {
        SQLiteDatabase database = noteDBHelper.getReadableDatabase();
        Cursor cursor = database.query(DBTables.NoteItemTable.TABLE_NAME, null, null, null, null,null, DBTables.NoteItemTable._ID + " ASC");
        try {
            return readAllNotes(cursor);
        }finally {
            if(cursor != null){
                cursor.close();
            }
            database.close();
        }
    }

    @Override
    public void insertNote(NoteItem noteItem) {
        SQLiteDatabase database = noteDBHelper.getWritableDatabase();
        try {
            database.insert(DBTables.NoteItemTable.TABLE_NAME, null, buildNoteItemValue(noteItem));
        }finally {
            database.close();
        }
    }

    @Override
    public void removeNote(NoteItem noteItem) {
        SQLiteDatabase database = noteDBHelper.getWritableDatabase();
        try {
            database.delete(DBTables.NoteItemTable.TABLE_NAME, DBTables.NoteItemTable.TEXT_CONTENT + " =?", new String[]{noteItem.getTextContent()});
        }finally {
            database.close();
        }
    }

    private ContentValues buildNoteItemValue(NoteItem noteItem){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBTables.NoteItemTable.TEXT_CONTENT, noteItem.getTextContent());
        contentValues.put(DBTables.NoteItemTable.AUDIO_CONTENT, noteItem.getAudioContent());
        contentValues.put(DBTables.NoteItemTable.IMAGE_CONTENT, noteItem.getImageContent());
        contentValues.put(DBTables.NoteItemTable.VIDEO_CONTENT, noteItem.getVideoContent());
        return contentValues;
    }

    private List<NoteItem> readAllNotes(Cursor cursor){
        if(cursor == null) return null;
        List<NoteItem> noteItemList = new ArrayList<>();

        int textContentIndex = cursor.getColumnIndex(DBTables.NoteItemTable.TEXT_CONTENT);
        int audioContentIndex = cursor.getColumnIndex(DBTables.NoteItemTable.AUDIO_CONTENT);
        int imageContentIndex = cursor.getColumnIndex(DBTables.NoteItemTable.IMAGE_CONTENT);
        int videoContentIndex = cursor.getColumnIndex(DBTables.NoteItemTable.VIDEO_CONTENT);
        int alarmTimeIndex = cursor.getColumnIndex(DBTables.NoteItemTable.ALARM_TIME);
        int createTimeIndex = cursor.getColumnIndex(DBTables.NoteItemTable.CREATE_TIME);

        while (cursor.moveToNext()){
            String textContent = cursor.getString(textContentIndex);
            String audioContent = cursor.getString(audioContentIndex);
            String imageContent = cursor.getString(imageContentIndex);
            String videoContent = cursor.getString(videoContentIndex);
            long alarmTime = cursor.getLong(alarmTimeIndex);
            long createTime = cursor.getLong(createTimeIndex);

            NoteItem noteItem = new NoteItem();
            noteItem.setTextContent(textContent);
            noteItem.setImageContent(imageContent);
            noteItem.setAudioContent(audioContent);
            noteItem.setVideoContent(videoContent);
            noteItem.setAlarmTime(alarmTime);
            noteItem.setCreatTime(createTime);
            noteItemList.add(noteItem);
        }

        return noteItemList;
    }
}
