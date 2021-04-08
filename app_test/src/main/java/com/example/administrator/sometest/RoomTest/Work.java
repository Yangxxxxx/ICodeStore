package com.example.administrator.sometest.RoomTest;

import androidx.room.Entity;

/**
 * Created by Administrator on 2018/6/21 0021.
 */

public class Work {
    private String companyName;
    private String compnayAddress;

    public Work(String companyName, String compnayAddress){
        this.companyName = companyName;
        this.compnayAddress = compnayAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompnayAddress() {
        return compnayAddress;
    }

    public void setCompnayAddress(String compnayAddress) {
        this.compnayAddress = compnayAddress;
    }
}
