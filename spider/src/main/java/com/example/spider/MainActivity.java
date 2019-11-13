package com.example.spider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.spider.annotation.ClassPath;
import com.example.spider.annotation.FieldPath;
import com.example.spider.dictionary.Translation;
import com.example.spider.dictionary.Word;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static String NET_URL = "https://dictionary.cambridge.org/zhs/%E8%AF%8D%E5%85%B8/%E8%8B%B1%E8%AF%AD-%E6%B1%89%E8%AF%AD-%E7%AE%80%E4%BD%93/";

    private HtmlParserManager htmlParserManager = new HtmlParserManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.search_view).setOnClickListener(this);
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
        }
    }

    private void showWordInfo(final Word  word){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = findViewById(R.id.content_view);
                textView.setText(word.toString());
            }
        });
    }
}
