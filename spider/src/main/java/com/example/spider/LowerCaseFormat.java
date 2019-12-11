package com.example.spider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spider.dictionaryDB.SpiderDatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LowerCaseFormat extends RelativeLayout {
    private ArrayList<String> words = new ArrayList<>();
    private SpiderDatabase spiderDatabase;

    private TextView contentView;
    private TextView confirmView;

    public LowerCaseFormat(Context context, AttributeSet attrs) {
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
        words.addAll(spiderDatabase.queryAllWord());
    }

    private void check(){
        for (String item: words){
            String lowerCaseStr = item.toLowerCase();
            if(!item.equals(lowerCaseStr)) {
                spiderDatabase.updateWord(item, item.toLowerCase(), -1);
            }
        }
    }

    private String genString(){
        StringBuilder builder = new StringBuilder();
        for (String item: words){
            String lowerCaseStr = item.toLowerCase();
            if(!item.equals(lowerCaseStr)) {
                builder.append(item).append("\n");
            }
        }
        return builder.toString();
    }

}
