package com.example.jtnote.bean;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class NoteItem {
    String textContent;
    String audioContent;
    String imageContent;
    String videoContent;
    long alarmTime;
    long creatTime;

    public NoteItem(){

    }

    public NoteItem(String textContent, int creatTime){
        this.textContent = textContent;
        this.creatTime = creatTime;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getAudioContent() {
        return audioContent;
    }

    public void setAudioContent(String audioContent) {
        this.audioContent = audioContent;
    }

    public String getImageContent() {
        return imageContent;
    }

    public void setImageContent(String imageContent) {
        this.imageContent = imageContent;
    }

    public String getVideoContent() {
        return videoContent;
    }

    public void setVideoContent(String videoContent) {
        this.videoContent = videoContent;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }
}
