package com.example.administrator.sometest.RoomTest;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Created by Administrator on 2018/6/20 0020.
 */

@Database(entities = {Person.class}, version = 1)
public abstract class PersonDatabase extends RoomDatabase {

    public abstract PersonDao getPersonDao();
}
