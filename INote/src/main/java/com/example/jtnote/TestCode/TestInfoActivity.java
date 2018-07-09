package com.example.jtnote.TestCode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jtnote.INoteSharePreference;
import com.example.jtnote.R;

public class TestInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_info);

        TextView textView = findViewById(R.id.tv_content);
        textView.setText(INoteSharePreference.getInstance().getString("debugInfoKey"));
    }
}
