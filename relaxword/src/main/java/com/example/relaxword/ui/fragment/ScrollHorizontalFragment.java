package com.example.relaxword.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.relaxword.R;
import com.example.relaxword.ui.presenter.WordNetInfoPresenter;

import java.util.ArrayList;
import java.util.List;


public class ScrollHorizontalFragment extends Fragment {
    private ViewPager viewPager;
    private WordNetInfoPresenter wordNetInfoPresenter;
    private WordNetInfoFragment wordNetInfoFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private BlankPageVisibilityListener blankPageVisibilityListener;

    public void updateWord(String word){
        wordNetInfoPresenter.updateWord(word);
    }

    public boolean backPress(){
        if(viewPager.getCurrentItem() != 0){
            viewPager.setCurrentItem(0);
            wordNetInfoFragment.backPress();
            return true;
        }
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scroll_horizontal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof BlankPageVisibilityListener){
            blankPageVisibilityListener = (BlankPageVisibilityListener) context;
        }
    }

    private void initData(){
        wordNetInfoFragment = WordNetInfoFragment.newInstance();
        wordNetInfoPresenter = new WordNetInfoPresenter(wordNetInfoFragment);

        fragmentList.add(BlankFragment.newInstance());
        fragmentList.add(wordNetInfoFragment);
    }

    private void initView(View view){
        viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager()));
        viewPager.addOnPageChangeListener(new PagerChangeListener());
    }

    class FragmentAdapter extends FragmentPagerAdapter{

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    class PagerChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(blankPageVisibilityListener != null){
                blankPageVisibilityListener.onBlankPageVisibilityChange(position == 0);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public interface BlankPageVisibilityListener{
        void onBlankPageVisibilityChange(boolean isVisible);
    }
}
