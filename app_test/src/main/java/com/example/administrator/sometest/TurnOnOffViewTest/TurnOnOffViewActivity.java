package com.example.administrator.sometest.TurnOnOffViewTest;

import android.annotation.TargetApi;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.administrator.sometest.R;

public class TurnOnOffViewActivity extends AppCompatActivity {

    @TargetApi(14)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_on_off_view);

        Switch switchView = (Switch)findViewById(R.id.sw);
        switchView.setChecked(true);
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(TurnOnOffViewActivity.this, isChecked+"", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
