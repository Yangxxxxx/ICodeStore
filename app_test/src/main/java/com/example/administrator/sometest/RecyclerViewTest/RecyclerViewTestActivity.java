package com.example.administrator.sometest.RecyclerViewTest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.sometest.R;

public class RecyclerViewTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);

        RecyclerView recyclerView = findViewById(R.id.rc_view);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.HORIZONTAL, false));
//        new PagerSnapHelper().attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new PagerGridLayoutManager(2, 3, PagerGridLayoutManager.HORIZONTAL));
        new PagerGridSnapHelper().attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(new TestAdapter());
    }

    class TestAdapter extends  RecyclerView.Adapter<TestAdapter.TestHolder>{

        @Override
        public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_recylerview, parent, false);
            return new TestHolder(view);
        }

        @Override
        public void onBindViewHolder(TestHolder holder, int position) {
            ((TextView)holder.itemView).setText(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return 60;
        }

        class TestHolder extends  RecyclerView.ViewHolder{

            public TestHolder(View itemView) {
                super(itemView);
            }
        }
    }


}
