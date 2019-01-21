package com.example.administrator.sometest.DialTest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.administrator.sometest.R;

public class DialTestActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial_test);
        initView();

        findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSelectHelper.run();

                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        itemSelectHelper.stop(1);
                    }
                }, 3000);
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.e("yang", "DialTestActivity ondestroy");
        super.onDestroy();
        itemSelectHelper.forceStop();
    }

    private void initView(){
        recyclerView = findViewById(R.id.lottery_panel);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new LotteryPanelAdapter());
        itemSelectHelper.attachToRecyclerView(recyclerView);
    }


    private ItemSelectHelper itemSelectHelper = new ItemSelectHelper(new ItemSelectHelper.RollCallBack() {
        @Override
        public void onItemRolling(RecyclerView.ViewHolder currViewHolder, RecyclerView.ViewHolder preViewHolder) {
            if(preViewHolder instanceof LotteryPanelAdapter.LotteryPanelHolder){
                ((LotteryPanelAdapter.LotteryPanelHolder)preViewHolder).coverView.setVisibility(View.INVISIBLE);
            }

            if(currViewHolder instanceof LotteryPanelAdapter.LotteryPanelHolder){
                ((LotteryPanelAdapter.LotteryPanelHolder)currViewHolder).coverView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onItemSelected(RecyclerView.ViewHolder viewHolder) {

        }
    });


    class LotteryPanelAdapter extends RecyclerView.Adapter<LotteryPanelAdapter.LotteryPanelHolder>{

        @Override
        public LotteryPanelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift, parent, false);
            return new LotteryPanelHolder(view);
        }

        @Override
        public void onBindViewHolder(LotteryPanelHolder holder, int position) {
            holder.contentView.setText(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return 9;
        }

        class LotteryPanelHolder extends RecyclerView.ViewHolder{
            private TextView contentView;
            private TextView coverView;

            public LotteryPanelHolder(View itemView) {
                super(itemView);
                contentView = itemView.findViewById(R.id.tv_content);
                coverView = itemView.findViewById(R.id.tv_cover);
            }
        }
    }
}
