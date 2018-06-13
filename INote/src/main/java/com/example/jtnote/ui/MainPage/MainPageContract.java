package com.example.jtnote.ui.MainPage;

import com.example.jtnote.bean.NoteItem;

import java.util.List;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class MainPageContract {
    public interface Presenter{
        void textEntryClick();
        void newTextContent(String content);
        void onDestory();
    }

    public interface View{
        void notesChange(List<NoteItem> noteItemList);
    }
}
