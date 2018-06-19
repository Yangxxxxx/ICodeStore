package com.example.jtnote.ui.MainPage;

import com.example.jtnote.Model;
import com.example.jtnote.bean.NoteItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class MainPagePresenter implements MainPageContract.Presenter, Model.OnNoteChangeListener{
    private MainPageContract.View view;
    private Model model;
    private List<NoteItem> selectNotes = new ArrayList<>();
    private boolean isDeleteMode;

    public MainPagePresenter(MainPageContract.View view){
        this.view = view;
        model = Model.getInstance();
        model.addNoteChangeListener(this);
        view.turnNormalMode();
    }

    @Override
    public void textEntryClick() {
    }

    @Override
    public void newTextContent(String content) {
        model.insertNote(new NoteItem(content, 0));
    }

    @Override
    public void noteItemClick(NoteItem noteItem) {
        if(isDeleteMode){
            if(selectNotes.contains(noteItem)){
                selectNotes.remove(noteItem);
            }else {
                selectNotes.add(noteItem);
            }
            view.selecteNotesChange(selectNotes);
            if(selectNotes.size() == 0){
                turnNormalMode();
            }

        }else {

        }
    }

    @Override
    public void deleteSelectNotes() {
        for (NoteItem noteItem: selectNotes){
            model.deleteNote(noteItem);
        }
        turnNormalMode();
    }

    @Override
    public void turnDeleteMode() {
        if(isDeleteMode) return;
        isDeleteMode = true;
        selectNotes.clear();
        view.turnDeleteMode();
    }

    @Override
    public boolean isNoteSelected(NoteItem noteItem) {
        return selectNotes.contains(noteItem);
    }

    @Override
    public boolean onBackPress() {
        if(!isDeleteMode) return false;
        turnNormalMode();
        return true;
    }

    @Override
    public void onDestory() {
        model.removeNoteChangeListener(this);
    }

    @Override
    public void onNoteChanged(List<NoteItem> noteItemList) {
        view.notesChange(noteItemList);
    }

    private void turnNormalMode(){
        isDeleteMode = false;
        selectNotes.clear();
        view.turnNormalMode();
        view.selecteNotesChange(selectNotes);
    }
}
