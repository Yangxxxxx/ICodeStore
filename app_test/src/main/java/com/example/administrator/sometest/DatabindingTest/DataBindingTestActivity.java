package com.example.administrator.sometest.DatabindingTest;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.example.administrator.sometest.R;

public class DataBindingTestActivity extends AppCompatActivity {
//    ActivityDataBindingTestBinding binding;
//    Abc abc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_binding_test);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding_test);
//        abc = new Abc("hahaha", 123);
//        binding.setAbc(abc);
    }

    public void onClick1(View view) {
//        abc.a += "a";
//        binding.setAbc(abc);
    }
}
