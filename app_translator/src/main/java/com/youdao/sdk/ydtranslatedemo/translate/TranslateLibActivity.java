package com.youdao.sdk.ydtranslatedemo.translate;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.common.Constants;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;
import com.youdao.sdk.ydtranslate.WebExplain;
import com.youdao.sdk.ydtranslatedemo.R;
import com.youdao.sdk.ydtranslatedemo.dictionarydb.DicDatabase;
import com.youdao.sdk.ydtranslatedemo.dictionarydb.DicDatabaseHelper;
import com.youdao.sdk.ydtranslatedemo.utils.ToastUtils;
import com.youdao.sdk.ydtranslatedemo.worddb.WordDatabase;
import com.youdao.sdk.ydtranslatedemo.worddb.WordDatabaseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class TranslateLibActivity extends Activity implements View.OnClickListener {
    private Translator translator;
    private int worldIndex = 0;
    private DicDatabase dicDatabase;
    private WordDatabase wordDatabase;

    private List<String> excelList = new ArrayList<>();
    private List<WordBean> dbWordBeanList;

    TextView wordView;
    TextView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translatelib);
        wordView = (TextView) findViewById(R.id.tv_word);
        contentView = (TextView) findViewById(R.id.tv_content);
        findViewById(R.id.tv_start_trans).setOnClickListener(this);
        findViewById(R.id.tv_start_copy).setOnClickListener(this);
        findViewById(R.id.tv_start_word_database).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_start_trans:
                dicDatabase = new DicDatabase(this);
                dbWordBeanList = dicDatabase.qureyWords();
                Log.e("yang", "wordList db: " + dbWordBeanList.size());

                try {
                    coverToXml();
                } catch (BiffException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                queryStart();

                break;
            case R.id.tv_start_copy:
                copyDicDatabase2SDCard(DicDatabaseHelper.dbName);
                Toast.makeText(this, "start....", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tv_start_word_database:
                wordDatabase = new WordDatabase(this);
                try {
                    coverToXml();
                } catch (BiffException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for(String item: excelList){
                    wordDatabase.insertWord(item);
                }

                copyDicDatabase2SDCard(WordDatabaseHelper.dbName);
                Toast.makeText(this, "start....", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void copyDicDatabase2SDCard(String dbName){
        File dbFile = getDatabasePath(dbName);
        File sdFile = new File("/sdcard/"+dbName);
        try {
            copyFileUsingFileStreams(dbFile, sdFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void copyFileUsingFileStreams(File source, File dest)
            throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }

    private void queryStart() {
        if (isDestroyed() || isFinishing()) {
            Log.e("yang", "task cancled by activity finish");
            return;
        }


        for (int i = worldIndex; i < excelList.size(); i++) {
            String world = excelList.get(worldIndex);
            worldIndex++;

            if (isExistInDB(world)) {
                Log.e("yang", "database has exist");
                continue;
            }

            queryWorld(world);
            break;
        }
    }


    public void coverToXml() throws BiffException, IOException {

        File mExcelFile = new File("/sdcard/worlds.xls");
        if(!mExcelFile.exists()) return;
        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setEncoding("ISO-8859-1");
        Workbook workbook = Workbook.getWorkbook(mExcelFile, workbookSettings);
        Sheet[] sheets = workbook.getSheets();
        if (sheets != null) {
            for (Sheet s : sheets) {
                int columnNumber = s.getColumns();
                if (columnNumber > 0) {
                    Cell[] keys = s.getColumn(0);
                    Log.e("yang", "size: " + keys.length);
                    for(Cell item: keys){
                        excelList.add(item.getContents());
                        Log.e("yang", item.getContents() +" ");
                    }
                }
            }
        }
    }







    private void queryWorld(final String world) {


        String from = "英文";
        String to = "中文";
        final String input = world;

        Language langFrom = LanguageUtils.getLangByName(from);
        // 若设置为自动，则查询自动识别源语言，自动识别不能保证完全正确，最好传源语言类型
        // Language langFrosm = LanguageUtils.getLangByName("自动");
        Language langTo = LanguageUtils.getLangByName(to);

        TranslateParameters tps = new TranslateParameters.Builder()
                .source("youdao").from(langFrom).to(langTo).sound(Constants.SOUND_OUTPUT_MP3).voice(Constants.VOICE_BOY_UK).timeout(3000).build();// appkey可以省
        final long start = System.currentTimeMillis();

        translator = Translator.getInstance(tps);
        translator.lookup(input, "requestId", new TranslateListener() {
            @Override
            public void onResult(final Translate result, String input, String requestId) {
//                Log.e("yang", world + " result: " + result.getExplains().toString() + "\n");

                WordBean wordBean = translateConver2WordBean(result);
                dicDatabase.insertWord(wordBean);
                dbWordBeanList.add(wordBean);
                showWord(wordBean);

//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                queryStart();
            }

            @Override
            public void onError(final TranslateErrorCode error, String requestId) {
                ToastUtils.show("查询错误:" + error.name());
            }
            @Override
            public void onResult(List<Translate> results, List<String> inputs, List<TranslateErrorCode> errors, String requestId) {

            }
        });
    }

    private WordBean translateConver2WordBean(Translate translate){
        WordBean wordBean = new WordBean();
        wordBean.setWord(translate.getQuery());
        wordBean.setMeanings(translate.getExplains());
        wordBean.setWebMeanings(WebMeaningConvert(translate.getWebExplains()));
        wordBean.setPhoneticUK(translate.getUkPhonetic());
        wordBean.setPhoneticUS(translate.getUsPhonetic());
        wordBean.setVoiceUKurl(translate.getUKSpeakUrl());
        wordBean.setVoiceUSurl(translate.getUSSpeakUrl());
        return wordBean;
    }

    private List<WebMeaning> WebMeaningConvert(List<WebExplain> webExplainList){
        List<WebMeaning> webMeaningList = new ArrayList<>();
        if(webExplainList == null) return webMeaningList;

        for(WebExplain item: webExplainList){
            WebMeaning webMeaning = new WebMeaning();
            webMeaning.setKey(item.getKey());
            webMeaning.setMeanings(item.getMeans());
            webMeaningList.add(webMeaning);
        }
        return webMeaningList;
    }

    private void showWord(final WordBean wordBean){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String excelInfo = "excel list: " + excelList.size() +"\n";
                String dbInfo = "db List: " + dbWordBeanList.size() + "\n";
                wordView.setText(excelInfo + dbInfo + wordBean.getWord());
                contentView.setText(new Gson().toJson(wordBean));
            }
        });
    }

    private boolean isExistInDB(String word){
        for(WordBean wordBean: dbWordBeanList){
            if(wordBean.getWord().equals(word)) return true;
        }
        return false;
    }


}
