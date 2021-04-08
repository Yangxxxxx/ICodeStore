package com.example.administrator.sometest.DialTest;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

public class ItemSelectHelper {
    private final static int[] ROLL_ORDER = new int[]{0, 1, 2, 5, 8, 7, 6, 3};
    private RecyclerView recyclerView;
    private RollCallBack rollCallBack;
    private int finishPos = -1;
    private boolean forceStop = false;

    public ItemSelectHelper(RollCallBack rollCallBack){
        this.rollCallBack = rollCallBack;
    }

    public void attachToRecyclerView(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }

    public void run(){
        resetData();
        notifyChange(0);
    }

    public void stop(int stopPos){
        this.finishPos = stopPos;
    }

    public void forceStop(){
        forceStop = true;
    }

    private void notifyChange(final int num){
        Log.e("yang", "enter notifyChange");
        final int currPos = num % ROLL_ORDER.length;
        final int prePos = (num - 1) % ROLL_ORDER.length;
        boolean atLeastOneRound = num >= ROLL_ORDER.length;//至少转一圈
        final boolean isFinish = (finishPos == currPos) && atLeastOneRound;

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                int currIndex = ROLL_ORDER[currPos];
                int preIndex = (prePos < 0) ? -1 : ROLL_ORDER[prePos];
                rollCallBack.onItemRolling(recyclerView.findViewHolderForAdapterPosition(currIndex),
                        recyclerView.findViewHolderForAdapterPosition(preIndex));

                if(isFinish){
                    rollCallBack.onItemSelected(recyclerView.findViewHolderForAdapterPosition(currIndex));
                }
            }
        });

        if(!isFinish && !forceStop) {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyChange(num + 1);
                }
            }, 200);
        }
    }

    private void resetData(){
        forceStop = false;
        finishPos = -1;
    }

    public interface RollCallBack{
        void onItemRolling(RecyclerView.ViewHolder currViewHolder, RecyclerView.ViewHolder preViewHolder);
        void onItemSelected(RecyclerView.ViewHolder viewHolder);
    }
}
