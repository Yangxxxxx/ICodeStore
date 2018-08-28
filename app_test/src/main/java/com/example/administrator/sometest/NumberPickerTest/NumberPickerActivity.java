package com.example.administrator.sometest.NumberPickerTest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.sometest.R;

public class NumberPickerActivity extends AppCompatActivity {
    private NumberPanel numberPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_picker);

        numberPanel = findViewById(R.id.number_panel);
        numberPanel.setRange(10, 60);
        numberPanel.setNumberSelectListener(new NumberPanel.NumberSelectListener() {
            @Override
            public void OnNumberSelect(int num) {

            }
        });
    }
}
