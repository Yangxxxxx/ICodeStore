package com.example.administrator.sometest.SystemBarTest;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.example.administrator.sometest.R;

public class SystemBarMainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_bar_main);
    }

    public void click1(View view){
        startActivity(new Intent(this, Activity1.class));
    }

    public void click2(View view){
        startActivity(new Intent(this, Activity2.class));
    }

    public void click3(View view){
        startActivity(new Intent(this, Activity3.class));
    }

}
