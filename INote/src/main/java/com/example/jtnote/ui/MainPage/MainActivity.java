package com.example.jtnote.ui.MainPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jtnote.Constants;
import com.example.jtnote.R;
import com.example.jtnote.TestCode.TestInfoActivity;
import com.example.jtnote.bean.NoteItem;
import com.example.jtnote.service.NoteService;
import com.example.jtnote.ui.KeyboardActivity;
import com.example.jtnote.utils.SingleViewHelper;
import com.example.jtnote.widget.GestureDetectLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainPageContract.View{
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private final int VIEW_HOLDER_TAG_KEY = R.id.view_holder_tag_key;

    private List<NoteItem> noteItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private View funcLayout;
    private View deleteLayout;
    private View dragHintLayout;
    private MainPageContract.Presenter presenter;
    private SingleViewHelper singleViewHelper = new SingleViewHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        startService(new Intent(this, NoteService.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_text:
//                presenter.textEntryClick();
                KeyboardActivity.start(this, Constants.KEYBOARD_ACTIVITY_REQUESTCODE);
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
//        deleteLayout.setVisibility(View.VISIBLE);
//        funcLayout.setVisibility(View.INVISIBLE);
        singleViewHelper.show(deleteLayout);
    }

    @Override
    public void turnNormalMode() {
//        deleteLayout.setVisibility(View.INVISIBLE);
//        funcLayout.setVisibility(View.VISIBLE);
        singleViewHelper.show(funcLayout);
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
        if(requestCode == Constants.KEYBOARD_ACTIVITY_REQUESTCODE && resultCode == RESULT_OK && data != null){
            String inputStr = data.getStringExtra(Constants.KEY_INPUT_CONTENT);
            boolean goMorePage = data.getBooleanExtra(Constants.KEY_GO_MORE_PAGE, false);
            if(TextUtils.isEmpty(inputStr)) return;
            presenter.newTextContent(inputStr, goMorePage);
        }
    }

    private class NoteContentAdapter extends RecyclerView.Adapter<NoteContentAdapter.NoteContentHolder> implements GestureDetectLayout.OnGestureListener {


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

            holder.gestureDetectLayout.setSelected(presenter.isNoteSelected(noteItem));
            holder.gestureDetectLayout.setOnGestureListener(this);
            holder.gestureDetectLayout.setTag(noteItem);
            holder.gestureDetectLayout.setTag(VIEW_HOLDER_TAG_KEY, holder);
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
        public void onLongClick(View v, boolean pressOnBorder) {
            if(pressOnBorder){
                Object object = v.getTag(VIEW_HOLDER_TAG_KEY);
                if(object instanceof RecyclerView.ViewHolder){
                    itemTouchHelper.startDrag((RecyclerView.ViewHolder)object);
                }
            }else {
                presenter.turnDeleteMode();
                presenter.noteItemClick((NoteItem)v.getTag());
            }
        }

        public class NoteContentHolder extends RecyclerView.ViewHolder{
            private TextView textContent;
            private TextView alarmHint;
            private View expireView;
            private GestureDetectLayout gestureDetectLayout;

            public NoteContentHolder(View itemView) {
                super(itemView);
                textContent = itemView.findViewById(R.id.tv_content);
                alarmHint = itemView.findViewById(R.id.tv_alarm_hint);
                expireView = itemView.findViewById(R.id.view_expire);
                gestureDetectLayout = itemView.findViewById(R.id.item_root);
            }
        }
    }

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //首先回调的方法 返回int表示是否监听该方向
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//线性排列时监听到的为上下动作则为：拖拽排序
//            int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;//线性排列时监听到的为左右动作时则为：侧滑删除
            return makeMovementFlags(dragFlags, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //滑动事件
            //交换位置
            Collections.swap(noteItemList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
            //刷新adapter
            recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//            //侧滑事件
//            noteItemList.remove(viewHolder.getAdapterPosition());
//            //刷新adapter
//            recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            singleViewHelper.show(dragHintLayout);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            singleViewHelper.show(funcLayout);
        }
    });


    private void initView(){
        recyclerView = findViewById(R.id.rc_note_board);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NoteContentAdapter());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        funcLayout = findViewById(R.id.tv_text);
        deleteLayout = findViewById(R.id.tv_delete);
        dragHintLayout = findViewById(R.id.tv_drag_hint);
        funcLayout.setOnClickListener(this);
        deleteLayout.setOnClickListener(this);
        singleViewHelper.add(funcLayout);
        singleViewHelper.add(deleteLayout);
        singleViewHelper.add(dragHintLayout);

        //testcode
        funcLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(MainActivity.this, TestInfoActivity.class));
                return true;
            }
        });
    }

    private void initData(){
        presenter = new MainPagePresenter(this, this);
    }

}
