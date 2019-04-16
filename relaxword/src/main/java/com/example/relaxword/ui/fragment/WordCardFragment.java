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
import com.example.relaxword.ui.bean.Translation;
import com.example.relaxword.ui.bean.WebMeaning;
import com.example.relaxword.ui.bean.Word;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class WordCardFragment extends Fragment implements Model.LoadWordListener{
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.removeOnScrollListener(onScrollListener);
        Model.getInstance().removeLoadWordListener(this);
    }

    @Override
    public void onWordsLoaded(List<Word> list) {
        wordList.addAll(list);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public String getShowingWord() {
        int pos = linearLayoutManager.findFirstVisibleItemPosition();
        return wordList.get(pos).getSpell();
    }

    private void initData() {
        wordList.clear();
        Model.getInstance().addLoadWordListener(this);
        Model.getInstance().loadNextPageWord();
//        wordList.addAll(Model.getInstance().getAllWord());
    }

    private void initView(View view) {
        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView = view.findViewById(R.id.rc_word_list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new WordListAdapter());
        new PagerSnapHelper().attachToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(onScrollListener);
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if(newState == SCROLL_STATE_IDLE && layoutManager instanceof LinearLayoutManager){
                int lastPostion = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                if(wordList.size() - lastPostion <= 3){
                    Model.getInstance().loadNextPageWord();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordListHolder> {


        @NonNull
        @Override
        public WordListAdapter.WordListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main_activity_list, viewGroup, false);
            return new WordListAdapter.WordListHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull WordListAdapter.WordListHolder wordListHolder, int i) {
            Word word = wordList.get(i);
            Translation translation = word.getTranslation();
            wordListHolder.wordContent.setText(wordList.get(i).getSpell());
            wordListHolder.phoneticUS.setText("美：[" + translation.getPhoneticUS() + "]");
            wordListHolder.phoneticUK.setText("英：[" + translation.getPhoneticUK() + "]");
            wordListHolder.meaning.setText(formatMeaning(translation.getMeanings(), "\n"));
            wordListHolder.webMeaning.setText(formatWebMeaning(translation.getWebMeanings()));
        }

        @Override
        public int getItemCount() {
            return wordList.size();
        }

        class WordListHolder extends RecyclerView.ViewHolder {
            private TextView wordContent;
            private TextView phoneticUS;
            private TextView phoneticUK;
            private TextView meaning;
            private TextView webMeaning;

            public WordListHolder(@NonNull View itemView) {
                super(itemView);
                wordContent = itemView.findViewById(R.id.tv_word);
                phoneticUK = itemView.findViewById(R.id.tv_phonetic_uk);
                phoneticUS = itemView.findViewById(R.id.tv_phonetic_us);
                meaning = itemView.findViewById(R.id.tv_meanning);
                webMeaning = itemView.findViewById(R.id.tv_web_meanning);
            }
        }

        private String formatMeaning(List<String> list, String seprator) {
            if(list == null) return "";
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                result.append(list.get(i));
                if(i != list.size() - 1) result.append(seprator);
            }
            return result.toString();
        }

        private String formatWebMeaning(List<WebMeaning> list) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                result.append(String.valueOf(i + 1));
                result.append(".");
                result.append(list.get(i).getKey());
                result.append(":\n   ");
                result.append(formatMeaning(list.get(i).getMeanings(), "; "));
                result.append(":\n");
                if (i != list.size() - 1) result.append("\n");
            }
            return result.toString();
        }
    }
}
