package com.pukka.ydepg.moudule.mytv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.moudule.mytv.bean.OrderFilterDataBean;

import java.util.List;

/**
 * Created by hasee on 2017/8/28.
 */

public class FilterTimeAdapter extends RecyclerView.Adapter<FilterTimeAdapter.ViewHolder> {
    private Context mContext;
    /**
     * 选中position
     */
    private int mPosition;
    RecyclerView mRecyclerView;
    List<OrderFilterDataBean> mDataList;
    private OnItemSelectListener mListener;
    /**
     * 之前position --因为在两个列表切换焦点
     */
    private int oldPosition;

    public FilterTimeAdapter(List<OrderFilterDataBean> dataList, RecyclerView recyclerView, Context context){
        mContext = context;
        mRecyclerView =  recyclerView;
        mDataList = dataList;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_filter_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OrderFilterDataBean orderFilterDataBean = mDataList.get(position);
        holder.filterView.setText(orderFilterDataBean.getName());
        if (mPosition == position){
            holder.selectImg.setVisibility(View.VISIBLE);
        }else {
            holder.selectImg.setVisibility(View.GONE);
        }
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.white_transparent_30));
                    if (null != mListener){
                        mListener.onItemSelected(orderFilterDataBean);
                    }
                    mPosition = position;
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mPosition != oldPosition) {
                                oldPosition = mPosition;
                                notifyDataSetChanged();
                            }
                        }
                    });
                }else {
                    holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == mDataList ? 0 : mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView filterView;
        ImageView selectImg;
        public ViewHolder(View itemView) {
            super(itemView);
            filterView = (TextView) itemView.findViewById(R.id.mytv_filter_view);
            selectImg = (ImageView) itemView.findViewById(R.id.mvtv_filter_select);
        }
    }
    public interface OnItemSelectListener{
       void onItemSelected(OrderFilterDataBean bean);
    }

    public void setOnItemSelectListener(OnItemSelectListener listener){
        mListener = listener;
    }
}
