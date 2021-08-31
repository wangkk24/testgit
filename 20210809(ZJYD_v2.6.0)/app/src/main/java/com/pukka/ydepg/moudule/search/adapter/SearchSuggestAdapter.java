package com.pukka.ydepg.moudule.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.utils.uiutil.Colors;
import com.pukka.ydepg.launcher.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.search.adapter.SearchSuggestAdapter.java
 * @author:xj
 * @date: 2017-12-20 09:46
 */
public class SearchSuggestAdapter extends RecyclerView.Adapter<SearchSuggestAdapter.SearchSuggestViewHolder> {

    private Context mContext;

    private List<String> mData;

    private SearchSuggestAdapter.OnItemClickListener mOnItemClickListener;

    private boolean isOnItemClick = false;

    private int clickPosition = -1;

    private int currentPosition = -1;

    public SearchSuggestAdapter(Context context, SearchSuggestAdapter.OnItemClickListener itemClickListener){
        mContext = context;
        mOnItemClickListener = itemClickListener;
    }

    public void setmData(List<String> data){
        if(null == mData){
            mData = new ArrayList<>();
        }
        isOnItemClick = false;
        clickPosition = -1;
        mData.clear();
        if(null != data && data.size() > 0){
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(SearchSuggestAdapter.SearchSuggestViewHolder holder, int position) {
        holder.tv.setText(mData.get(position));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mOnItemClickListener){
                    isOnItemClick = true;
                    clickPosition = position;
                    mOnItemClickListener.onAssociationItemClick(mData.get(position));
                }
            }
        });
        holder.item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    currentPosition = position;
                    if(isOnItemClick){
                        notifyDataSetChanged();
                    }
                    isOnItemClick = false;
                    clickPosition = -1;
                    holder.item.setBackgroundResource(R.drawable.activity_search_border);
                    holder.tv.setTextColor(Colors.getInstance().getColor(mContext.getResources(), R.color.white_0));
                }
                else{
                    if(!isOnItemClick) {
                        holder.item.setBackgroundResource(android.R.color.transparent);
                        holder.tv.setTextColor(Colors.getInstance().getColor(mContext.getResources(), R.color.c02_color));
                    }
                }
            }
        });

        holder.item.setBackgroundResource(android.R.color.transparent);
        holder.tv.setTextColor(Colors.getInstance().getColor(mContext.getResources(), R.color.c24_color));

        if(position == currentPosition){
            holder.item.requestFocus();
        }
    }

    @Override
    public SearchSuggestAdapter.SearchSuggestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchSuggestAdapter.SearchSuggestViewHolder(LayoutInflater.from(mContext).inflate(R.layout.search_suggest_layout, parent, false));
    }

    @Override
    public int getItemCount() {
        return null == mData ? 0 : mData.size();
    }

    class SearchSuggestViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.search_suggest_item_layout)
        View item;
        @BindView(R.id.search_suggest_item_text)
        TextView tv;
        public SearchSuggestViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClickListener{
        void onAssociationItemClick(String key);
    }
    public void setOnItemClickListener(SearchSuggestAdapter.OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public boolean isOnItemClick() {
        return isOnItemClick;
    }

    public int getClickPosition() {
        return clickPosition;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void resetCurrentPosition(){
        currentPosition = -1;
    }

    public static boolean isKeySuggestOpen(){
        String suggestSwitch = CommonUtil.getConfigValue(Constant.SEARCH_SUGGEST_SWITCH); //0:关  1开
        if ( !"0".equals(suggestSwitch) || BuildConfig.DEBUG ){
            return true;
        } else {
            return false;
        }
    }
}
