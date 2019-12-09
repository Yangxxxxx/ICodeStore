package com.example.spider;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spider.bean.WordDB;
import com.example.spider.bean.WordState;
import com.example.spider.db.SpiderDatabase;
import com.example.spider.dictionary.Word;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CheckWordConsistent extends RelativeLayout {
    private HashMap<String, WordDB> words = new HashMap<>();
    private SpiderDatabase spiderDatabase;

    private TextView contentView;
    private TextView confirmView;

    public CheckWordConsistent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = findViewById(R.id.content_view);
        confirmView = findViewById(R.id.confirm_view);

        contentView.setText(genString());
        confirmView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

        findViewById(R.id.copy_view).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(getContext(), "准备拷贝",Toast.LENGTH_SHORT).show();
                    Utils.copyFileUsingFileStreams(getContext().getDatabasePath("dictionary.db"), new File("/sdcard/spider_dic.db"));

                    Toast.makeText(getContext(), "拷贝完成",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void init(){
        spiderDatabase = new SpiderDatabase(getContext());
        words.putAll(spiderDatabase.queryNotConsistentWords());
    }

    private void check(){
        for (String key: words.keySet() ){
           WordDB wordDB = words.get(key);
           if((wordDB.state == WordState.NO_CONTENT) || (wordDB.state == WordState.BLANK)){
               spiderDatabase.deleteWord(key);
               Log.e("Check", "delete " + key);
           }
           if(wordDB.state == WordState.NOT_SAME){
               spiderDatabase.updateWord(key, wordDB.name, WordState.NORMAL);
               Log.e("Check", "update " + key + "->" + wordDB.name);
           }
        }
    }

    private String genString(){
        StringBuilder builder = new StringBuilder();
        for (String key: words.keySet() ){
            builder.append(key).append(" : ").append(words.get(key).name).append("\n");
        }
        return builder.toString();
    }

}
