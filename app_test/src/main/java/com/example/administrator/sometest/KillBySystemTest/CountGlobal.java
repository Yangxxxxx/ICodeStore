package com.example.administrator.sometest.KillBySystemTest;

public class CountGlobal {
    public static CountGlobal countGlobal = new CountGlobal();

    private int count = 0;

    public int getCount(){
        return ++count;
    }
}
