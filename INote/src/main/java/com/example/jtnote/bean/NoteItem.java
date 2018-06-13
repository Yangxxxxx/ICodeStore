package com.example.jtnote.bean;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class NoteItem {
    String textContent;
    int creatTime;

    public NoteItem(String textContent, int creatTime){
        this.textContent = textContent;
        this.creatTime = creatTime;
    }

    public String getTextContent(){
        return textContent;
    }

    public void setTextContent(String textContent){
        this.textContent = textContent;
    }
}
