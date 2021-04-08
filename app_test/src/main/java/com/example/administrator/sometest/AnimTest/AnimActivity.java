package com.example.administrator.sometest.AnimTest;

import android.animation.ObjectAnimator;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.administrator.sometest.R;

public class AnimActivity extends AppCompatActivity {
    View textview01;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        textview01 = findViewById(R.id.tv1);
        textview01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AnimActivity.this, "clicked!", Toast.LENGTH_SHORT).show();
            }
        });

//        xmlAnimTest(R.anim.anim_translate01);
//        xmlAnimTest(R.anim.anim_set01);
//        animateTest();
        propertyTest();
    }

    private void xmlAnimTest(int animID){
        Animation animation = AnimationUtils.loadAnimation(this, animID);
//        animation.setFillAfter(true);//控件并没有移动
//        animation.setFillEnabled(true);
        textview01.startAnimation(animation);
    }

    private void animateTest(){
        textview01.animate()
                .alpha(0)
//                .translationY(300)
                .y(300)
                .setDuration(3000)
                .start();
    }

    private void propertyTest(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(textview01, "scaleX", 1f, 0.2f);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
    }
}
