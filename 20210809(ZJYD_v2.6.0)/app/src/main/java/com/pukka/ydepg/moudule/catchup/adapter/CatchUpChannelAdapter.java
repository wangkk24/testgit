package com.pukka.ydepg.moudule.catchup.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import java.util.List;

/**
 *
 *点播栏目列表适配器
 * @author: ld
 * @date: 2017-12-19
 */
public class CatchUpChannelAdapter extends RecyclerView.Adapter<CatchUpChannelAdapter.ViewHolder> implements View.OnClickListener {

    private static final String TAG=CatchUpChannelAdapter.class.getName();
    private List<String> mSubjects;

    //记录每次刷洗数据的position
    private int mPosition = -1;
    private OnFocusChangeListener mOnFocusChangeListen;
    private String mFocusID;//驻留栏目焦点
    private String currentFocusID;
    private VerticalGridView mRecyclerView;

    public String getCurrentContentId() {
        return currentFocusID;
    }
    public CatchUpChannelAdapter() {
    }

    public void setData(List<String> subjects){
        this.mSubjects = subjects;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catchup_name, parent, false);
        return new ViewHolder(view);
    }

    public void setOnFocusChangeListen(OnFocusChangeListener onFocusChangeListen) {
        this.mOnFocusChangeListen = onFocusChangeListen;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.itemView.setTag(subjects.get(position));
        holder.itemView.setTag(position);
        holder.name.setText(mSubjects.get(position));

        SuperLog.debug(TAG, "onBindViewHolder mFocusID:" + mFocusID + ",item Id:" + mSubjects.get(position));

    }

    @Override
    public int getItemCount() {
        if (null == mSubjects){
            return 0;
        }
        return mSubjects.size();
    }

    @Override
    public void onClick(View v) {
        /*if (mMoviewListOnItemClickListener != null) {
            mMoviewListOnItemClickListener.onItemClick(v, (Subject) v.getTag());
        }*/
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public ViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.catagory_name);
            itemView.setOnClickListener(CatchUpChannelAdapter.this);
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        v.findViewById(R.id.rela_channel_bg).setSelected(true);
                        if (mPosition != (int)v.getTag()){
                            mPosition = (int)v.getTag();
                            mOnFocusChangeListen.OnFocusChange(v,mSubjects.get(mPosition));
                        }
                        //Subject subject = (Subject) v.getTag();
                        /*Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        msg.obj = subject;
                        if (mHandler.hasMessages(1)) {
                            mHandler.removeMessages(1);
                        }
                        mHandler.sendMessage(msg);*/
                    }else{
                        v.findViewById(R.id.rela_channel_bg).setSelected(false);
                    }
                }
            });
        }
    }


    public  interface OnFocusChangeListener {
        void OnFocusChange(View view, String subject);
    }

}
