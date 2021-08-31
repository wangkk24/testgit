package com.pukka.ydepg.moudule.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.uiutil.Colors;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EveryoneSearchAdapter extends RecyclerView.Adapter<EveryoneSearchAdapter.SearchAssociationViewHolder>{

    private Context mContext;

    private List<String> mData;

    private OnItemClickListener mOnItemClickListener;

    private int currentPosition = -1;

    public EveryoneSearchAdapter(Context context, OnItemClickListener itemClickListener){
        mContext = context;
        mOnItemClickListener = itemClickListener;
    }

    public void setmData(List<String> data){
        if(null == mData){
            mData = new ArrayList<>();
        }
        mData.clear();
        if(null != data && data.size() > 0){
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(SearchAssociationViewHolder holder, int position) {
        holder.tv.setText(mData.get(position));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mOnItemClickListener){
                    mOnItemClickListener.onEveryoneSearchItemClick(mData.get(position));
                }
            }
        });
        holder.item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    currentPosition = position;
                    holder.item.setBackgroundResource(R.drawable.activity_search_border);
                    holder.tv.setTextColor(Colors.getInstance().getColor(mContext.getResources(), R.color.c21_color));
                    holder.tv.setSelected(true);
                }
                else{
                    currentPosition = -1;
                    holder.item.setBackgroundResource(android.R.color.transparent);
                    holder.tv.setTextColor(Colors.getInstance().getColor(mContext.getResources(), R.color.c24_color));
                    holder.tv.setSelected(false);
                }
            }
        });
    }

    @Override
    public SearchAssociationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchAssociationViewHolder(LayoutInflater.from(mContext).inflate(R.layout.search_everyone_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return null == mData ? 0 : mData.size();
    }

    class SearchAssociationViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.association_item_layout)
        View item;
        @BindView(R.id.association_item_text)
        TextView tv;
        SearchAssociationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClickListener{
        void onEveryoneSearchItemClick(String key);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
