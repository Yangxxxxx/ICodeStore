package com.example.administrator.sometest.TaskQueueTest;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.administrator.sometest.R;

public class TaskQueueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_queue);

        ExclusiveTaskManager.getInstance().addTask(exclusiveTask3);
        ExclusiveTaskManager.getInstance().addTask(exclusiveTask);
        ExclusiveTaskManager.getInstance().addTask(exclusiveTask2);

        findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExclusiveTaskManager.getInstance().start();
            }
        });
    }


    private ExclusiveTask exclusiveTask = new ExclusiveTask() {
        @Override
        public void excute() {
            new AlertDialog.Builder(TaskQueueActivity.this)
                    .setTitle("taskActvity")
                    .setMessage("this is taskActivity")
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
            Log.e("yang", " TaskQueueActivity exclusiveTask2 running");
        }
    };

    private ExclusiveTask exclusiveTask3 = new ExclusiveTask() {
        @Override
        public void excute() {
            Log.e("yang", " TaskQueueActivity exclusiveTask3 running");
        }
    };
}
