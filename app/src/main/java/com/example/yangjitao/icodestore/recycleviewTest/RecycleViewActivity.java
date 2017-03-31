package com.example.yangjitao.icodestore.recycleviewTest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yangjitao.icodestore.CommonFunciton.DividerGridItemDecoration;
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
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));

        HomeAdapter cateAdapter = new HomeAdapter();
        recyclerView.setAdapter(cateAdapter);
    }


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
//            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(RecycleViewActivity.this).inflate(R.layout.recycleview_list_item, parent, false));
            TextView textView = new TextView(parent.getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
            textView.setLayoutParams(layoutParams);
            textView.setBackgroundColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);

            return new MyViewHolder(textView);
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            holder.textView.setText(position+"");
        }

        @Override
        public int getItemCount()
        {
            return 39;
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            TextView textView;

            public MyViewHolder(View view)
            {
                super(view);
                textView = (TextView) view;
            }
        }
    }



//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recycleview);
//
//        recyclerView = (RecyclerView) findViewById(R.id.rcv_filters);
//        LinearLayoutManager cateLayoutManager = new LinearLayoutManager(this);
//        cateLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView.setLayoutManager(cateLayoutManager);
//        HomeAdapter cateAdapter = new HomeAdapter();
//        recyclerView.setAdapter(cateAdapter);
//    }
//
//
//    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
//    {
//
//        int[] previewID = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
//                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
//                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
//
//        public HomeAdapter() {
//        }
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
//        {
//            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(RecycleViewActivity.this).inflate(R.layout.recycleview_list_item, parent, false));
//            return holder;
//        }
//        @Override
//        public void onBindViewHolder(MyViewHolder holder, int position)
//        {
//            holder.iv.setImageResource(previewID[position]);
//        }
//
//        @Override
//        public int getItemCount()
//        {
//            return previewID.length;
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder
//        {
//
//            ImageView iv;
//
//            public MyViewHolder(View view)
//            {
//                super(view);
//                iv = (ImageView) view.findViewById(R.id.imageView);
//            }
//        }
//    }
}
