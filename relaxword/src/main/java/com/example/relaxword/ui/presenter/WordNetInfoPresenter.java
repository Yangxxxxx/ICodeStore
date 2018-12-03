package com.example.relaxword.ui.presenter;

import android.content.Context;

import com.example.relaxword.ui.Contract.WordNetInfoContract;
import com.example.relaxword.ui.Widget.SimpleWebView;

import java.util.ArrayList;
import java.util.List;

public class WordNetInfoPresenter implements WordNetInfoContract.Presenter {
    private static final String[] WEB_URL = new String[]{
            "https://fanyi.baidu.com/#en/zh/%1$s",
            "https://cn.bing.com/search?q=%1$s&FORM=BEHPTB&ensearch=1",
            "http://fanyi.sogou.com/#en/zh-CHS/%1$s"};

    private WordNetInfoContract.View view;
    private List<SimpleWebView> webViewList = new ArrayList<>();

    public WordNetInfoPresenter(WordNetInfoContract.View view){
        this.view = view;
    }

    @Override
    public void start(Context context) {
        for (int i = 0; i < WEB_URL.length; i++){
            webViewList.add(new SimpleWebView(context));
        }
        view.onLoadWebView(webViewList);
    }

    @Override
    public void updateWord(String word) {
        if(webViewList.isEmpty()) return;
        webViewList.get(0).loadUrl(String.format(WEB_URL[0], word));
        webViewList.get(1).loadUrl(String.format(WEB_URL[1], word));
        webViewList.get(2).loadUrl(String.format(WEB_URL[2], word));
    }
}
