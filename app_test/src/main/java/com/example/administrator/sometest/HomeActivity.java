package com.example.administrator.sometest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.administrator.sometest.ActivityLaunchModeTest.LaunchModeActivity;
import com.example.administrator.sometest.AnimTest.AnimActivity;
import com.example.administrator.sometest.BrodcastReceiverTest.ReceiverActivity;
import com.example.administrator.sometest.DPTest.DPTestActivity;
import com.example.administrator.sometest.DatabindingTest.DataBindingTestActivity;
import com.example.administrator.sometest.DialTest.DialTestActivity;
import com.example.administrator.sometest.DrawableTest.ClipDrawableTestActivity;
import com.example.administrator.sometest.HttpUrlConnectionTest.HttpUrlConnectionActivity;
import com.example.administrator.sometest.KotlinTest.KotlinTestActivity;
import com.example.administrator.sometest.LayoutChangeTest.LayoutChangeActivity;
import com.example.administrator.sometest.LifeCycleTest.LifeCycleActivity;
import com.example.administrator.sometest.MeasureLayoutTest.MeasureLayoutActivity;
import com.example.administrator.sometest.NumberPickerTest.NumberPickerActivity;
import com.example.administrator.sometest.OtherTest.OtherActivity;
import com.example.administrator.sometest.OtherTest.ScrollToolbarTestActivity;
import com.example.administrator.sometest.OverlayGuideTest.GuideActivity;
import com.example.administrator.sometest.RTLTest.RTLTestActivity;
import com.example.administrator.sometest.RecyclerViewTest.RecyclerViewTestActivity;
import com.example.administrator.sometest.RoomTest.RoomTestActivity;
import com.example.administrator.sometest.SavedInstanceStateTest.SavedInstanceStateActivity;
import com.example.administrator.sometest.ServiceTest.ServiceTestActivity;
import com.example.administrator.sometest.ShellTopActivity.ShelltopActivity;
import com.example.administrator.sometest.SocketTest.SocketActivity;
import com.example.administrator.sometest.SocketTest.SocketActivity;
import com.example.administrator.sometest.SystemBarTest.SystemBarMainActivity;
import com.example.administrator.sometest.TaskQueueTest.ExclusiveTask;
import com.example.administrator.sometest.TaskQueueTest.ExclusiveTaskManager;
import com.example.administrator.sometest.TaskQueueTest.TaskQueueActivity;
import com.example.administrator.sometest.TmpActivity.TempActivity;
import com.example.administrator.sometest.ToolbarMenuTest.ToolbarMenuActivity;
import com.example.administrator.sometest.TouchEventTest.TouchEventActivity;
import com.example.administrator.sometest.ViewDrawProcessTest.ViewDrawProcessActivity;
import com.example.administrator.sometest.ViewGragHelperTest.ViewDragActivity;
import com.example.administrator.sometest.ViewPagerTest.ViewPagerTestActivity;
import com.example.administrator.sometest.fragmentTest.FragmentTestActivity;
import com.example.administrator.sometest.mp4parser.MP4ParserActivity;
import com.example.administrator.sometest.patch9Test.Patch9Activity;

public class HomeActivity extends AppCompatActivity{
    private static final String TAG = "HomeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addButton("MP4ParserActivity", MP4ParserActivity.class);
        addButton("ShelltopActivity", ShelltopActivity.class);
        addButton("ViewDragActivity", ViewDragActivity.class);
        addButton("RecyclerViewTestActivity", RecyclerViewTestActivity.class);
        addButton("DialTestActivity", DialTestActivity.class);
        addButton("NumberPickerActivity", NumberPickerActivity.class);
        addButton("LifeCycleActivity", LifeCycleActivity.class);
        addButton("TaskQueueActivity", TaskQueueActivity.class);
        addButton("SocketActivity", SocketActivity.class);
        addButton("SavedInstanceStateActivity", SavedInstanceStateActivity.class);
        addButton("MeasureLayoutActivity", MeasureLayoutActivity.class);
        addButton("HttpUrlConnectionActivity", HttpUrlConnectionActivity.class);
        addButton("LayoutChangeActivity", LayoutChangeActivity.class);
        addButton("RoomTestActivity", RoomTestActivity.class);
        addButton("ServiceTestActivity", ServiceTestActivity.class);
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
        addButton("DataBindingTestActivity", DataBindingTestActivity.class);
        addButton("clipDrawableTest", ClipDrawableTestActivity.class);
        addButton("OtherTest", OtherActivity.class);
        addButton("DPTest", DPTestActivity.class);
        addButton("ScrollToolbar", ScrollToolbarTestActivity.class);
        addButton("FragmentTestActivity", FragmentTestActivity.class);
        addButton("TempActivity", TempActivity.class);

        ExclusiveTaskManager.getInstance().addTask(exclusiveTask2);
//        ExclusiveTaskManager.getInstance().addTask(exclusiveTask);
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

    private ExclusiveTask exclusiveTask = new ExclusiveTask() {
        @Override
        public void excute() {
            Log.e("yang", "HomeActivity dialog");
            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("HomeActivity")
                    .setMessage("this is HomeActivity")
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finishTask();
                        }
                    })
                    .show();
            stayTaskRunning();
        }
    };


    private ExclusiveTask exclusiveTask2 = new ExclusiveTask() {
        @Override
        public void excute() {
            Log.e("yang", "HomeActivity exclusiveTask2 running");
        }
    };

    @Override
    protected void onDestroy() {
        ExclusiveTaskManager.getInstance().finish();
        super.onDestroy();
    }
}
