package com.example.yangjitao.icodestore.drawableTest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.yangjitao.icodestore.R;

/**
 * Created by user1 on 2016/11/2.
 */
public class DrawableTestActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_test);
        findViewById(R.id.bt_select).setOnClickListener(this);

        //git测试
        //设置监听才有点击效果
        findViewById(R.id.one_pic).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {/** git测试 local add */
        int id = v.getId();
        switch (id){
            case R.id.bt_select:
                v.setSelected(!v.isSelected());
            break;
        }
    }
}
