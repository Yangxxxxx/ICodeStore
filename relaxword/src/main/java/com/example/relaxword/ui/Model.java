package com.example.relaxword.ui;

import android.content.Context;

import com.example.relaxword.R;
import com.example.relaxword.ui.bean.Word;
import com.example.relaxword.ui.db.worddb.WordDatabase;
import com.example.relaxword.ui.db.worddb.WordDatabaseHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Model {
    private Context context;
    private WordDatabase wordDatabase;


    private static Model model = new Model();
    private Model(){}
    public static Model getInstance(){
        return model;
    }

    public void init(Context context){
        this.context = context;
        wordDatabase = new WordDatabase(context);
        copyData2Local();
    }

    public List<Word> getAllWord(){
        return wordDatabase.qureyAllWords();
    }


    private void copyData2Local(){
        File localWordListDBFile = context.getDatabasePath(WordDatabaseHelper.dbName);
        InputStream rawStream = context.getResources().openRawResource(R.raw.wordlist);
        if(!localWordListDBFile.exists()) {
            try {
                localWordListDBFile.getParentFile().mkdir();
                localWordListDBFile.createNewFile();
                OutputStream localStream = new FileOutputStream(localWordListDBFile);
                CommonUtils.copyFileUsingFileStreams(rawStream, localStream);
                rawStream.close();
                localStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
