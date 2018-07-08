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

    public List<NoteItem> getAllNotes(){
        return noteItemList;
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

    public void updateNote(NoteItem noteItem){
        int index = -1;

        for (int i = 0; i < noteItemList.size(); i++){
            if(noteItemList.get(i).getId() == noteItem.getId()){
                index = i;
                break;
            }
        }

        if(index != -1){
            noteItemList.remove(index);
            noteItemList.add(index, noteItem);
            dbUsageInterface.updateNote(noteItem);
            invokeNoteChangeListener();
        }
    }

    public void noteStateChanged(){
        invokeNoteChangeListener();
    }

    public void addNoteChangeListener(OnNoteChangeListener listener){
        onNoteChangeListeners.add(listener);
        invokeNoteChangeListener();
    }

    public void removeNoteChangeListener(OnNoteChangeListener listener){
        onNoteChangeListeners.remove(listener);
    }

    public int genUniqueID(){
        int currMaxId = 0;
        for(NoteItem noteItem: noteItemList){
            if(noteItem.getId() > currMaxId) currMaxId = noteItem.getId();
        }
        return currMaxId + 1;
    }

    private void invokeNoteChangeListener(){
        for(OnNoteChangeListener listener: onNoteChangeListeners){
            listener.onNoteChanged(noteItemList);
        }
    }

    public interface OnNoteChangeListener{
        void onNoteChanged(List<NoteItem> noteItemList);
    }
}
