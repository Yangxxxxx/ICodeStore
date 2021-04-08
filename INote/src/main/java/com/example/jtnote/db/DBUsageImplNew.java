package com.example.jtnote.db;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.annotation.NonNull;

import com.example.jtnote.bean.NoteItem;
import com.example.jtnote.bean.NoteItemDao;

import java.util.List;

/**
 * Created by Administrator on 2018/6/20 0020.
 */

@Database(entities = {NoteItem.class}, version = 2)
public abstract class DBUsageImplNew extends RoomDatabase implements DBUsageInterface{
    @Override
    public List<NoteItem> queryAllNotes() {
        return getNoteItemDao().queryAllNotes();
    }

    @Override
    public void insertNote(NoteItem noteItem) {
        getNoteItemDao().insertNote(noteItem);
    }

    @Override
    public void removeNote(NoteItem noteItem) {
        getNoteItemDao().deleteNote(noteItem);
    }

    @Override
    public void updateNote(NoteItem noteItem) {
        getNoteItemDao().updateNote(noteItem);
    }

    public static Migration migration1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    abstract NoteItemDao getNoteItemDao();
}
