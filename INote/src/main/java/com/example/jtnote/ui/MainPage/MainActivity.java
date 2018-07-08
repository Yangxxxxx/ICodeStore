package com.example.jtnote.ui.MainPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jtnote.Constants;
import com.example.jtnote.R;
import com.example.jtnote.bean.NoteItem;
import com.example.jtnote.ui.KeyboardActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainPageContract.View{
    private static final int KEYBOARD_ACTIVITY_REQUESTCODE = 100;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private List<NoteItem> noteItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private View funcLayout;
    private View deleteLayout;
    private MainPageContract.Presenter presenter;
    private List<NoteItem> selectedList = new ArrayList<>();
    private boolean isDeleteMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_text:
//                presenter.textEntryClick();
                KeyboardActivity.start(this, KEYBOARD_ACTIVITY_REQUESTCODE);
                break;
            case R.id.tv_delete:
                presenter.deleteSelectNotes();
                break;
        }
    }

    @Override
    public void notesChange(List<NoteItem> noteList) {
        noteItemList.clear();
        noteItemList.addAll(noteList);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void selecteNotesChange(List<NoteItem> noteItemList) {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void turnDeleteMode() {
        deleteLayout.setVisibility(View.VISIBLE);
        funcLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void turnNormalMode() {
        deleteLayout.setVisibility(View.INVISIBLE);
        funcLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if(presenter.onBackPress()) return;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == KEYBOARD_ACTIVITY_REQUESTCODE && resultCode == RESULT_OK && data != null){
            String inputStr = data.getStringExtra(Constants.KEY_INPUT_CONTENT);
            if(TextUtils.isEmpty(inputStr)) return;
            presenter.newTextContent(inputStr);
        }
    }

    private class NoteContentAdapter extends RecyclerView.Adapter<NoteContentAdapter.NoteContentHolder> implements View.OnClickListener, View.OnLongClickListener{


        @Override
        public NoteContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_note, parent, false);
            return new NoteContentHolder(itemView);
        }

        @Override
        public void onBindViewHolder(NoteContentHolder holder, int position) {
            NoteItem noteItem = noteItemList.get(position);
            holder.textContent.setText(noteItem.getTextContent());

            if(noteItem.getAlarmTime() == 0){
                holder.alarmHint.setVisibility(View.INVISIBLE);
                holder.expireView.setVisibility(View.INVISIBLE);
            }else {
                boolean noExpire = noteItem.getAlarmTime() > System.currentTimeMillis();
                holder.alarmHint.setVisibility(View.VISIBLE );
                holder.alarmHint.setText(simpleDateFormat.format(noteItem.getAlarmTime()));
                holder.expireView.setVisibility(noExpire ? View.INVISIBLE : View.VISIBLE);
            }

            holder.itemView.setSelected(presenter.isNoteSelected(noteItem));
            holder.itemView.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);
            holder.itemView.setTag(noteItem);
        }

        @Override
        public int getItemCount() {
            return noteItemList.size();
        }

        @Override
        public void onClick(View v) {
            presenter.noteItemClick((NoteItem)v.getTag());
        }

        @Override
        public boolean onLongClick(View v) {
            presenter.turnDeleteMode();
            presenter.noteItemClick((NoteItem)v.getTag());
            return true;
        }

        public class NoteContentHolder extends RecyclerView.ViewHolder{
            private TextView textContent;
            private TextView alarmHint;
            private View expireView;

            public NoteContentHolder(View itemView) {
                super(itemView);
                textContent = itemView.findViewById(R.id.tv_content);
                alarmHint = itemView.findViewById(R.id.tv_alarm_hint);
                expireView = itemView.findViewById(R.id.view_expire);
            }
        }
    }

    private void initView(){
        recyclerView = findViewById(R.id.rc_note_board);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NoteContentAdapter());

        funcLayout = findViewById(R.id.tv_text);
        deleteLayout = findViewById(R.id.tv_delete);
        funcLayout.setOnClickListener(this);
        deleteLayout.setOnClickListener(this);
    }

    private void initData(){
        presenter = new MainPagePresenter(this, this);
    }

}
