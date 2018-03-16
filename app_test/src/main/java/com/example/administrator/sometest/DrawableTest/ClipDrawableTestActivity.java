package com.example.administrator.sometest.DrawableTest;

import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.sometest.R;

public class ClipDrawableTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_drawable_test);
        clipDrawableTest();
    }


    int levelValue = 0;
    private void clipDrawableTest(){
        ImageView imageView = (ImageView)findViewById(R.id.tv1);
        final ClipDrawable clipDrawable = (ClipDrawable)imageView.getDrawable();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipDrawable.setLevel(levelValue+=100);
            }
        });
    }
}
