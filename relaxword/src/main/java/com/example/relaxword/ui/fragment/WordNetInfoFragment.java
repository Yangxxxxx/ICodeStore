package com.example.relaxword.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.relaxword.R;
import com.example.relaxword.ui.CustomSystemWidget.TabLayoutLocal;

import java.util.ArrayList;
import java.util.List;

public class WordNetInfoFragment extends Fragment {
    private ViewPager viewPager;

    private List<WebView> webViewList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_word_net_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view){
        viewPager = view.findViewById(R.id.vp_content);
        viewPager.setAdapter(new ContentAdapter());

        TabLayoutLocal tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabMode(TabLayoutLocal.MODE_SCROLLABLE);
    }

    private void initData(){
        webViewList.add(genWebView());
        webViewList.add(genWebView());
        webViewList.add(genWebView());

        String word = "hello";

        webViewList.get(0).loadUrl(String.format("https://m.baidu.com/sf_fanyi/#en/zh/%1$s", word));
        webViewList.get(1).loadUrl(String.format("https://cn.bing.com/dict/search?q=%1$s&FORM=HDRSC6", word));
        webViewList.get(2).loadUrl(String.format("http://fanyi.sogou.com/#en/zh-CHS/%1$s", word));
    }

    private WebView genWebView(){
        WebView webView = new WebView(getContext());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        return webView;
    }


    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return webViewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = webViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(webViewList.get(position));
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return "Title";
        }
    }

}
