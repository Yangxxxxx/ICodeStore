package com.example.administrator.sometest.SavedInstanceStateTest;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.example.administrator.sometest.R;
import com.example.administrator.sometest.fragmentTest.Fragment1;
import com.example.administrator.sometest.fragmentTest.Fragment2;

import java.util.Set;

public class SavedInstanceStateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            printBundle(0, savedInstanceState);
            savedInstanceState.clear();
            savedInstanceState = null;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_instance_state);

        //现场回复后会有两个fragment2
        Fragment2 fragment = new Fragment2();
//        fragment.setRetainInstance(false);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, fragment).commitAllowingStateLoss();

        findViewById(R.id.tv_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlbulm();
            }
        });
    }


    private void startAlbulm(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Uri resultData = data.getData();
            Log.e("yang", "selected photo: " + resultData);
        }

    }

    private void printBundle(int nestID, Bundle bundle){
        Set<String> keys = bundle.keySet();
        for (String item: keys){
            Object object = bundle.get(item);
            Log.e("yang", "nestID: " + nestID+ " key: " + item + " value: " + (object == null ? null : object.toString()));
            if(object instanceof Bundle){
                printBundle(nestID + 1, (Bundle)object);
            }else if(object instanceof SparseArray){
                printAparseArray(nestID + 1, (SparseArray) object);
            }
        }
    }

    private void printAparseArray(int nestID, SparseArray sparseArray){
        for (int i = 0; i < sparseArray.size(); i++){
            int keyID = sparseArray.keyAt(i);
            Object object = sparseArray.get(keyID);
            Log.e("yang", "nestID: " + nestID+ " sparseArrayID: " + keyID + " value: " + (object == null ? null : object.toString()));
        }
    }
}
