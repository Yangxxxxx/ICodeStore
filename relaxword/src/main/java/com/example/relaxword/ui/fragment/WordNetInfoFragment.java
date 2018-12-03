package com.example.relaxword.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.relaxword.R;
import com.example.relaxword.ui.Contract.WordNetInfoContract;
import com.example.relaxword.ui.Widget.CustomSystemWidget.TabLayoutLocal;
import com.example.relaxword.ui.Widget.SimpleWebView;
import com.example.relaxword.ui.presenter.WordNetInfoPresenter;

import java.util.ArrayList;
import java.util.List;

public class WordNetInfoFragment extends Fragment implements WordNetInfoContract.View {
    private ViewPager viewPager;
    private WordNetInfoContract.Presenter presenter;

    private List<SimpleWebView> webViewList = new ArrayList<>();

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
        presenter.start(getContext());

        //test code
        presenter.updateWord("contract");
    }

    @Override
    public void onDestroyView() {
        for (SimpleWebView item: webViewList){
            item.clearCache();
        }
        super.onDestroyView();
    }

    @Override
    public void onLoadWebView(List<SimpleWebView> simpleWebViews) {
        webViewList.clear();
        webViewList.addAll(simpleWebViews);
        viewPager.setAdapter(new ContentAdapter());
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


    private void initView(View view){
        viewPager = view.findViewById(R.id.vp_content);

        TabLayoutLocal tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initData(){
        presenter = new WordNetInfoPresenter(this);
    }
}
