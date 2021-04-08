package com.example.relaxword.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.relaxword.R;
import com.example.relaxword.ui.Constants;
import com.example.relaxword.ui.Contract.WordNetInfoContract;
import com.example.relaxword.ui.Widget.CustomSystemWidget.TabLayoutLocal;
import com.example.relaxword.ui.Widget.SimpleWebView;

import java.util.ArrayList;
import java.util.List;

public class WordNetInfoFragment extends Fragment implements WordNetInfoContract.View {
    private ViewPager viewPager;
    private WordNetInfoContract.Presenter presenter;

    private List<SimpleWebView> webViewList = new ArrayList<>();

    public static WordNetInfoFragment newInstance() {
        return new WordNetInfoFragment();
    }

    public void backPress(){
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        for (SimpleWebView item: webViewList){
            item.clearCache();
        }
        super.onDestroyView();
    }

    @Override
    public void setPresenter(WordNetInfoContract.Presenter presenter) {
        this.presenter = presenter;
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
            return Constants.WEB_NAME[position];
        }
    }


    private void initView(View view){
        viewPager = view.findViewById(R.id.vp_content);

        TabLayoutLocal tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
