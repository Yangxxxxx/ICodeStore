package com.example.jtnote.db;

import com.example.jtnote.bean.NoteItem;

import java.util.List;

/**
 * Created by Administrator on 2018/6/14 0014.
 */

public interface DBUsageInterface {
    List<NoteItem> queryAllNotes();
    void insertNote(NoteItem noteItem);
    void removeNote(NoteItem noteItem);
}
