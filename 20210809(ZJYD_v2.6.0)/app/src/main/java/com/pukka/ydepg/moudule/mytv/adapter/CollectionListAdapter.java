package com.pukka.ydepg.moudule.mytv.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.IsTV;

import java.util.List;

import static com.pukka.ydepg.common.utils.UtilBase.getApplicationContext;

/**
 * Created by hasee on 2017/8/27.
 */

public class CollectionListAdapter extends RecyclerView.Adapter<CollectionListAdapter.ViewHolder> {

    private final Context mContext;
    private  List<Content> mContentList;

    public CollectionListAdapter(List<Content> contentList, Context context){
        mContentList = contentList;
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mytv_list_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Content content = mContentList.get(position);
        VOD vod = content.getVOD();
        if (null != vod ){
            if (!TextUtils.isEmpty(vod.getName())) {
                holder.name.setText(vod.getName().trim());
            }
            if (IsTV.isTVFar(vod)){
                holder.image.setVisibility(View.VISIBLE);
                holder.image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.icon_tv));
            } else {
                holder.image.setVisibility(View.VISIBLE);
                holder.image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.icon_phone));
            }
        }
    }
    public void addData(List<Content> contentList) {
        mContentList.addAll(contentList);
        notifyItemRangeInserted(mContentList.size() - contentList.size(), contentList.size());
    }
    @Override
    public int getItemCount() {
        return null == mContentList ? 0 : mContentList.size() > 10 ? 10 :mContentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView time;
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.mytv_list_name);
            time = (TextView) itemView.findViewById(R.id.mytv_list_time);
            image= (ImageView)itemView.findViewById(R.id.mytv_list_image);
        }
    }
}
