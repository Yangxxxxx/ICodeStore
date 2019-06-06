package com.example.relaxword.ui.bean;

import java.util.List;

public class WebMeaning {
    private String key;
    private List<String> meanings;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<String> meanings) {
        this.meanings = meanings;
    }
}
