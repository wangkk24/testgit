package com.pukka.ydepg.moudule.vod.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;

import java.util.List;
/**
 *
 *综艺选集适配器
 * @author: ld
 * @date: 2017-12-19
 */
public class ChooseVarietyEpisodeAdapter extends RecyclerView.Adapter<ChooseVarietyEpisodeAdapter.ViewHolder> implements EpisodeAdapter {

    private List<Episode> mEpisodeList;
    private DetailPresenter mPresenter;
    private View.OnKeyListener mOnKeyListener;
    private Episode mSelectEpisode;


    private int bookmarkPosition=-1;

    private String showEpisodeVIPMark;

    private boolean is4KSource = false;

    private boolean needRequestFocus = false;

    private String lastPlayUrl;
    private String lastPlayID;

    public void setNeedRequestFocus(boolean needRequest) {
        this.needRequestFocus = needRequest;
    }

    public int getBookmarkPosition() {
        return bookmarkPosition;
    }

    public ChooseVarietyEpisodeAdapter(List<Episode> episodes, DetailPresenter presenter) {
        mEpisodeList = episodes;
        mPresenter = presenter;
        showEpisodeVIPMark = LauncherService.getInstance().getLauncher().getExtraData().get(Constant.SHOW_EPSIODE_VIP_MARK);
    }

    public void setIs4KSource(boolean is4KSource) {
        this.is4KSource = is4KSource;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_variety_epsiode, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Episode episode = mEpisodeList.get(position);
        holder.itemView.setTag(episode);
        if (!TextUtils.isEmpty(episode.getSitcomNO())&&!TextUtils.isEmpty(episode.getVOD().getName())) {
            holder.nameTv.setText(/**"第"+episode.getSitcomNO()+"期   "+**/episode.getVOD().getName());
        }
        SuperLog.info2SDDebug("EpisodeVIP", "showEpisodeVIPMark is " + showEpisodeVIPMark);
        if(SuperScriptUtil.getInstance().isShowVipMark(episode.getVOD(), mPresenter.getVODDetail()==null?"":mPresenter.getVODDetail().getPrice())&&(TextUtils.isEmpty(showEpisodeVIPMark)||"1".equals(showEpisodeVIPMark))){
            holder.vipImg.setVisibility(View.VISIBLE);
        }else{
            holder.vipImg.setVisibility(View.GONE);
        }
        if(VodUtil.canPlay(is4KSource)) {
            holder.mark.setVisibility(View.GONE);
            holder.parent.setBackgroundResource(R.drawable.detail_episode_normal);
        }
        else{
            holder.mark.setVisibility(View.VISIBLE);
            holder.parent.setBackgroundResource(android.R.color.transparent);
        }
        if (mSelectEpisode != null) {
            if (mSelectEpisode.getSitcomNO().equals(episode.getSitcomNO())) {
                bookmarkPosition=position;
               holder.playImg.setVisibility(View.VISIBLE);
                if(needRequestFocus){
                    holder.itemView.requestFocus();
                    needRequestFocus = false;
                }
            }else{
                holder.playImg.setVisibility(View.GONE);
            }

        }else{
            holder.playImg.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mEpisodeList.size();
    }

    @Override
    public void setDataEpisodesSource(List<String> episodes) {

    }

    @Override
    public void setDataSource(List<Episode> episodes) {
        mEpisodeList.clear();
        mEpisodeList.addAll(episodes);
        notifyDataSetChanged();
    }

    public void setOnKeyListener(View.OnKeyListener listener) {
        mOnKeyListener = listener;
    }

    @Override
    public void setSelectEpisode(Episode episode) {
        mSelectEpisode = episode;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv;

        private ImageView vipImg;

        private ImageView playImg;

        private RelativeLayout parent;

        private ImageView mark;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.episodes_name);
            vipImg= (ImageView) itemView.findViewById(R.id.vipimg);
            playImg =(ImageView) itemView.findViewById(R.id.playimg);
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
                        SuperLog.debug("ChooseVarietyEpisodeAdapter", "isClickEpisode:" + mPresenter.isClickEpisode());
                        Episode episode = (Episode) v.getTag();
                        mPresenter.setLastPlayUrl(lastPlayUrl);
                        mPresenter.setLastPlayID(lastPlayID);
                        mPresenter.playVOD(episode);
                    }
                }
            });
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    TextView tv = (TextView) v.findViewById(R.id.episodes_name);
                    if (hasFocus) {
                        if(VodUtil.canPlay(is4KSource)) {
                            v.setBackground(mPresenter.getRxAppCompatActivity().getResources().getDrawable(R.drawable.detail_choose_episode_focus));
                        }
                        tv.setTextColor(mPresenter.getRxAppCompatActivity().getResources().getColor(R.color.c21_color));
                        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                        tv.setSelected(true);
                    }else{
                        if(VodUtil.canPlay(is4KSource)) {
                            v.setBackground(mPresenter.getRxAppCompatActivity().getResources().getDrawable(R.drawable.detail_episode_normal));
                        }
                        tv.setTextColor(mPresenter.getRxAppCompatActivity().getResources().getColor(R.color.c24_color));
                        tv.setEllipsize(TextUtils.TruncateAt.END);
                        tv.setSelected(false);
                    }
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setLastPlayID(String lastPlayID) {
        this.lastPlayID = lastPlayID;
    }

    public void setLastPlayUrl(String lastPlayUrl) {
        this.lastPlayUrl = lastPlayUrl;
    }
}