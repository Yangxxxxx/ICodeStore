package com.example.jtnote;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.jtnote.bean.NoteItem;
import com.example.jtnote.db.DBUsageImplNew;
import com.example.jtnote.db.DBUsageInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class Model {
    private final String DB_NAME = "INote_DB";

    private List<NoteItem> noteItemList = new ArrayList<>();
    private List<OnNoteChangeListener> onNoteChangeListeners = new ArrayList<>();
    private DBUsageInterface dbUsageInterface;

    private static Model model = new Model();
    private Model(){
    }
    public static Model getInstance(){
        return model;
    }

    public void init(Context context){
//        getNoteContent_debug();

//        dbUsageInterface = new DBUsageimpl(context);
        dbUsageInterface = Room.databaseBuilder(context, DBUsageImplNew.class, DB_NAME)
                .addMigrations(DBUsageImplNew.migration1_2)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        noteItemList.addAll(dbUsageInterface.queryAllNotes());
    }

    public void insertNote(NoteItem noteItem){
        noteItemList.add(noteItem);
        dbUsageInterface.insertNote(noteItem);
        invokeNoteChangeListener();
    }

    public void deleteNote(NoteItem noteItem){
        noteItemList.remove(noteItem);
        dbUsageInterface.removeNote(noteItem);
        invokeNoteChangeListener();
    }

    public void addNoteChangeListener(OnNoteChangeListener listener){
        onNoteChangeListeners.add(listener);
        invokeNoteChangeListener();
    }

    public void removeNoteChangeListener(OnNoteChangeListener listener){
        onNoteChangeListeners.remove(listener);
    }

    private void invokeNoteChangeListener(){
        for(OnNoteChangeListener listener: onNoteChangeListeners){
            listener.onNoteChanged(noteItemList);
        }
    }

    private void getNoteContent_debug(){
        noteItemList.add(new NoteItem("aaaaaaaaa", 1));
        noteItemList.add(new NoteItem("bbbbbbbbb", 2));
        noteItemList.add(new NoteItem("ccccccccc", 3));
        noteItemList.add(new NoteItem("ddddddddd", 4));
        noteItemList.add(new NoteItem("eeeeeeeee", 5));
    }

    public interface OnNoteChangeListener{
        void onNoteChanged(List<NoteItem> noteItemList);
    }
}
