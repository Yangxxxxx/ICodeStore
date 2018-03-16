package com.example.administrator.sometest.DPTest;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.administrator.sometest.R;


public class DPTestActivity extends AppCompatActivity {
    DisplayMetrics displayMetrics = new DisplayMetrics();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dptest);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        findViewById(R.id.tv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DPTestActivity.this, DPTestActivity2.class));
            }
        });


        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        Point RealPoint = new Point();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(RealPoint);
        }

        Configuration configuration = getResources().getConfiguration();

        float with = RealPoint.x / displayMetrics.densityDpi;
        float height = RealPoint.y / displayMetrics.densityDpi;
        double screenSize = Math.sqrt((with * with) + (height * height));

        TextView textView = (TextView)findViewById(R.id.tv);
        textView.setText(
                screenWidth + " " + screenHeight + " \n"
                        + " real size: " + RealPoint.x + " "+ RealPoint.y +"\n"
                + " densityDpi: " + displayMetrics.densityDpi+"\n"
                + " xdpi: " + displayMetrics.xdpi+"\n"
                + " ydpi: " + displayMetrics.xdpi+"\n"
                + " density: " + displayMetrics.density+"\n"
                + "width dp:  " + screenWidth/displayMetrics.density  + "  " + screenHeight/displayMetrics.density + "\n"
                + "screenWidthDp smallestScreenWidthDp :  " + configuration.screenWidthDp + " " + configuration.smallestScreenWidthDp + "\n"
                        + "navigation :  " + configuration.navigation + " " + configuration.navigationHidden + "\n"
                + "res folder: " + getString(R.string.folder_name) + "\n"
                + " screen size(inch) w: " + String.format("%1$.2f", with) + "  h: " + String.format("%1$.2f", height) + " size: " + String.format("%1$.2f", screenSize)
        );

        getOneInchView();
        getOneInchView2();
    }

    private void getOneInchView(){
        View view = findViewById(R.id.tv_one_inch);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = displayMetrics.densityDpi;
        layoutParams.height = displayMetrics.densityDpi;
        view.setLayoutParams(layoutParams);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DPTestActivity.this, DPTestActivity2.class));
            }
        });
    }


    private void getOneInchView2(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        View view = findViewById(R.id.tv_one_inch2);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int)displayMetrics.xdpi;
        layoutParams.height = (int)displayMetrics.ydpi;
        view.setLayoutParams(layoutParams);
    }
}
