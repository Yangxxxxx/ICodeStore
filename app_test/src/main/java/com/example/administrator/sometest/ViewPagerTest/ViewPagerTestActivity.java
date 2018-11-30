package com.example.administrator.sometest.ViewPagerTest;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.sometest.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerTestActivity extends AppCompatActivity {
    private List<View> pagerItems = new ArrayList<>();
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_test);
        initView();
    }


    private void initView(){
        pagerItems.add(getItem(1));
        pagerItems.add(getItem(2));
        pagerItems.add(getItem(3));
        pagerItems.add(getItem(4));
        pagerItems.add(getItem(5));
        pagerItems.add(getItem(6));
        pagerItems.add(getItem(7));
        pagerItems.add(getItem(8));

        viewPager = (ViewPager)findViewById(R.id.vp01);
        viewPager.setAdapter(new IPagerAdapter());


        TabLayout tabLayout = (TabLayout)findViewById(R.id.tl);
        tabLayout.setupWithViewPager(viewPager);
    }



    class IPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            Log.e("yang", "getCount");
            return pagerItems.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            Log.e("yang", "isViewFromObject");
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.e("yang", "instantiateItem " + position);
            View itemView = pagerItems.get(position);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.e("yang", "destroyItem" + position);
            container.removeView(pagerItems.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "haha";
        }

    }

    private View getItem(int num){
//        ImageView imageView = new ImageView(this);
//        imageView.setImageResource(R.drawable.ic_launcher);
//        return  imageView;
        TextView textView = new TextView(this);
        textView.setText(num+"");
        return textView;
    }
}
