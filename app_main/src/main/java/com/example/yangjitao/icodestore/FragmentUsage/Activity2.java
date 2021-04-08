package com.example.yangjitao.icodestore.FragmentUsage;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yangjitao.icodestore.R;

import java.util.ArrayList;
import java.util.List;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(new IFragmentAdapter(getSupportFragmentManager()));
//        viewPager.setOffscreenPageLimit(2);
    }


    class IFragmentAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentList = new ArrayList<>();

        public IFragmentAdapter(FragmentManager fm) {
            super(fm);
            fragmentList.add(new Fragment1());
            fragmentList.add(new Fragment2());
            fragmentList.add(new Fragment3());
            fragmentList.add(new Fragment4());
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
}
