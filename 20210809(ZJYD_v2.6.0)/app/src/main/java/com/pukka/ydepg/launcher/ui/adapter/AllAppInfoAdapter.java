package com.pukka.ydepg.launcher.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.bean.node.AppInfo;
import com.pukka.ydepg.launcher.ui.activity.AllAppInfoActivity;
import com.pukka.ydepg.launcher.util.APPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllAppInfoAdapter extends RecyclerView.Adapter<AllAppInfoAdapter.AllAppInfoViewHolder>{

    private static final String TAG = "AllAppInfoAdapter";

    private List<AppInfo> mData;

    private Context mContext;

    private OnFocusChangedCallBack callBack;

    private int currentPosition = 0;

    public AllAppInfoAdapter(Context context, OnFocusChangedCallBack callBack){
        mContext = context;
        this.callBack = callBack;
    }

    @Override
    public AllAppInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AllAppInfoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_all_app, parent, false));
    }

    @Override
    public void onBindViewHolder(AllAppInfoViewHolder holder, int position) {
        holder.imageView.setBackground(mData.get(position).getAppIcon());
        holder.name.setText(mData.get(position).getAppName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    currentPosition = position;
                    Intent intent = APPUtils.startAPP(mContext, mData.get(position).getPackageName(), null, null, null);
                    if (null != intent) {
                        mContext.startActivity(intent);
                    }
                }catch (Exception e){
                    SuperLog.error(TAG, "start activity failed, error is " + e.getMessage());
                }
            }
        });

        holder.layout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    currentPosition = position;
                    if(callBack != null){
                        callBack.onFocusChanged(position);
                    }
                }
            }
        });

        if(position == currentPosition){
            holder.layout.requestFocus();
        }
    }

    @Override
    public int getItemCount() {
        return null == mData ? 0 : mData.size();
    }

    public void setmData(List<AppInfo> data){
        if(null == mData){
            mData = new ArrayList<>();
        }
        mData.clear();
        if(null != data){
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    class AllAppInfoViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.app_image)
        ImageView imageView;
        @BindView(R.id.app_name)
        TextView name;
        @BindView(R.id.app_layout)
        LinearLayout layout;
        public AllAppInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnFocusChangedCallBack{
        void onFocusChanged(int position);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
