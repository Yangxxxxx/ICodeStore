package com.example.jtnote.ui.MainPage;

import com.example.jtnote.Model;
import com.example.jtnote.bean.NoteItem;
import com.example.jtnote.ui.KeyboardActivity;

import java.util.List;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class MainPagePresenter implements MainPageContract.Presenter, Model.OnNoteChangeListener{
    private MainPageContract.View view;
    private Model model;

    public MainPagePresenter(MainPageContract.View view){
        this.view = view;
        model = Model.getInstance();
        model.addNoteChangeListener(this);
    }

    @Override
    public void textEntryClick() {
    }

    @Override
    public void newTextContent(String content) {
        model.insertNote(new NoteItem(content, 0));
    }

    @Override
    public void onDestory() {
        model.removeNoteChangeListener(this);
    }

    @Override
    public void onNoteChanged(List<NoteItem> noteItemList) {
        view.notesChange(noteItemList);
    }
}
