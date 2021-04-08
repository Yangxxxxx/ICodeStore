package com.example.jtnote.widget;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jtnote.R;


/**
 * Created by Administrator on 2018/8/27 0027.
 */

public class NumberPanel extends RecyclerView{
    private final int MAX_NUMBER = 60;
    private int rangeMin = 0;
    private int rangeMax = MAX_NUMBER;
    private int preSelectNumberIndex = -1;
    private int selectNumberIndex = -1;
    private NumberSelectListener numberSelectListener;

    public NumberPanel(Context context) {
        this(context, null);
    }

    public NumberPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberPanel(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setRange(int rangeMin, int rangeMax){
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        if(getAdapter() != null){
            getAdapter().notifyDataSetChanged();
        }
    }

    public void setSelectNumberIndex(int selectNumberIndex){
        this.selectNumberIndex = selectNumberIndex;
        if(getAdapter() != null){
            getAdapter().notifyDataSetChanged();
        }
    }

    public void setNumberSelectListener(NumberSelectListener numberSelectListener){
        this.numberSelectListener = numberSelectListener;
    }

    class NumberAdapter extends Adapter<NumberAdapter.NumberViewHolder> implements OnClickListener{

        @Override
        public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_number_panel, parent, false);
            return new NumberViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NumberViewHolder holder, int position) {
            String number = String.format("%02d", position);
            holder.contentView.setText(number);
            boolean validRange = position >= rangeMin && position <= rangeMax;
            holder.contentView.setSelected(validRange); //将不可点击部分置灰
            if(validRange) {
                holder.contentView.setTag(position);
                holder.contentView.setOnClickListener(this);
                holder.contentView.setEnabled(position != selectNumberIndex); //将选择的item高亮
            }
        }

        @Override
        public int getItemCount() {
            return MAX_NUMBER;
        }

        @Override
        public void onClick(View v) {
            if(numberSelectListener == null) return;
            int pos = (int)v.getTag();
            numberSelectListener.OnNumberSelect(pos);
            preSelectNumberIndex = selectNumberIndex;
            selectNumberIndex = pos;
            notifyItemChanged(pos);
            notifyItemChanged(preSelectNumberIndex);
        }

        class NumberViewHolder extends ViewHolder{
            private TextView contentView;

            public NumberViewHolder(View itemView) {
                super(itemView);
                contentView = itemView.findViewById(R.id.tv_content);
            }
        }
    }


    private void init(){
        setBackgroundColor(Color.WHITE);
        ((DefaultItemAnimator)getItemAnimator()).setSupportsChangeAnimations(false);
        LayoutManager layoutManager = new GridLayoutManager(getContext(), 8);
        setLayoutManager(layoutManager);
        setAdapter(new NumberAdapter());
    }

    public interface NumberSelectListener{
        void OnNumberSelect(int num);
    }
}
