package com.example.spider.dictionary;

import android.support.annotation.NonNull;

import com.example.spider.annotation.FieldPath;

public class SampleSentence {
    @FieldPath(".eg.deg")
    public String enSentence;

    @FieldPath(".trans.dtrans.dtrans-se.hdb")
    public String znSentence;

    @NonNull
    @Override
    public String toString() {
        return enSentence + "\n" + znSentence;
    }
}
