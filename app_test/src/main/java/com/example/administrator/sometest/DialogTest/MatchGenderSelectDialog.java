package com.example.administrator.sometest.DialogTest;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.example.administrator.sometest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/29 0029.
 */

public class MatchGenderSelectDialog extends Dialog implements View.OnClickListener{
    private List<View> allItems = new ArrayList<>();
    public static final int MATCH_BOTH = 0;
    public static final int MATCH_MALE = 1;
    public static final int MATCH_FEMALE = 2;
    private OnSelectListener onSelectListener;


    public MatchGenderSelectDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_match_gender_select);
        init();
    }

    public MatchGenderSelectDialog setSelectListener(OnSelectListener onSelectListener){
        this.onSelectListener = onSelectListener;
        return this;
    }

    @Override
    public void onClick(View v) {
        for(View view: allItems){
            view.setSelected(false);
        }
        v.setSelected(true);

        if(onSelectListener != null){
            onSelectListener.onSelect(allItems.indexOf(v));
        }
    }

    interface OnSelectListener{
        void onSelect(int gender);
    }

    private void init(){
        allItems.add(findViewById(R.id.ll_both));
        allItems.add(findViewById(R.id.ll_male));
        allItems.add(findViewById(R.id.ll_female));

        for (View view: allItems){
            view.setOnClickListener(this);
        }
    }

}
