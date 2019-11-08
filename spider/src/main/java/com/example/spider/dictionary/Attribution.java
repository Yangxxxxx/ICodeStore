package com.example.spider.dictionary;

import android.support.annotation.NonNull;

import com.example.spider.annotation.FieldPath;

public class Attribution {
    @FieldPath("pos dpos")
    public String wordAttr; //词性：verb、noun等

    @FieldPath("def-block ddef_block ")
    public Translation[] translations;

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(wordAttr);
        builder.append("\n");
        if(translations != null){
            for (Translation item: translations){
                builder.append(item.toString());
                builder.append("\n\n");
            }
        }

        return builder.toString();
    }
}
