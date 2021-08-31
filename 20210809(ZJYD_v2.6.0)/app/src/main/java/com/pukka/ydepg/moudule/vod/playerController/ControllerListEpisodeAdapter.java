package com.pukka.ydepg.moudule.vod.playerController;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.moudule.vod.presenter.VoddetailEpsiodePresenter;
import com.pukka.ydepg.moudule.vod.utils.BrowseEpsiodesUtils;
import com.pukka.ydepg.moudule.vod.utils.VoddetailEpsiodesUtils;

import java.util.ArrayList;
import java.util.List;

public class ControllerListEpisodeAdapter extends RecyclerView.Adapter<ControllerListEpisodeAdapter.ViewHolder> implements ControllerEpisodeAdapter{

    private static final String TAG = "ControllerListEpisodeAd";

    private List<Episode> mEpisodeList = new ArrayList<>();
    private List<String> mEpisodeCountList = new ArrayList<>();
    private String fatherPrice;
    private BrowseEpsiodesUtils utils;

    private Context mContext;

    private Episode mSelectEpisode;

    //用于标记当前请求到的数据，防止数据错位
    private String episodeTag = "";

    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_SMALL);

    public ControllerListEpisodeAdapter(List<Episode> mEpisodeList,List<String> mEpisodeCountList , String fatherPrice, BrowseEpsiodesUtils utils, Context mContext) {
        this.mEpisodeList = mEpisodeList;
        this.mEpisodeCountList = mEpisodeCountList;
        this.fatherPrice = fatherPrice;
        this.utils = utils;
        this.mContext = mContext;
    }

    private View.OnKeyListener onKeyListener;

    private View.OnClickListener onClickListener;

    public void setOnKeyListener(View.OnKeyListener onKeyListener) {
        this.onKeyListener = onKeyListener;
    }

    public void setItemOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_window_controller_item_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        if (null != onClickListener){
//            holder.bg.setOnClickListener(onClickListener);
//        }

        if (null != mEpisodeList && mEpisodeList.size() > position ){

            Episode episode = mEpisodeList.get(position);

            holder.bg.setTag(episode.getSitcomNO());

            holder.bg.setTag(R.id.controller_item_position,position);

            holder.title.setText(episode.getSitcomNO());

            if (Integer.valueOf(episode.getSitcomNO()) == utils.getBookMarkSitNum()){
                holder.title.setTextColor(mContext.getResources().getColor(R.color.controller_item_title_color_selected));
                holder.playingIcon.setVisibility(View.VISIBLE);
                GlideApp.with(mContext).load(R.drawable.controller_playing).into(holder.playingIcon);
            }else{
                holder.title.setTextColor(mContext.getResources().getColor(R.color.white_0));
                holder.playingIcon.setVisibility(View.GONE);
            }

            //角标逻辑

            boolean isShow = SuperScriptUtil.getInstance().getSuperScriptLogic(episode.getVOD());
            if (isShow){
                //走PBS角标优化新逻辑
                String url = SuperScriptUtil.getInstance().getSuperScriptByVod(episode.getVOD(),SuperScriptUtil.SCRIPT_PLAY_SUB_ICON);
                if (!TextUtils.isEmpty(url)){
                    GlideApp.with(mContext).load(url).into(holder.vipImg);
                    holder.vipImg.setVisibility(View.VISIBLE);
                }else{
                    holder.vipImg.setVisibility(View.GONE);
                }
            }else{
                //走兼容逻辑，可能展示默认角标
                boolean needShowVip = SuperScriptUtil.getInstance().isEpisodeShowVip(episode.getVOD(),fatherPrice);
                if (needShowVip){
                    holder.vipImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.controller_item_vip));
                    holder.vipImg.setVisibility(View.VISIBLE);
                }else{
                    holder.vipImg.setVisibility(View.GONE);
                }
            }
        }else if (mEpisodeCountList.size() > position ){

            String mSitcomNO = mEpisodeCountList.get(position);

            holder.title.setText(mSitcomNO);

            if (null != mSelectEpisode && mSitcomNO.equals(mSelectEpisode.getSitcomNO())){
                holder.playingIcon.setVisibility(View.VISIBLE);
                GlideApp.with(mContext).load(R.drawable.controller_playing).into(holder.playingIcon);
            }else{
                holder.playingIcon.setVisibility(View.GONE);
            }

            holder.vipImg.setVisibility(View.GONE);

        }

    }

    @Override
    public void setDataSource(List<Episode> episodes) {
        this.mEpisodeList = episodes;
        notifyDataSetChanged();
    }

    @Override
    public void setDataEpisodesSource(List<String> episodes) {
        episodeTag = episodes.get(0);
        mEpisodeCountList = episodes;
        mEpisodeList = utils.getEpisodes(Integer.valueOf(episodes.get(0)));

        if (null != mEpisodeList && mEpisodeList.size() > 0){
            notifyDataSetChanged();
        }else{
            int offset = utils.getOffset(episodes.get(0));
            Log.i(TAG, "setDataEpisodesSource: "+ JsonParse.object2String(episodes));
            utils.getEpisodeList(episodes.size(), offset, new VoddetailEpsiodePresenter.GetEpisodeListCallback() {
                @Override
                public void getEpisodeListSuccess(int total, List<Episode> episodes) {
                    Log.i(TAG, "setDataEpisodesSource: "+episodes.get(0).getSitcomNO());
                    if (episodes.size() > 0 && episodeTag.equals(episodes.get(0).getSitcomNO())){
                        mEpisodeList = episodes;
                        utils.saveEpisodes(episodes);
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void getEpisodeListFail() {

                }
            });
        }
    }

    @Override
    public void setSelectEpisode(Episode episode) {
        mSelectEpisode = episode;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null != mEpisodeList && mEpisodeList.size() > 0 ){
            return mEpisodeList.size();
        }else if (null != mEpisodeCountList && mEpisodeCountList.size()>0){
            return mEpisodeCountList.size();
        }else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView playingIcon ;
        ImageView bg;
        TextView title;
        ImageView vipImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playingIcon = itemView.findViewById(R.id.controller_playing_icon);
            bg          = itemView.findViewById(R.id.controller_item_bg);
            title       = itemView.findViewById(R.id.controller_item_text);
            vipImg      = itemView.findViewById(R.id.vipimg);

            bg.setOnKeyListener(onKeyListener);

            if (mFocusHighlight != null) {
                mFocusHighlight.onInitializeView(itemView);
            }

            bg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (mFocusHighlight != null) {
                        mFocusHighlight.onItemFocused(itemView, hasFocus);
                    }

                    try {
                        int sitNum = Integer.valueOf((String) bg.getTag());
                        if (sitNum== utils.getBookMarkSitNum()){
                            if (hasFocus){
                                title.setTextColor(mContext.getResources().getColor(R.color.white_0));
                                if (playingIcon.getVisibility() == View.VISIBLE){
                                    GlideApp.with(mContext).load(R.drawable.detail_playing).into(playingIcon);
                                }
                            }else{
                                title.setTextColor(mContext.getResources().getColor(R.color.controller_item_title_color_selected));
                                if (playingIcon.getVisibility() == View.VISIBLE){
                                    GlideApp.with(mContext).load(R.drawable.controller_playing).into(playingIcon);
                                }
                            }
                        }
                    }catch (Exception e){
                        SuperLog.error(TAG, e);
                    }
                }
            });

        }
    }
}
