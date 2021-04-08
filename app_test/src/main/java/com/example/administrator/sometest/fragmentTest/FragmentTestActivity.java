package com.example.administrator.sometest.fragmentTest;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.sometest.R;
import com.example.administrator.sometest.RuntimePermissionManager;

import java.util.ArrayList;
import java.util.List;

public class FragmentTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);

        ViewPager viewPager = (ViewPager)findViewById(R.id.main_viewPager);
        viewPager.setAdapter(new IFragmentAdapter(getSupportFragmentManager()));
        RuntimePermissionManager.getInstance().setMainActivity(this);
    }

    class IFragmentAdapter extends FragmentPagerAdapter{
        List<Fragment> fragmentList = new ArrayList<>();

        public IFragmentAdapter(FragmentManager fm) {
            super(fm);
            fragmentList.add(Fragment1.newInstance());
            fragmentList.add(Fragment2.newInstance());
            fragmentList.add(Fragment3.newInstance());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RuntimePermissionManager.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
