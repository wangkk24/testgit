package com.pukka.ydepg.moudule.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 鍵盤适配器
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.search.adapter.KeyboardCharAdapter.java
 * @author:xj
 * @date: 2017-12-15 15:20
 */

public class KeyboardNumberAdapter extends RecyclerView.Adapter<KeyboardNumberAdapter.KeyboardViewHolder> {

    Context mContext;

    private final int POSITION_NUMBER_5 = 4;

    private View firstFocusView;

    List<String> mKeyboardList;
    private OnItemClickListener mOnItemClickListener;
    private int mCurrentLine = -1;

    public KeyboardNumberAdapter(Context mContext, List<String> mKeyboardList) {
        this.mContext = mContext;
        this.mKeyboardList = mKeyboardList;
    }

    @Override
    public KeyboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_keyboard_number_item, parent, false);
        return new KeyboardViewHolder(view);
    }


    @Override
    public void onBindViewHolder(KeyboardViewHolder holder, int position) {
         holder.tv.setText(mKeyboardList.get(position));
         holder.tv.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (null != mOnItemClickListener){
                     mOnItemClickListener.onNumberItemClick(mKeyboardList.get(position));
                 }
             }
         });
        holder.tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mCurrentLine = position / 3;
                }
            }
        });

        //字母M为默认落焦位置
        if (position == POSITION_NUMBER_5) {
            holder.itemView.requestFocus();
            firstFocusView = holder.itemView;
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return null == mKeyboardList ? 0 : mKeyboardList.size();
    }

    class KeyboardViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.keyboard_item_text)
        TextView tv;
        @BindView(R.id.keyboard_item_layout)
        View keyboardItemLayout;
        public KeyboardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public interface OnItemClickListener{
        void onNumberItemClick(String key);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public int getCurrentLine(){
        return mCurrentLine;
    }

    public void requestFirstFocus(){
        if(firstFocusView != null){
            firstFocusView.requestFocus();
        }
    }
}