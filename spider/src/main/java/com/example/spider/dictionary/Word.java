package com.example.spider.dictionary;

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
}
