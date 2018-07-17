package com.example.jtnote.ui.MainPage;

import android.content.Context;

import com.example.jtnote.Model;
import com.example.jtnote.bean.NoteItem;
import com.example.jtnote.ui.DetailPage.DetialActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class MainPagePresenter implements MainPageContract.Presenter, Model.OnNoteChangeListener{
    private Context context;
    private MainPageContract.View view;
    private Model model;
    private List<NoteItem> selectNotes = new ArrayList<>();
    private boolean isDeleteMode;

    public MainPagePresenter(Context context, MainPageContract.View view){
        this.view = view;
        this.context = context;
        model = Model.getInstance();
        model.addNoteChangeListener(this);
        view.turnNormalMode();
    }

    @Override
    public void textEntryClick() {
    }

    @Override
    public void newTextContent(String content, boolean goMorePage) {
        NoteItem noteItem = new NoteItem(model.genUniqueID(), content, 0);
        model.insertNote(noteItem);
        if(goMorePage){
            DetialActivity.start(context, noteItem);
        }
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
            DetialActivity.start(context, noteItem);
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
