package com.example.jtnote.ui.DetailPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.example.jtnote.Constants;
import com.example.jtnote.R;
import com.example.jtnote.bean.NoteItem;
import com.example.jtnote.ui.TextDetailPage.TextDetailFragment;
import com.example.jtnote.ui.TimePickerPage.TimePickerFragment;


public class DetialActivity extends AppCompatActivity implements View.OnClickListener{
    private TextDetailFragment textDetailFragment;
    private TimePickerFragment timePickerFragment;

    private NoteItem noteItem;

    public static void start(Context context, NoteItem noteItem){
        Intent intent = new Intent(context, DetialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.KEY_NOTEITEM_PARAM, noteItem);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detial);
        initView();
        initData();
        replaceFragment(textDetailFragment);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_text_page:
                replaceFragment(textDetailFragment);
                break;
            case  R.id.tv_time_page:
                replaceFragment(timePickerFragment);
                break;
        }
    }

    private void initView(){
        findViewById(R.id.tv_text_page).setOnClickListener(this);
        findViewById(R.id.tv_time_page).setOnClickListener(this);
    }

    private void initData(){
        Intent intent = getIntent();
        if(intent != null){
            noteItem = (NoteItem)intent.getSerializableExtra(Constants.KEY_NOTEITEM_PARAM);
        }

        textDetailFragment = TextDetailFragment.newInstance(noteItem);
        timePickerFragment = TimePickerFragment.newInstance(noteItem);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
