package com.example.administrator.sometest.MeasureLayoutTest;

import android.app.Dialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.sometest.R;

public class MeasureLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_layout);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_test);
        dialog.show();
    }
}
