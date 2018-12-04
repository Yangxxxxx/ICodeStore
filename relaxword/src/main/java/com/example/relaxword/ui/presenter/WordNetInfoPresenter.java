package com.example.relaxword.ui.presenter;

import android.content.Context;

import com.example.relaxword.ui.Constants;
import com.example.relaxword.ui.Contract.WordNetInfoContract;
import com.example.relaxword.ui.Widget.SimpleWebView;

import java.util.ArrayList;
import java.util.List;

public class WordNetInfoPresenter implements WordNetInfoContract.Presenter {
    private WordNetInfoContract.View view;
    private List<SimpleWebView> webViewList = new ArrayList<>();

    public WordNetInfoPresenter(WordNetInfoContract.View view){
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start(Context context) {
        webViewList.clear();
        for (int i = 0; i < Constants.WEB_URL.length; i++){
            webViewList.add(new SimpleWebView(context));
        }
        view.onLoadWebView(webViewList);
    }

    @Override
    public void updateWord(String word) {
        if(webViewList.isEmpty()) return;
        for (int i = 0; i < Constants.WEB_URL.length; i++){
            webViewList.get(i).loadUrl(String.format(Constants.WEB_URL[i], word));
        }
    }
}
