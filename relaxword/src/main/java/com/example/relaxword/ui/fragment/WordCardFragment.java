package com.example.relaxword.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.relaxword.R;
import com.example.relaxword.ui.Model;
import com.example.relaxword.ui.bean.Word;

import java.util.ArrayList;
import java.util.List;

public class WordCardFragment extends Fragment {
//    private static final String[] test_words = new String[]{"hello", "widget", "fragment", "bundle", "instance", "layout", "manager", "recycle"};

    private List<Word> wordList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_word_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
    }

    public String getShowingWord(){
        int pos = linearLayoutManager.findFirstVisibleItemPosition();
        return wordList.get(pos).getSpell();
    }

    private void initData(){
        wordList.clear();
        wordList.addAll(Model.getInstance().getAllWord());
    }

    private void initView(View view) {
        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView = view.findViewById(R.id.rc_word_list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new WordListAdapter());
        new PagerSnapHelper().attachToRecyclerView(recyclerView);
    }

    class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordListHolder> {


        @NonNull
        @Override
        public WordListAdapter.WordListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main_activity_list, viewGroup, false);
            return new WordListAdapter.WordListHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull WordListAdapter.WordListHolder wordListHolder, int i) {
            wordListHolder.wordContent.setText(wordList.get(i).getSpell());
        }

        @Override
        public int getItemCount() {
            return wordList.size();
        }

        class WordListHolder extends RecyclerView.ViewHolder {
            private TextView wordContent;

            public WordListHolder(@NonNull View itemView) {
                super(itemView);
                wordContent = itemView.findViewById(R.id.tv_word);
            }
        }
    }
}
