package com.example.administrator.sometest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.administrator.sometest.ActivityLaunchModeTest.LaunchModeActivity;
import com.example.administrator.sometest.AnimTest.AnimActivity;
import com.example.administrator.sometest.BrodcastReceiverTest.ReceiverActivity;
import com.example.administrator.sometest.DPTest.DPTestActivity;
import com.example.administrator.sometest.DatabindingTest.DataBindingTestActivity;
import com.example.administrator.sometest.DrawableTest.ClipDrawableTestActivity;
import com.example.administrator.sometest.KotlinTest.KotlinTestActivity;
import com.example.administrator.sometest.OtherTest.OtherActivity;
import com.example.administrator.sometest.OtherTest.ScrollToolbarTestActivity;
import com.example.administrator.sometest.OverlayGuideTest.GuideActivity;
import com.example.administrator.sometest.RTLTest.RTLTestActivity;
import com.example.administrator.sometest.SystemBarTest.SystemBarMainActivity;
import com.example.administrator.sometest.TmpActivity.TempActivity;
import com.example.administrator.sometest.ToolbarMenuTest.ToolbarMenuActivity;
import com.example.administrator.sometest.TouchEventTest.TouchEventActivity;
import com.example.administrator.sometest.ViewDrawProcessTest.ViewDrawProcessActivity;
import com.example.administrator.sometest.ViewPagerTest.ViewPagerTestActivity;
import com.example.administrator.sometest.patch9Test.Patch9Activity;

public class HomeActivity extends AppCompatActivity{
    private static final String TAG = "HomeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addButton("ViewDrawProcessActivity", ViewDrawProcessActivity.class);
        addButton("TouchEventActivity", TouchEventActivity.class);
        addButton("LaunchModeActivity", LaunchModeActivity.class);
        addButton("ReceiverActivity", ReceiverActivity.class);
        addButton("AnimActivity", AnimActivity.class);
        addButton("GuideActivity", GuideActivity.class);
        addButton("Patch9Activity", Patch9Activity.class);
        addButton("RTLTestActivity", RTLTestActivity.class);

        addButton("ToolbarMenuActivity", ToolbarMenuActivity.class);
        addButton("SystemBarMainActivity", SystemBarMainActivity.class);
        addButton("KotlinTestActivity", KotlinTestActivity.class);
        addButton("ViewPagerTestActivity", ViewPagerTestActivity.class);
        addButton("databindingTest", DataBindingTestActivity.class);
        addButton("clipDrawableTest", ClipDrawableTestActivity.class);
        addButton("OtherTest", OtherActivity.class);
        addButton("DPTest", DPTestActivity.class);
        addButton("ScrollToolbar", ScrollToolbarTestActivity.class);
        addButton("TempActivity", TempActivity.class);
    }

    private void addButton(String buttonText, final Class activityClass){
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ll_container);
        Button button = new Button(this);
        button.setText(buttonText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, activityClass));
            }
        });
        linearLayout.addView(button);
    }

}
