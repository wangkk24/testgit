package com.pukka.ydepg.moudule.vrplayer.vrplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pukka.ydepg.R;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.module.VideoBean;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.view.MD360PlayerActivity;

import java.util.ArrayList;

/**
 * 功能描述
 *
 * @author l00477311
 * @since 2020-07-28
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyHolder> {
    private final LayoutInflater inflater;
    private Context context;
    private ArrayList<VideoBean> mList;
    private ArrayList<VideoBean> configList;

    public VideoAdapter(Context context, ArrayList<VideoBean> mList) {
        this.context = context;
        this.mList = mList;
        inflater = LayoutInflater.from(context);
    }

    @Override

    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyHolder(inflater.inflate(R.layout.layout_video_player_select_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        //加载图片
        Glide.with(context).load(mList.get(position).getPicture().getPosterUrl()).error(R.drawable.icon_default).placeholder(R.drawable.icon_default).into(holder.ivSelectIcon);

       // GlideUtils.showImageView(context,mList.get(position).getPicture().getPosterUrl(),R.drawable.icon_default,R.drawable.icon_default,holder.ivSelectIcon);
        bindItemMyHolder(holder, position);
    }

    private void bindItemMyHolder(MyHolder holder, final int position) {
        VideoBean bean = mList.get(position);
        setImage(holder.ivSelectIcon, "");
        holder.tvName.setText(bean.getResourceName());
        holder.llContent.setOnClickListener(v -> {
            MD360PlayerActivity.startVideo(context, bean);
        });

        holder.itemView.setFocusable(true);
        holder.itemView.setTag(position);
        holder.itemView.setOnFocusChangeListener((v, hasFocus) -> {
            holder.llContent.setSelected(hasFocus);
        });

    }

    private void setImage(ImageView imageView, String path) {
//        imageView.setImageResource(R.drawable.icon_default);
    }

    public void addItem(VideoBean bean) {
        mList.add(bean);
        notifyDataSetChanged();
    }

    public void addAllItem(ArrayList<VideoBean> mDatas) {
        mList.clear();
        mList.addAll(mDatas);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
      return mList == null ? 0 : mList.size();
    }

    public void setConfigList(ArrayList<VideoBean> configList) {
        this.configList = configList;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private final FrameLayout llContent;
        private final ImageView ivSelectIcon;
        private final TextView tvName;
        private final LinearLayout main;

        public MyHolder(View itemView) {
            super(itemView);
            llContent = itemView.findViewById(R.id.ll_content);
            ivSelectIcon = itemView.findViewById(R.id.iv_select_icon);
            tvName = itemView.findViewById(R.id.tv_select_name);
            main = itemView.findViewById(R.id.main);
        }
    }
}
