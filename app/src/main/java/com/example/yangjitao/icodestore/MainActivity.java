package com.example.yangjitao.icodestore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.yangjitao.icodestore.NotificationTest.NotificationTestActivity;
import com.example.yangjitao.icodestore.drawableTest.DrawableTestActivity;
import com.example.yangjitao.icodestore.recycleviewTest.RecycleViewActivity;

public class MainActivity extends AppCompatActivity {
    LinearLayout layoutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutContent = (LinearLayout) findViewById(R.id.ll_content);

        addActivityButton(RecycleViewActivity.class, "RecycleView Test");
        addActivityButton(DrawableTestActivity.class, "Drawable Test");
        addActivityButton(NotificationTestActivity.class, "Notification Test");

    }

    private void addActivityButton(final Class c, String description){
        Button button = new Button(this);
        button.setText(description);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, c));
            }
        });

        layoutContent.addView(button);
    }


}
