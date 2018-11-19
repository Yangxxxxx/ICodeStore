package com.example.jtnote;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.example.jtnote.bean.NoteItem;
import com.example.jtnote.db.DBUsageImplNew;
import com.example.jtnote.db.DBUsageInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class Model {
    private final String DB_NAME = "INote_DB";

    private HandlerThread handlerThread;
    private Handler workHanlder;

    private List<NoteItem> noteItemList = new ArrayList<>();
    private List<OnNoteChangeListener> onNoteChangeListeners = new ArrayList<>();
    private DBUsageInterface dbUsageInterface;
    private INoteSharePreference iNoteSharePreference;

    private static Model model = new Model();
    private Model(){
        initData();
    }
    public static Model getInstance(){
        return model;
    }

    public void initModel(final Context context){
        runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                dbUsageInterface = Room.databaseBuilder(context, DBUsageImplNew.class, DB_NAME)
                        .addMigrations(DBUsageImplNew.migration1_2)
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
                noteItemList.addAll(dbUsageInterface.queryAllNotes());
                sortNoteList();
                INoteApplication.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invokeNoteChangeListener();
                    }
                });
            }
        });

    }

    public void initData(){
        handlerThread = new HandlerThread("workThread");
        handlerThread.start();
        workHanlder = new Handler(handlerThread.getLooper());
        iNoteSharePreference = INoteSharePreference.getInstance();
    }

    public List<NoteItem> getAllNotes(){
        return noteItemList;
    }

    public void insertNote(NoteItem noteItem){
        noteItemList.add(noteItem);
        dbUsageInterface.insertNote(noteItem);
        iNoteSharePreference.setNoteListOrder(noteItemList);
        invokeNoteChangeListener();
    }

    public void deleteNote(NoteItem noteItem){
        noteItemList.remove(noteItem);
        dbUsageInterface.removeNote(noteItem);
        iNoteSharePreference.setNoteListOrder(noteItemList);
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

    public void onItemPosChange(int orgPos, int targetPos) {
        NoteItem orgItem = noteItemList.remove(orgPos);
        noteItemList.add(targetPos, orgItem);
        iNoteSharePreference.setNoteListOrder(noteItemList);
    }

    public interface OnNoteChangeListener{
        void onNoteChanged(List<NoteItem> noteItemList);
    }

    private void runOnWorkThread(Runnable runnable){
        workHanlder.post(runnable);
    }

    private void sortNoteList(){
        String[] orderArr = INoteSharePreference.getInstance().getNoteListOrder();
        final HashMap<String, Integer> orderMap = new HashMap<>();
        for (int i = 0; i < orderArr.length; i++){
            orderMap.put(orderArr[i], i);
        }

        Collections.sort(noteItemList, new Comparator<NoteItem>() {
            @Override
            public int compare(NoteItem o1, NoteItem o2) {
                return orderMap.get(String.valueOf(o1.getId())) - orderMap.get(String.valueOf(o2.getId()));
            }
        });
    }
}
