package com.example.jtnote.bean;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.jtnote.db.DBTables;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

@Entity(tableName = DBTables.NoteItemTable.TABLE_NAME)
public class NoteItem implements Serializable{
    @ColumnInfo(name = DBTables.NoteItemTable._ID) @PrimaryKey
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
    public NoteItem(int id, String textContent, long creatTime){
        this.id = id;
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

    public void removeAlarm(){
        setAlarmTime(0);
    }

    public boolean hasAlarm(){
        return alarmTime > 0;
    }

    /**
     * 返回闹钟距离当前的时间间隔
     * @return
     */
    public long getTimeIntervalMilli(){
        return alarmTime - System.currentTimeMillis();
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
