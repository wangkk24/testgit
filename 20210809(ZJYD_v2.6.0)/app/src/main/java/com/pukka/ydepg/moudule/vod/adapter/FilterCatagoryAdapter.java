package com.pukka.ydepg.moudule.vod.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Genre;

import java.util.List;

/**
 *
 *类别，流派过滤适配器
 * @author: ld
 * @date: 2017-12-19
 */

public class FilterCatagoryAdapter extends RecyclerView.Adapter<FilterCatagoryAdapter.ViewHolder> implements View.OnClickListener{

    private List<Genre> genreList;

    private int currentPosition;

    private View mSelectedView;
    private View.OnKeyListener mOnKeyListener;
    private OnItemClickListener mOnItemClickListener = null;


    public FilterCatagoryAdapter(List<Genre> list) {
        this.genreList = list;
    }

    public void setmOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movies_list_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.chooseIcon.setVisibility(View.INVISIBLE);
        holder.name.setText(genreList.get(position).getGenreName());
        holder.itemView.setTag(position);
        if(currentPosition == position){
            holder.itemView.setSelected(true);
            mSelectedView = holder.itemView;
            holder.chooseIcon.setVisibility(View.VISIBLE);
        }
    }

    public void setOnKeyListener(View.OnKeyListener listener) {
        mOnKeyListener = listener;
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    @Override
    public void onClick(View v) {
        currentPosition=(Integer) v.getTag();
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag(),mSelectedView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView chooseIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(FilterCatagoryAdapter.this);
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    TextView tv = (TextView) v.findViewById(R.id.movies_list_filter_item_text);
                    if(hasFocus){
                        tv.setSelected(true);
                        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    }else{
                        tv.setSelected(false);
                        tv.setEllipsize(TextUtils.TruncateAt.END);
                    }
                }
            });
            if (mOnKeyListener != null) {
                itemView.setOnKeyListener(mOnKeyListener);
            }
            name = (TextView) itemView.findViewById(R.id.movies_list_filter_item_text);
            chooseIcon = (ImageView) itemView.findViewById(R.id.movies_list_filter_item_img);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int position, View selectedView);
    }
}
