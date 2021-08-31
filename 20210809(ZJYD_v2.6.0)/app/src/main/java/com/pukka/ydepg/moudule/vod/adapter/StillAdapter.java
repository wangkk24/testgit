package com.pukka.ydepg.moudule.vod.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pukka.ydepg.R;
import com.pukka.ydepg.moudule.vod.activity.PicBrowserActivity;


import java.util.ArrayList;
/**
 *
 *剧照适配器
 * @author: ld
 * @date: 2017-12-19
 */

public class StillAdapter extends RecyclerView.Adapter<StillAdapter.ViewHolder> {

    private ArrayList<String> mStillList;

    public StillAdapter(ArrayList<String> stills) {
        mStillList = stills;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details_stills, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        String img = mStillList.get(position);
        if (!TextUtils.isEmpty(img)) {
            Glide.with(holder.itemView.getContext()).load(img).into(holder.stillImg);
        }
    }

    @Override
    public int getItemCount() {
        return mStillList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView stillImg;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    Intent intent = new Intent();
                    intent.setClass(v.getContext(), PicBrowserActivity.class);
                    intent.putStringArrayListExtra("pics", mStillList);
                    intent.putExtra("current_item", position);
                    v.getContext().startActivity(intent);
                }
            });
            stillImg = (ImageView) itemView.findViewById(R.id.still_img);
        }
    }
}
