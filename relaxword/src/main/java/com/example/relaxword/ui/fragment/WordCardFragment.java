package com.example.relaxword.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.relaxword.R;
import com.example.relaxword.ui.Model;
import com.example.relaxword.ui.Widget.CardRecyclerView;
import com.example.relaxword.ui.Widget.DragLayout;
import com.example.relaxword.ui.bean.Translation;
import com.example.relaxword.ui.bean.WebMeaning;
import com.example.relaxword.ui.bean.Word;
import com.example.relaxword.ui.ctrls.AudioPlaymanager;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class WordCardFragment extends Fragment implements Model.LoadWordListener, Model.UnknownWordChangeListener {
//    private static final String[] test_words = new String[]{"hello", "widget", "fragment", "bundle", "instance", "layout", "manager", "recycle"};

    private List<Word> wordList = new ArrayList<>();
    private CardRecyclerView recyclerView;
    private TextView unknownNumView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_word_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.removeOnScrollListener(onScrollListener);
        Model.getInstance().removeLoadWordListener(this);
        Model.getInstance().removeUnknownwordChangeListener(this);
    }

    @Override
    public void onWordsLoaded(List<Word> list) {
        wordList.addAll(list);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onUnknownWordChange(int unknownWordNum) {
        unknownNumView.setText(getString(R.string.unknown_word_title, unknownWordNum));
    }

    public String getShowingWord() {
        int pos = linearLayoutManager.findFirstVisibleItemPosition();
        return wordList.get(pos).getSpell();
    }

    private void initData() {
        wordList.clear();
        Model.getInstance().addUnknownWordChangeListener(this);
        Model.getInstance().addLoadWordListener(this);
        Model.getInstance().loadNextPageWord();
//        wordList.addAll(Model.getInstance().getAllWord());

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new WordListAdapter());
        new PagerSnapHelper().attachToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(onScrollListener);
    }

    private void initView(View view) {
        unknownNumView = view.findViewById(R.id.tv_unknownNum);
        recyclerView = view.findViewById(R.id.rc_word_list);
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (newState == SCROLL_STATE_IDLE && layoutManager instanceof LinearLayoutManager) {
                int lastPostion = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                if (wordList.size() - lastPostion <= 3) {
                    Model.getInstance().loadNextPageWord();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordListHolder>
            implements View.OnClickListener, DragLayout.DragRemoveListener {


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
            wordListHolder.phoneticUS.setText("/" + translation.getPhoneticUS() + "/");
            wordListHolder.phoneticUK.setText("/" + translation.getPhoneticUK() + "/");
            wordListHolder.meaning.setText(formatMeaning(translation.getMeanings(), "\n"));
            wordListHolder.webMeaning.setText(formatWebMeaning(translation.getWebMeanings()));
            wordListHolder.phoneticUK.setOnClickListener(this);
            wordListHolder.phoneticUS.setOnClickListener(this);
            wordListHolder.knownView.setOnClickListener(this);
            wordListHolder.phoneticUK.setTag(i);
            wordListHolder.phoneticUS.setTag(i);
            wordListHolder.knownView.setTag(i);
            wordListHolder.drawView.setCardRecyclerView(recyclerView);
            wordListHolder.drawView.setDragRemoveListener(this);
            wordListHolder.drawView.setTag(i);
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
            private TextView knownView;
            private DragLayout drawView;

            public WordListHolder(@NonNull View itemView) {
                super(itemView);
                wordContent = itemView.findViewById(R.id.tv_word);
                phoneticUK = itemView.findViewById(R.id.tv_phonetic_uk);
                phoneticUS = itemView.findViewById(R.id.tv_phonetic_us);
                meaning = itemView.findViewById(R.id.tv_meanning);
                webMeaning = itemView.findViewById(R.id.tv_web_meanning);
                knownView = itemView.findViewById(R.id.tv_know);
                drawView = itemView.findViewById(R.id.dl_layout);
            }
        }

        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            if (!(obj instanceof Integer)) return;
            int index = (int) obj;
            Word word = wordList.get(index);

            switch (v.getId()) {
                case R.id.tv_know:
                    wordList.remove(index);
                    Model.getInstance().addKnownWord(word);
                    notifyItemRemoved(index);
                    notifyDataSetChanged();
                    break;
                case R.id.tv_phonetic_uk:
                    Translation translationUK = word.getTranslation();
                    if(translationUK != null) {
                        AudioPlaymanager.getInstance().playSound(translationUK.getVoiceUKurl(), null);
                    }
                    break;
                case R.id.tv_phonetic_us:
                    Translation translationUS = word.getTranslation();
                    AudioPlaymanager.getInstance().playSound(translationUS.getVoiceUSurl(), new AudioPlaymanager.PlayListener() {
                        @Override
                        public void onPlayEnd() {
                            Log.e("yang", "onPlayend");
                        }
                    });
                    break;
            }

        }

        @Override
        public void onDragRemove(View v) {
            Object obj = v.getTag();
            if (!(obj instanceof Integer)) return;
            int index = (int) obj;
            Word word = wordList.get(index);

            wordList.remove(index);
            Model.getInstance().addKnownWord(word);
            notifyItemRemoved(index);
            notifyDataSetChanged();
        }

        private String formatMeaning(List<String> list, String seprator) {
            if (list == null) return "";
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                result.append(list.get(i));
                if (i != list.size() - 1) result.append(seprator);
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
