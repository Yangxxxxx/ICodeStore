package com.example.yangjitao.icodestore.FragmentUsage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.example.yangjitao.icodestore.R;

import java.util.ArrayList;
import java.util.List;

public class Activity1 extends AppCompatActivity {
    List<Fragment> fragmentList = new ArrayList<>();
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        container = (FrameLayout)findViewById(R.id.container);

        fragmentList.add(new Fragment1());
        fragmentList.add(new Fragment2());
        fragmentList.add(new Fragment3());

        addAllFragment();

        showFragment(fragmentList.get(0));
    }

    public void nowClick(View view){
        int id = view.getId();
        if(id == R.id.bt1){
            showFragment(fragmentList.get(0));
        }else if(id == R.id.bt2){
            showFragment(fragmentList.get(1));
        }else if(id == R.id.bt3){
            showFragment(fragmentList.get(2));
        }
    }

    private void showFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for(Fragment item: fragmentList){
            transaction.hide(item);
        }
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }

    private void addAllFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for(Fragment fragment: fragmentList){
            transaction.add(R.id.container, fragment);
            transaction.hide(fragment);
        }
        transaction.commitAllowingStateLoss();
    }

}
