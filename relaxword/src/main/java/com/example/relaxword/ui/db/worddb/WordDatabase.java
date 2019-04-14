package com.example.relaxword.ui.db.worddb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.relaxword.ui.bean.Word;

import java.util.ArrayList;
import java.util.List;

public class WordDatabase {
    private WordDatabaseHelper wordDatabaseHelper;

    public WordDatabase(Context context){
        wordDatabaseHelper = new WordDatabaseHelper(context);
    }

    public void insertWord(String word){
        SQLiteDatabase database = wordDatabaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WordDatabaseHelper.WordTable.WORD, word);
        contentValues.put(WordDatabaseHelper.WordTable.WORD_STATE, 0);
        database.insertOrThrow(WordDatabaseHelper.WordTable.TABLE_NAME, null, contentValues);

        database.close();
    }

    public List<Word> qureyAllWords(){
        List<Word> wordList = new ArrayList<>();
        SQLiteDatabase database = wordDatabaseHelper.getReadableDatabase();
        Cursor cursor = database.query(WordDatabaseHelper.WordTable.TABLE_NAME, null, null, null, null, null, null);
        if(cursor == null) return wordList;

        while (cursor.moveToNext()){
            int spellIndex = cursor.getColumnIndex(WordDatabaseHelper.WordTable.WORD);
            int stateIndex = cursor.getColumnIndex(WordDatabaseHelper.WordTable.WORD_STATE);
            String spell = cursor.getString(spellIndex);
            int state = cursor.getInt(stateIndex);
            wordList.add(new Word(spell, state));
        }

        cursor.close();
        database.close();
        return wordList;
    }
}
