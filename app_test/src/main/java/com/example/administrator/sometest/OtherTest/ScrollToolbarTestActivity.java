package com.example.administrator.sometest.OtherTest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.administrator.sometest.R;

public class ScrollToolbarTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_toolbar_test);

        final EnoughScrollRecyclerView recyclerView = (EnoughScrollRecyclerView)findViewById(R.id.rv_friends);
        recyclerView.setAdapter(new IAdapter());
        ILayoutManager linearLayoutManager = new ILayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setBindView(findViewById(R.id.rl_title));


//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        count += 1;
//                        recyclerView.getAdapter().notifyDataSetChanged();
////                        recyclerView.updateScrollState();
//                    }
//                });
//            }
//        }, 1000, 2000);
    }

    int count = 3;

    class IAdapter extends RecyclerView.Adapter<IAdapter.IHoder>{

        @Override
        public IHoder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setHeight(100);
            return new IHoder(textView);
        }

        @Override
        public void onBindViewHolder(IHoder holder, int position) {
            holder.textView.setText(position+"");
        }

        @Override
        public int getItemCount() {
            return count;
        }

        class IHoder extends RecyclerView.ViewHolder{
            private TextView textView;

            public IHoder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }
    }

    class ILayoutManager extends LinearLayoutManager{
        private boolean canScroll = true;

        public ILayoutManager(Context context) {
            super(context);
        }

        public ILayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public void setScroll(boolean canScroll){
            this.canScroll = canScroll;
        }

        @Override
        public boolean canScrollVertically() {
            return canScroll;
        }
    }

}
