package com.pukka.ydepg.moudule.vod.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.UBDTool;
import com.pukka.ydepg.common.report.ubd.scene.UBDPlay;
import com.pukka.ydepg.common.utils.HeartBeatUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.common.utils.TextUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.vod.activity.BrowseTVPlayFragment;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.cache.VodDetailCacheService;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.presenter.VoddetailEpsiodePresenter;
import com.pukka.ydepg.moudule.vod.utils.VoddetailEpsiodesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liudo on 2019/3/26.
 */

public class ChooseChildEpisodeAdapter extends RecyclerView.Adapter<ChooseChildEpisodeAdapter.ViewHolder> implements EpisodeAdapter
{
    private static final String TAG = "ChooseChildEpisodeAdapt";

    private List<Episode> mEpisodeList;

    private DetailPresenter mPresenter;
    private View.OnKeyListener mOnKeyListener;
    private Episode mSelectEpisode;
    private ChildModeVodDetailActivity.VodDetailHandler mHandler;

    private int bookmarkPosition=-1;

    private String showEpsiodeVIPMark;

    private boolean is4KSource = false;

    private Activity  activity;

    //用于标记当前请求到的数据，防止数据错位
    private String episodeTag = "";
    private List<String> mEpisodeCountList = new ArrayList<>();
    private VoddetailEpsiodesUtils utils;

    public int getBookmarkPosition() {
        return bookmarkPosition;
    }

    public List<Episode> getEpisodeList()
    {
        return mEpisodeList;
    }


    public ChooseChildEpisodeAdapter(ChildModeVodDetailActivity.VodDetailHandler handler,List<Episode> episodes, DetailPresenter presenter,Activity mActivity,VoddetailEpsiodesUtils utils,List<String> mEpisodeCountList) {
        setHasStableIds(true);
        this.mHandler = handler;
        mEpisodeList = episodes;
        mPresenter = presenter;
        showEpsiodeVIPMark= LauncherService.getInstance().getLauncher().getExtraData().get(Constant.SHOW_EPSIODE_VIP_MARK);
        this.utils = utils;
        this.mEpisodeCountList = mEpisodeCountList;
        this.activity=mActivity;
    }

    public void setIs4KSource(boolean is4KSource) {
        this.is4KSource = is4KSource;
    }

    @Override
    public ChooseChildEpisodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_detail_choose_epsiode, parent, false);
        return new ChooseChildEpisodeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChooseChildEpisodeAdapter.ViewHolder holder, int position) {

        if (null != mEpisodeList && mEpisodeList.size()>0){
            Episode episode = mEpisodeList.get(position);
            holder.itemView.setTag(episode);
            if (!TextUtils.isEmpty(episode.getSitcomNO())) {
                holder.nametv.setText(episode.getSitcomNO());
            }

            boolean isShow = SuperScriptUtil.getInstance().getSuperScriptLogic(episode.getVOD());
            if (isShow){
                //走PBS角标优化新逻辑
                String url = SuperScriptUtil.getInstance().getSuperScriptByVod(episode.getVOD(),SuperScriptUtil.SCRIPT_CHILD_SUB_ICON);
                if (!TextUtils.isEmpty(url)){
                    GlideApp.with(holder.itemView.getContext()).load(url).into(holder.vipImg);
                    holder.vipImg.setVisibility(View.VISIBLE);
                }else{
                    holder.vipImg.setVisibility(View.GONE);
                }
            }else{
                //走兼容逻辑，可能展示默认角标
                String fatherPrice = "";
                if (null != mPresenter && null != mPresenter.getVODDetail()){
                    fatherPrice = mPresenter.getVODDetail().getPrice();
                }
                boolean needShowVip = SuperScriptUtil.getInstance().isEpisodeShowVip(episode.getVOD(),fatherPrice);
                if (needShowVip){
                    holder.vipImg.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(R.drawable.details_episode_vip));
                    holder.vipImg.setVisibility(View.VISIBLE);
                }else{
                    holder.vipImg.setVisibility(View.GONE);
                }
            }
            if(VodUtil.canPlay(is4KSource)) {
                holder.mark.setVisibility(View.GONE);
                holder.parent.setBackgroundResource(R.drawable.episodes_shape_normal);
            }
            else{
                holder.mark.setVisibility(View.VISIBLE);
                holder.parent.setBackgroundResource(android.R.color.transparent);
            }

            holder.nametv.setCompoundDrawables(null, null, null, null);//还原所有textview的状态
            if (mSelectEpisode != null) {
                if (mSelectEpisode.getSitcomNO().equals(episode.getSitcomNO())) {
                    bookmarkPosition=position;
                    holder.playimg.setVisibility(View.VISIBLE);
                }else{
                    holder.playimg.setVisibility(View.GONE);
                }
            }else{
                holder.playimg.setVisibility(View.GONE);
            }
        }else{
            String sitNum = mEpisodeCountList.get(position);
            holder.nametv.setText(sitNum);
            holder.vipImg.setVisibility(View.GONE);
            holder.playimg.setVisibility(View.GONE);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
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

    @Override
    public void setDataEpisodesSource(List<String> episodes) {
        episodeTag = episodes.get(0);
        mEpisodeCountList = episodes;
        mEpisodeList = utils.getEpisodes(Integer.valueOf(episodes.get(0)));

        if (null != mEpisodeList && mEpisodeList.size() > 0){
            notifyDataSetChanged();
        }else{
            mEpisodeList = new ArrayList<>();
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
    public void setDataSource(List<Episode> episodes) {
        Log.i(TAG, "VoddetailEpsiodesUtils: "+episodes.size());
        this.mEpisodeList = episodes;
        notifyDataSetChanged();
    }

    public void setOnkeyListener(View.OnKeyListener listener) {
        mOnKeyListener = listener;
    }

    @Override
    public void setSelectEpisode(Episode episode) {
        mSelectEpisode = episode;
        notifyDataSetChanged();
    }

    private void episodePlay(Episode episode){
        Message message = Message.obtain();
        message.what = BrowseTVPlayFragment.EPISODE_PLAY;
        Bundle bundle = new Bundle();
        bundle.putString("SitcomNO", episode.getSitcomNO());
        bundle.putString("EpisodesId", episode.getVOD().getID());
        bundle.putString("MediaId", episode.getVOD().getMediaFiles().get(0).getID());
        bundle.putSerializable("episodeVod", episode.getVOD());
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nametv;

        private ImageView vipImg;

        private ImageView playimg;

        private RelativeLayout parent;

        private ImageView mark;

        public ViewHolder(View itemView) {
            super(itemView);
            nametv= (TextView) itemView.findViewById(R.id.episodes_name);
            vipImg= (ImageView) itemView.findViewById(R.id.vipimg);
            playimg=(ImageView) itemView.findViewById(R.id.playimg);
            parent =(RelativeLayout) itemView.findViewById(R.id.parent);
            mark = (ImageView) itemView.findViewById(R.id.episodes_mark);
            if (mOnKeyListener != null) {
                itemView.setOnKeyListener(mOnKeyListener);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(VodUtil.canPlay(is4KSource)) {
                        mPresenter.setClickEpisode(true);
                        mPresenter.setButtonOrderOrSee(true);
                        SuperLog.debug("ChooseEpisodeAdapter", "isClickEpisode:" + mPresenter.isClickEpisode());
                        Episode episode = (Episode) v.getTag();
//                        mPresenter.playVOD(episode);
                        if (null != episode){
                            episodePlay(episode);
                            if(null!=activity&&activity instanceof ChildModeVodDetailActivity){
                                if(episode!=mSelectEpisode){
                                    mSelectEpisode=episode;
                                    notifyDataSetChanged();
                                }
                                ((ChildModeVodDetailActivity)activity).hideDetailDesc();
                                UBDPlay.recordSwitchEpisode(mPresenter.getVODDetail(),episode.getVOD().getID(), UBDConstant.ACTION.PLAY_NORMAL);
                            }
                        }
                    }
                }
            });
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        if(VodUtil.canPlay(is4KSource)) {
                            v.setBackground(mPresenter.getRxAppCompatActivity().getResources().getDrawable(R.drawable.episodes_shape_selected));
                        }
                        nametv.setTextColor(mPresenter.getRxAppCompatActivity().getResources().getColor(R.color.c21_color));
                        TextUtil.setMarqueen(true,nametv);
                    }else{
                        if(VodUtil.canPlay(is4KSource)) {
                            v.setBackground(mPresenter.getRxAppCompatActivity().getResources().getDrawable(R.drawable.episodes_shape_normal));
                        }
                        nametv.setTextColor(mPresenter.getRxAppCompatActivity().getResources().getColor(R.color.c24_color));
                        TextUtil.setMarqueen(false,nametv);
                    }
                }
            });
        }
    }

}
