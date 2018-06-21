package com.example.jtnote.bean;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.jtnote.db.DBTables;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

@Entity(tableName = DBTables.NoteItemTable.TABLE_NAME)
public class NoteItem {
    @ColumnInfo(name = DBTables.NoteItemTable._ID) @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = DBTables.NoteItemTable.TEXT_CONTENT)
    String textContent;

    @ColumnInfo(name = DBTables.NoteItemTable.AUDIO_CONTENT)
    String audioContent;

    @ColumnInfo(name = DBTables.NoteItemTable.IMAGE_CONTENT)
    String imageContent;

    @ColumnInfo(name = DBTables.NoteItemTable.VIDEO_CONTENT)
    String videoContent;

    @ColumnInfo(name = DBTables.NoteItemTable.ALARM_TIME)
    long alarmTime;

    @ColumnInfo(name = DBTables.NoteItemTable.CREATE_TIME)
    long creatTime;

    public NoteItem(){

    }

    @Ignore
    public NoteItem(String textContent, long creatTime){
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
