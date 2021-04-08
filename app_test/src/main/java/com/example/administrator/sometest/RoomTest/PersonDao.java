package com.example.administrator.sometest.RoomTest;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
