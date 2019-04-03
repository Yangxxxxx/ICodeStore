package com.youdao.sdk.ydtranslatedemo;


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
import com.youdao.sdk.ydtranslatedemo.utils.ToastUtils;

import java.util.List;

public class TranslateLibActivity extends Activity {
    private Translator translator;
    private int worldIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryStart();
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


}
