package com.pukka.ydepg.moudule.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.uiutil.Colors;
import com.pukka.ydepg.moudule.search.bean.SearchActorBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActorAdapter extends RecyclerView.Adapter<SearchActorAdapter.SearchActorViewHolder>{
    private Context mContext;

    private List<SearchActorBean> mData;

    private SearchActorAdapter.OnItemClickListener mOnItemClickListener;

    private int currentPosition = -1;

    public SearchActorAdapter(Context context, SearchActorAdapter.OnItemClickListener itemClickListener){
        mContext = context;
        mOnItemClickListener = itemClickListener;
    }

    public void setmData(List<SearchActorBean> data){
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
    public void onBindViewHolder(SearchActorAdapter.SearchActorViewHolder holder, int position) {
        holder.tv.setText(mData.get(position).getName());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mOnItemClickListener){
                    mOnItemClickListener.onSearchActorItemClick(mData.get(position));
                }
            }
        });
        holder.item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    currentPosition = position;
                    holder.tv.setSelected(true);
                }
                else{
                    currentPosition = -1;
                    holder.tv.setSelected(false);
                }
            }
        });
    }

    @Override
    public SearchActorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchActorViewHolder(LayoutInflater.from(mContext).inflate(R.layout.search_actor_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return null == mData ? 0 : mData.size();
    }

    class SearchActorViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.actor_item_layout)
        View item;
        @BindView(R.id.actor_item_text)
        TextView tv;
        public SearchActorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClickListener{
        void onSearchActorItemClick(SearchActorBean actorBean);
    }
    public void setOnItemClickListener(SearchActorAdapter.OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
