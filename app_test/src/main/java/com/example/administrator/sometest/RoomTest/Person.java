package com.example.administrator.sometest.RoomTest;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Administrator on 2018/6/20 0020.
 */

@Entity(tableName = "PersonTabe")
public class Person {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "gender")

    private long gender;

    @Ignore
    private int reserve;

    @Embedded
    private Work firstWork;


    public Person(String name, long gender){
        this.name = name;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getGender() {
        return gender;
    }

    public int getReserve() {
        return reserve;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setReserve(int reserve) {
        this.reserve = reserve;
    }

    public Work getFirstWork() {
        return firstWork;
    }

    public void setFirstWork(Work firstWork) {
        this.firstWork = firstWork;
    }
}
