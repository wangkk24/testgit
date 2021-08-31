package com.pukka.ydepg.moudule.vod.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;

import java.util.List;
/**
 *
 *片花适配器
 * @author: ld
 * @date: 2017-12-19
 */

public class TrailersAdapter  extends RecyclerView.Adapter<TrailersAdapter.ViewHolder>{

    private List<VODMediaFile> mVODMediaFiles;

    private DetailPresenter  mDetailPresenter;

    private String vodId;
    public TrailersAdapter(List<VODMediaFile> mediaFiles,DetailPresenter  mDetailPresenter,String vodId) {
        mVODMediaFiles = mediaFiles;
        this.mDetailPresenter=mDetailPresenter;
        this.vodId=vodId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details_trailers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VODMediaFile vodMediaFile = mVODMediaFiles.get(position);
        holder.itemView.setTag(position);
        Picture picture = vodMediaFile.getPicture();
        if (picture != null) {
            List<String> posters = picture.getPosters();
            if (posters != null && posters.size() != 0) {
                Glide.with(holder.itemView.getContext()).load(posters.get(0)).into(holder.trailersImg);
            }
        }
        String time = vodMediaFile.getElapseTime();
        if (TextUtils.isEmpty(time)) {
            holder.trailersText.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        return mVODMediaFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView trailersImg;
        private TextView trailersText;
        public ViewHolder(View itemView) {
            super(itemView);
            trailersImg = (ImageView) itemView.findViewById(R.id.trailers_img);
            trailersText = (TextView) itemView.findViewById(R.id.video_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position= (int) view.getTag();
                    mDetailPresenter.playClipfile(mVODMediaFiles.get(position),vodId,mVODMediaFiles.get(position).getElapseTime());
                }
            });
        }
    }
}
