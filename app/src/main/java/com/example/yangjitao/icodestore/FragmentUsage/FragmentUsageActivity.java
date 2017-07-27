package com.example.yangjitao.icodestore.FragmentUsage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.yangjitao.icodestore.R;

public class FragmentUsageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_usage);
    }

    public void nowClick(View view){
        int id = view.getId();
        if(id == R.id.click_switch){
            startActivity(new Intent(this, Activity1.class));
        }else if(id == R.id.swip_switch){
            startActivity(new Intent(this, Activity2.class));
        }else if(id == R.id.menu_pop){
            startActivity(new Intent(this, Activity3.class));
        }
    }
}
