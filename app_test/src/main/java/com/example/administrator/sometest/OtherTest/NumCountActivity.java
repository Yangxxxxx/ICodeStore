package com.example.administrator.sometest.OtherTest;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.administrator.sometest.R;
import com.example.administrator.sometest.UtilTest.NumberCount;

public class NumCountActivity extends AppCompatActivity {
    private NumberCount numberCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_other_test);

        numberCount = new NumberCount(0, 8, 1000) {
            @Override
            public void onTick(int currNum) {
                Log.e("yang", "onTick: " + currNum);
            }

            @Override
            public void onFinish() {
                Log.e("yang", "onFinish");
            }
        };
    }

    public void click1(View view) {
        numberCount.start();
    }

    public void click2(View view) {
        numberCount.cancel();
    }

    private void showText(String text){
        TextView textView = (TextView)findViewById(R.id.tv_text_bord);
        textView.setText(text);
    }

    public void click3(View view) {
        numberCount.pause();
    }

    public void click4(View view) {
        numberCount.resume();
    }
}
