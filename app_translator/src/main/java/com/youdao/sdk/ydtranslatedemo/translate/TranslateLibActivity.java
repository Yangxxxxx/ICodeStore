package com.youdao.sdk.ydtranslatedemo.translate;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.common.Constants;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;
import com.youdao.sdk.ydtranslate.WebExplain;
import com.youdao.sdk.ydtranslatedemo.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class TranslateLibActivity extends Activity {
    private Translator translator;
    private int worldIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryStart();
        try {
            coverToXml();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void queryStart(){
        if(Constant.worldList.length > worldIndex) {
            String world = Constant.worldList[worldIndex];
            queryWorld(world);
            worldIndex++;
        }else {
            Log.e("yang", "jieshu");
        }
    }


    public void coverToXml() throws BiffException, IOException {

        File mExcelFile = new File("/sdcard/worlds.xls");
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
                    int i =0;
                    for(Cell item: keys){
                        Log.e("yang", item.getContents() +" ");
                        i++;
                        if(i % 30 == 0) Log.e("yang", "\n");
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
                Log.e("yang", world + " result: " + result.getExplains().toString() + "\n");
                queryStart();
                WordBean wordBean = translateConver2WordBean(result);
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
        for(WebExplain item: webExplainList){
            WebMeaning webMeaning = new WebMeaning();
            webMeaning.setKey(item.getKey());
            webMeaning.setMeanings(item.getMeans());
            webMeaningList.add(webMeaning);
        }
        return webMeaningList;
    }


}
