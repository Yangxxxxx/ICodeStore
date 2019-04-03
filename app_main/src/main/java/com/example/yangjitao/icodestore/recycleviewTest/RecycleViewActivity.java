package com.example.yangjitao.icodestore.recycleviewTest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yangjitao.icodestore.CommonFunciton.GridItemDecoration;
import com.example.yangjitao.icodestore.R;

public class RecycleViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);

        recyclerView = (RecyclerView) findViewById(R.id.rcv_filters);
//        LinearLayoutManager cateLayoutManager = new LinearLayoutManager(this);
//        cateLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        Log.e("yang", "enter onCreate");
        recyclerView.setLayoutManager(new GridLayoutManager(this, 6));

        recyclerView.addItemDecoration(new GridItemDecoration(0, 0, 0, 0, 20));
//
        HomeAdapter cateAdapter = new HomeAdapter();
        recyclerView.setAdapter(cateAdapter);

    }


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            ImageView imageView = new ImageView(parent.getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return new MyViewHolder(imageView);
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            holder.imageView.setImageResource(R.mipmap.img);
        }

        @Override
        public int getItemCount()
        {
            return 139;
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            ImageView imageView;

            public MyViewHolder(View view)
            {
                super(view);
                imageView = (ImageView)view;
            }
        }
    }

}
