package com.example.spider.dictionary;

import android.support.annotation.NonNull;

import com.example.spider.annotation.ClassPath;
import com.example.spider.annotation.FieldPath;

public class Translation {

    @FieldPath("def ddef_d db")
    public String enExplanation;

    @FieldPath("trans dtrans dtrans-se ")
    public String zhExplanation;

    @FieldPath("examp dexamp")
    public SampleSentence[] sentences;

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(enExplanation);
        builder.append("\n");
        builder.append(zhExplanation);
        builder.append("\n\n");

        if(sentences != null) {
            for (SampleSentence item : sentences) {
                builder.append(item.toString());
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}
