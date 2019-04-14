package com.example.relaxword.ui;

import android.content.Context;
import android.util.Log;

import com.example.relaxword.R;
import com.example.relaxword.ui.bean.Translation;
import com.example.relaxword.ui.bean.Word;
import com.example.relaxword.ui.db.dictionarydb.DicDatabase;
import com.example.relaxword.ui.db.dictionarydb.DicDatabaseHelper;
import com.example.relaxword.ui.db.worddb.WordDatabase;
import com.example.relaxword.ui.db.worddb.WordDatabaseHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private static final String TAG = "Model";
    private Context context;
    private WordDatabase wordDatabase;
    private DicDatabase dicDatabase;

    private List<Word> allWordList = new ArrayList<>();
    private List<Translation> allTranslatonList = new ArrayList<>();


    private static Model model = new Model();
    private Model(){}
    public static Model getInstance(){
        return model;
    }

    public void init(Context context){
        this.context = context;
        allWordList.clear();
        allTranslatonList.clear();

        copyData2Local();

        wordDatabase = new WordDatabase(context);
        dicDatabase = new DicDatabase(context);

        long preTime = System.currentTimeMillis();

        allWordList.addAll(wordDatabase.qureyAllWords());

        Log.e(TAG, "qureyAllWords: " + (System.currentTimeMillis() - preTime));

        allTranslatonList.addAll(dicDatabase.qureyAllTranslation());

        Log.e(TAG, "qureyAllTranslation: " + (System.currentTimeMillis() - preTime));


        for(Word item: allWordList){
            int index = allTranslatonList.indexOf(item);
            if(index >= 0){
                item.setTranslation(allTranslatonList.get(index));
            }
        }

        Log.e(TAG, "setTranslation: " + (System.currentTimeMillis() - preTime));

    }

    public List<Word> getAllWord(){
        return allWordList;
    }


    private void copyData2Local(){
        copyRaw2Database(R.raw.wordlist, WordDatabaseHelper.dbName);
        copyRaw2Database(R.raw.dictionary_with_word, DicDatabaseHelper.dbName);
    }

    private void copyRaw2Database(int rawId, String databaseName){
        File localWordListDBFile = context.getDatabasePath(databaseName);
        InputStream rawStream = context.getResources().openRawResource(rawId);
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
