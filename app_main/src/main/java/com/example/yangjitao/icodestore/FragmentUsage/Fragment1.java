package com.example.yangjitao.icodestore.FragmentUsage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yangjitao.icodestore.R;

/**
 * Created by Administrator on 2017/7/27 0027.
 */

public class Fragment1 extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Log.e("Fragment", "Fragment1 onCreate " + this.hashCode());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("Fragment", "Fragment1 onCreateView " + this.hashCode() + " " + savedInstanceState);
        return inflater.inflate(R.layout.fragment1, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e("Fragment", "Fragment1 onCreateView " + this.hashCode());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        Log.e("Fragment", "Fragment1 onDestroyView "+ this.hashCode());
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {

        Log.e("Fragment", "Fragment1 onDestroy "+ this.hashCode());
        super.onDestroy();
    }
}
