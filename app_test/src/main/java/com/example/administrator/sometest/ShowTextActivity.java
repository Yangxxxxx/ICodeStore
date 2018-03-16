package com.example.administrator.sometest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.ClipboardManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class ShowTextActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_text);

        textView = (TextView)findViewById(R.id.tv_text_bord);
        ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        textView.setText(c.getText());
    }

    public void onPlus(View view){
        textView.setTextSize(px2dip(this, textView.getTextSize()) + 2);
    }


    public void onSub(View view){
        textView.setTextSize(px2dip(this, textView.getTextSize()) - 2);
    }


    public  int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
