package com.example.administrator.sometest.LayoutChangeTest;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.sometest.R;

public class LayoutChangeActivity extends AppCompatActivity {

    @TargetApi(20)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_change);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

//        findViewById(R.id.rl_root).setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
//            @Override
//            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
//                Log.e("yang", "insets: " + insets.getSystemWindowInsetLeft()+" "+
//                        insets.getSystemWindowInsetTop()+" "+
//                        insets.getSystemWindowInsetRight()+" "+
//                        insets.getSystemWindowInsetBottom());
//                return null;
//            }
//        });
    }

    public void click1(View view){
        EditText editText = (EditText)findViewById(R.id.et_edit);
        TextView textView = (TextView)findViewById(R.id.text);
        textView.setText(editText.getText().toString());
        Log.e("yang", "text: " + editText.getText().toString());
    }
}
