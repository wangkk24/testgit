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
public class KeyboardCharAdapter extends RecyclerView.Adapter<KeyboardCharAdapter.KeyboardViewHolder> {

    Context mContext;

    private final int POSITION_LETTER_M = 12;

    List<String> mKeyboardList;
    private OnItemClickListener mOnItemClickListener;
    private int mCurrentLine = -1;

    //这两个变量用来配合儿童版页面设置默认落焦
    private View firstFocusView;
    private boolean needRequestFocus = false;

    public KeyboardCharAdapter(Context mContext, List<String> mKeyboardList) {
        this.mContext = mContext;
        this.mKeyboardList = mKeyboardList;
    }

    @Override
    public KeyboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_keyboard_item, parent, false);
        return new KeyboardViewHolder(view);
    }


    @Override
    public void onBindViewHolder(KeyboardViewHolder holder, int position) {
        holder.tv.setText(mKeyboardList.get(position));
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemClickListener){
                    mOnItemClickListener.onItemClick(mKeyboardList.get(position));
                }
            }
        });
        holder.tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mCurrentLine = position / 5;
                }
            }
        });
        //字母M为默认落焦位置
        if (position == POSITION_LETTER_M) {
            if( needRequestFocus ){
                holder.itemView.requestFocus();
                needRequestFocus = false;
            }
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
        void onItemClick(String key);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public int getCurrentLine(){
        return mCurrentLine;
    }

    //此方法调用时KeyboardAdapter是否初始化完成不确定,因此若调用时未初始化完成则设置标志位,
    //待其初始化时在onBindViewHolder中完成焦点设置
    public void requestFirstFocus(boolean isFromNumber){
        if(firstFocusView != null && !isFromNumber){
            firstFocusView.requestFocus();
        } else {
            needRequestFocus = true;
        }
    }
}