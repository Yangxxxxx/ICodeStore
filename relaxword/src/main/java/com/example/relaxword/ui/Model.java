package com.example.relaxword.ui;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
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
    private static final int NUM_PER_LOAD = 10;
    private Context context;
    private HandlerThread handlerThread = new HandlerThread("model_thread");
    private Handler workHandler;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private WordDatabase wordDatabase;
    private DicDatabase dicDatabase;

    private List<Word> allWordList = new ArrayList<>();
    private List<Translation> allTranslatonList = new ArrayList<>();
    private List<LoadWordListener> loadWordListenerList = new ArrayList<>();

    private boolean isLoadingWord;


    private static Model model = new Model();
    private Model(){}
    public static Model getInstance(){
        return model;
    }


    public void init(Context context){
        this.context = context;
        allWordList.clear();
        allTranslatonList.clear();
        handlerThread.start();
        workHandler = new Handler(handlerThread.getLooper());

        copyData2Local();

        wordDatabase = new WordDatabase(context);
        dicDatabase = new DicDatabase(context);

        long preTime = System.currentTimeMillis();
        allWordList.addAll(wordDatabase.qureyAllWords());
        Log.e(TAG, "qureyAllWords: " + (System.currentTimeMillis() - preTime));
    }

    public void loadNextPageWord(){
        if(isLoadingWord) return;
        final long preTime = System.currentTimeMillis();
        isLoadingWord = true;
        workHandler.post(new Runnable() {
            @Override
            public void run() {
                final List<Word> selectList = new ArrayList<>();
                for (int i = 0; i < NUM_PER_LOAD; i++){
                    int pos = (int)(Math.random() * allWordList.size());
                    selectList.add(allWordList.get(pos));
                    allWordList.remove(pos);
                }
                dicDatabase.qureyTranslation(selectList);

                Log.e(TAG, "loadNextPageWord: " + (System.currentTimeMillis() - preTime));

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for(LoadWordListener item: loadWordListenerList){
                            item.onWordsLoaded(selectList);
                        }
                        isLoadingWord = false;
                    }
                });

            }
        });

    }

    public List<Word> getAllWord(){
        return allWordList;
    }

    public void addLoadWordListener(LoadWordListener listener){
        loadWordListenerList.add(listener);
    }

    public void removeLoadWordListener(LoadWordListener listener){
        loadWordListenerList.remove(listener);
    }

    public Handler getMainHandler(){
        return mainHandler;
    }

    public Handler getWorkHandler(){
        return workHandler;
    }

    public interface LoadWordListener{
        void onWordsLoaded(List<Word> list);
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
