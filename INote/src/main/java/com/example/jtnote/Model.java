package com.example.jtnote;

import com.example.jtnote.bean.NoteItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class Model {
    private List<NoteItem> noteItemList = new ArrayList<>();
    private List<OnNoteChangeListener> onNoteChangeListeners = new ArrayList<>();

    private static Model model = new Model();
    private Model(){
    }
    public static Model getInstance(){
        return model;
    }

    public void init(){
//        getNoteContent_debug();
    }

    public void insertNote(NoteItem noteItem){
        noteItemList.add(noteItem);
        invokeNoteChangeListener();
    }

    public void deleteNote(NoteItem noteItem){
        noteItemList.remove(noteItem);
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
