package com.pukka.ydepg.moudule.vod.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.SortFilterBean;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import java.util.List;

/**
 *
 *筛选排序适配器
 * @author: ld
 * @date: 2017-12-19
 */
public class FilterSortAdapter extends RecyclerView.Adapter<FilterSortAdapter.ViewHolder> implements View.OnClickListener{

    private List<SortFilterBean> sortFilterBeans;

    private int currentPosition;
    private View mSelectedView;
    private View.OnKeyListener mOnKeyListener;
    private OnItemClickListener onItemClickListener = null;
    public FilterSortAdapter(List<SortFilterBean> list) {
        this.sortFilterBeans = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movies_list_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SuperLog.info("lx","onBindViewHolder");
        holder.chooseIcon.setVisibility(View.INVISIBLE);
        holder.name.setText(sortFilterBeans.get(position).getName());
        holder.itemView.setTag(position);
        if(currentPosition ==position){
            holder.itemView.setSelected(true);
            mSelectedView = holder.itemView;
            holder.chooseIcon.setVisibility(View.VISIBLE);
        }
        /*if (position == 0) {
            holder.itemView.requestFocus();
        }*/
    }

    public void setOnKeyListener(View.OnKeyListener listener) {
        mOnKeyListener = listener;
    }

    @Override
    public int getItemCount() {
        return sortFilterBeans.size();
    }

    @Override
    public void onClick(View v) {
        currentPosition=(Integer) v.getTag();
        if(onItemClickListener != null){
            onItemClickListener.onItemClick(v, (Integer) v.getTag(),mSelectedView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView chooseIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(FilterSortAdapter.this);
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