package com.example.administrator.sometest.fragmentTest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.sometest.R;

import java.util.Timer;
import java.util.TimerTask;

public class Fragment2 extends Fragment {

    public static Fragment2 newInstance() {
        Fragment2 fragment = new Fragment2();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView textView = view.findViewById(R.id.tv_content);
        textView.setText(Fragment2.this.toString());
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
               Log.e("yang", Fragment2.this.toString() + "timerTask run");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(Fragment2.this.toString());
                    }
                });
            }
        }, 0, 1000);
    }
}
