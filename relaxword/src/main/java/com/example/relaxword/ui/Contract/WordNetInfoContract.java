package com.example.relaxword.ui.Contract;

import android.content.Context;

import com.example.relaxword.ui.Widget.SimpleWebView;

import java.util.List;

public class WordNetInfoContract {
    public interface Presenter{
        void start(Context context);
        void updateWord(String url);
    }

    public interface View{
        void onLoadWebView(List<SimpleWebView> simpleWebViews);
    }
}
