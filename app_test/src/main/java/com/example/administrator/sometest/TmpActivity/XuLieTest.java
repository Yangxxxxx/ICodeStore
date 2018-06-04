package com.example.administrator.sometest.TmpActivity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/5/11 0011.
 */

public class XuLieTest implements Parcelable{

    protected XuLieTest(Parcel in) {
    }

    public static final Creator<XuLieTest> CREATOR = new Creator<XuLieTest>() {
        @Override
        public XuLieTest createFromParcel(Parcel in) {
            return new XuLieTest(in);
        }

        @Override
        public XuLieTest[] newArray(int size) {
            return new XuLieTest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
