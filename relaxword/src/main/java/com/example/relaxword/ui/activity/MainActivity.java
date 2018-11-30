package com.example.relaxword.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.relaxword.R;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView(){
        recyclerView = findViewById(R.id.rc_word_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new WordListAdapter());
        new PagerSnapHelper().attachToRecyclerView(recyclerView);
    }

    private void initData(){

    }

    class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordListHolder>{


        @NonNull
        @Override
        public WordListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main_activity_list, viewGroup, false);
            return new WordListHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull WordListHolder wordListHolder, int i) {
            wordListHolder.wordContent.setText(String.valueOf(i));

            wordListHolder.wordContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, WordNetInfoActivity.class));
                }
            });
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        class WordListHolder extends RecyclerView.ViewHolder{
            private TextView wordContent;
            public WordListHolder(@NonNull View itemView) {
                super(itemView);
                wordContent = itemView.findViewById(R.id.tv_word);
            }
        }
    }
}
