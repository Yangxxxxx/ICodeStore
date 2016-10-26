package com.example.yangjitao.icodestore.recycleviewTest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.yangjitao.icodestore.R;

import java.util.List;

public class RecycleViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);

        recyclerView = (RecyclerView) findViewById(R.id.rcv_filters);
        LinearLayoutManager cateLayoutManager = new LinearLayoutManager(this);
        cateLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(cateLayoutManager);
        HomeAdapter cateAdapter = new HomeAdapter();
        recyclerView.setAdapter(cateAdapter);
    }


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        int[] previewID = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};

        public HomeAdapter() {
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(RecycleViewActivity.this).inflate(R.layout.recycleview_list_item, parent, false));
            return holder;
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            holder.iv.setImageResource(previewID[position]);
        }

        @Override
        public int getItemCount()
        {
            return previewID.length;
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            ImageView iv;

            public MyViewHolder(View view)
            {
                super(view);
                iv = (ImageView) view.findViewById(R.id.imageView);
            }
        }
    }
}
