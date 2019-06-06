package com.youdao.sdk.ydtranslatedemo.translate;

import java.util.List;

public class WordBean {
    private String voice_us_url;//美语发音网络连接
    private String voice_uk_url;//英语发音网络连接
    private String phonetic_us;//美语音标
    private String phonetic_uk;//英语音标

    private String word;
    private List<String> meanings;
    private List<WebMeaning> webMeanings;

    public String getVoiceUSurl() {
        return voice_us_url;
    }

    public void setVoiceUSurl(String voice_us_url) {
        this.voice_us_url = voice_us_url;
    }

    public String getVoiceUKurl() {
        return voice_uk_url;
    }

    public void setVoiceUKurl(String voice_uk_url) {
        this.voice_uk_url = voice_uk_url;
    }

    public String getPhoneticUS() {
        return phonetic_us;
    }

    public void setPhoneticUS(String phonetic_us) {
        this.phonetic_us = phonetic_us;
    }

    public String getPhoneticUK() {
        return phonetic_uk;
    }

    public void setPhoneticUK(String phonetic_uk) {
        this.phonetic_uk = phonetic_uk;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<String> meanings) {
        this.meanings = meanings;
    }

    public List<WebMeaning> getWebMeanings() {
        return webMeanings;
    }

    public void setWebMeanings(List<WebMeaning> webMeanings) {
        this.webMeanings = webMeanings;
    }

    @Override
    public boolean equals(Object obj) {
        boolean hasWord = obj instanceof String && word.equals((String)obj);
        boolean sameWord = obj instanceof WordBean && word.equals(((WordBean)obj).getWord());
        return hasWord || sameWord;
    }
}
