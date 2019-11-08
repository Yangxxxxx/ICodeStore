package com.example.spider.dictionary;

import android.support.annotation.NonNull;

import com.example.spider.annotation.FieldPath;

public class Word {
    @FieldPath("hw dhw")
    public String name;

    @FieldPath("uk dpron-i >pron dpron")
    public String ukPhonetic;

    @FieldPath("us dpron-i >pron dpron")
    public String usPhonetic;

    @FieldPath("sense-body dsense_b")
    public Translation[] translations;

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append("\n");
        builder.append("uk:" + ukPhonetic + "  us:" + usPhonetic);
        builder.append("\n");
        if(translations != null){
            for (Translation item: translations){
                builder.append(item.toString());
                builder.append("\n\n\n");
            }
        }

        return builder.toString();
    }
}
