package com.example.spider.annotation;

public enum AttrName {
    TEXT(""),
    SRC("src");

    private String name;
    AttrName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}
