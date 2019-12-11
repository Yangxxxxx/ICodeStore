package com.example.spider;

import android.Manifest;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spider.bean.WordState;
import com.example.spider.dictionaryDB.SpiderDatabase;
import com.example.spider.dictionary.Word;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static String NET_URL = "https://dictionary.cambridge.org/dictionary/english-chinese-simplified/";

    private HtmlParserManager htmlParserManager = new HtmlParserManager();
    private boolean stop = true;
    private ArrayList<String> words = new ArrayList<>();
    Gson gson = new Gson();
    private SpiderDatabase spiderDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.search_view).setOnClickListener(this);
        findViewById(R.id.tv_start).setOnClickListener(this);
        findViewById(R.id.tv_copy_db).setOnClickListener(this);

        spiderDatabase = new SpiderDatabase(this);
        words.addAll(spiderDatabase.queryAllBlankWord());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.search_view){
            EditText editText = findViewById(R.id.input_view);
            String inputText = editText.getText().toString();
            if(!TextUtils.isEmpty(inputText)){
                TextView textView = findViewById(R.id.content_view);
                textView.setText("searching...");

                htmlParserManager.parser(NET_URL + inputText, Word.class, new HtmlParserManager.ParserCallBack<Word>() {
                    @Override
                    public void onResult(Word word) {
                        showWordInfo(word);
                    }
                });
            }
        }else if(id == R.id.tv_start){
            stop = !stop;
            requestOneWord();
        }else if(id == R.id.tv_copy_db){
            try {
                Toast.makeText(this, "开始拷贝", Toast.LENGTH_SHORT).show();
                Utils.copyFileUsingFileStreams(getDatabasePath("dictionary.db"), new File("/sdcard/spider_dic.db"));
                Toast.makeText(this, "拷贝完成", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop = true;
    }

    private void requestOneWord(){
        if(stop) return;
        if(words.size() == 0) return;
        final String org = words.remove(0);
        final String item = org.replace("?", " ");
        htmlParserManager.parser(NET_URL + item, Word.class, new HtmlParserManager.ParserCallBack<Word>() {
            @Override
            public void onResult(Word word) {
                if(stop) return;
                showWordInfo(word);

                int state;
                if(org.equals(word.name)){
                    state = WordState.NORMAL;

                    spiderDatabase.updateExplain(org, gson.toJson(word), state);
                }else if(TextUtils.isEmpty(word.name)){
                    state = WordState.NO_CONTENT;

                    spiderDatabase.deleteWord(org);
                    Log.e("yang", "delete word: " + org);
                }else {
                    state = WordState.NOT_SAME;

                    spiderDatabase.updateExplain(org, gson.toJson(word), state);
                    spiderDatabase.updateWord(org, word.name, WordState.NORMAL);


                    Log.e("yang", "change word: " + org + "->" + word.name);
                }

//                spiderDatabase.updateExplain(item, gson.toJson(word), state);

                requestOneWord();
            }
        });
    }

    private void showWordInfo(final Word  word){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = findViewById(R.id.content_view);
                textView.setText("剩余： "+words.size() + "\n");
                textView.append(word.toString());
            }
        });
    }
}
