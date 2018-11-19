package com.example.jtnote.ui.MainPage;

import com.example.jtnote.bean.NoteItem;

import java.util.List;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class MainPageContract {
    public interface Presenter{
        void textEntryClick();
        void newTextContent(String content, boolean goMorePage);
        void noteItemClick(NoteItem noteItem);
        void deleteSelectNotes();
        void turnDeleteMode();
        boolean isNoteSelected(NoteItem noteItem);
        boolean onBackPress();
        void onDestory();
        void onItemPosChange(int orgPos, int targetPos);
    }

    public interface View{
        void notesChange(List<NoteItem> noteItemList);
        void selecteNotesChange(List<NoteItem> noteItemList);
        void turnNormalMode();
        void turnDeleteMode();
    }
}
