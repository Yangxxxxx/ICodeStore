package com.example.jtnote.bean;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Update
    void updateNote(NoteItem noteItem);
}
