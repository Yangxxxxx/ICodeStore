package com.example.jtnote.bean;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.jtnote.db.DBTables;

import java.util.List;

/**
 * Created by Administrator on 2018/6/20 0020.
 */

@Dao
public interface NoteItemDao{
    @Query("select * from " + DBTables.NoteItemTable.TABLE_NAME + " order by " + DBTables.NoteItemTable._ID + " ASC")
    List<NoteItem> queryAllNotes();

    @Insert
    void insertNote(NoteItem noteItem);

    @Delete
    void deleteNote(NoteItem noteItem);
}
