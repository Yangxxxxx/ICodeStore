package com.example.administrator.sometest.RoomTest;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Administrator on 2018/6/20 0020.
 */

@Dao
public interface PersonDao {

    @Query("select * from persontabe")
    List<Person> qureyAllPerson();

    @Insert
    void insert(Person person);

    @Delete
    void delete(Person person);

    @Update
    void update(Person person);
}
