package com.example.administrator.sometest.fragmentTest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.administrator.sometest.R;

public class FragmentTestActivity2 extends AppCompatActivity {
    private Fragment1 fragment1 = new Fragment1() ;
    private Fragment2 fragment2 = new Fragment2() ;
    private Fragment3 fragment3 = new Fragment3() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test2);
        addFragment(R.id.fl_container, fragment1);
//        addFragment(R.id.fl_container, fragment2);

        findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide(fragment1);
            }
        });

        findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(fragment1);
            }
        });
    }

    private void addFragment(int container, Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.anim.anim_edit_menu_show, R.anim.anim_edit_menu_hide);
        fragmentTransaction.add(container, fragment1);
        fragmentTransaction.hide(fragment1);

        fragmentTransaction.add(container, fragment2);
        fragmentTransaction.hide(fragment2);

        fragmentTransaction.commitAllowingStateLoss();
    }

    private void hide(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(0, R.anim.anim_edit_menu_hide);
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void show(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragment1);
        fragmentTransaction.hide(fragment2);
        fragmentTransaction.setCustomAnimations(R.anim.anim_edit_menu_show, 0);
        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }



}
