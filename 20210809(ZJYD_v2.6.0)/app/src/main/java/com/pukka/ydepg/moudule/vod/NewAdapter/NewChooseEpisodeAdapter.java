package com.pukka.ydepg.moudule.vod.NewAdapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.leanback.widget.HorizontalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.vod.adapter.ChooseEpisodeAdapter;
import com.pukka.ydepg.moudule.vod.adapter.EpisodeAdapter;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.presenter.NewDetailPresenter;
import com.pukka.ydepg.moudule.vod.presenter.VoddetailEpsiodePresenter;
import com.pukka.ydepg.moudule.vod.utils.DetailCommonUtils;
import com.pukka.ydepg.moudule.vod.utils.VoddetailEpsiodesUtils;

import java.util.ArrayList;
import java.util.List;

public class NewChooseEpisodeAdapter extends RecyclerView.Adapter <NewChooseEpisodeAdapter.ViewHolder>implements EpisodeAdapter {

    private List<Episode> mEpisodeList = new ArrayList<>();
    private List<String> mEpisodeCountList = new ArrayList<>();
    private NewDetailPresenter mPresenter;
    private View.OnKeyListener mOnKeyListener;
    private Episode mSelectEpisode;
    private Context context;
    //防止快速切集
    private boolean canclick = true;

    //防止快速点击
    private boolean avoidMutiClick = true;

    private int selectedPosition=-1;
    private int bookmarkPosition=-1;

    private String showEpisodeVIPMark;

    private boolean is4KSource = false;

    private boolean needRequestFocus = false;

    private boolean showPlaying;

    private String lastPlayUrl;
    private String lastPlayID;
    private VoddetailEpsiodesUtils utils;

    private HorizontalGridView recyclerView;

    private ClickListener clickListener;

    //用于标记当前请求到的数据，防止数据错位
    private String episodeTag = "";

    public void setClickListener(NewChooseEpisodeAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    //防止快速移动焦点错乱
    boolean canMove = true;

    public void setNeedRequestFocus(boolean needRequest) {
        this.needRequestFocus = needRequest;
    }

    public int getBookmarkPosition() {
        return bookmarkPosition;
    }

    public NewChooseEpisodeAdapter(List<Episode> episodeList,List<String> episodes, VoddetailEpsiodesUtils utils, NewDetailPresenter presenter, Context context , boolean is4KSource) {
        mEpisodeCountList = episodes;
        this.mEpisodeList = episodeList;

        if (!CollectionUtil.isEmpty(episodes)){
            episodeTag = episodes.get(0);
        }else if (!CollectionUtil.isEmpty(episodeList)){
            episodeTag = episodeList.get(0).getSitcomNO();
        }

        this.utils = utils;
        mPresenter = presenter;
        showEpisodeVIPMark = LauncherService.getInstance().getLauncher().getExtraData().get(Constant.SHOW_EPSIODE_VIP_MARK);
        this.context = context;
        setHasStableIds(true);

        this.is4KSource = is4KSource;
        boolean canPrePlay = DetailCommonUtils.canPlay(is4KSource);
        boolean ismigu = false;
        if (null != episodes && episodes.size()>0 && null != episodes.get(0)){
            ismigu = VodUtil.isMiguVod(utils.getmVoddetail());
        }
        showPlaying = canPrePlay && !ismigu;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_choose_episode_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.selectedIcon.setVisibility(View.GONE);
        if (null != mEpisodeList && mEpisodeList.size()>0){
            Episode episode = mEpisodeList.get(position);
            holder.itemView.setTag(episode);
            if (!TextUtils.isEmpty(episode.getSitcomNO())) {
                holder.nameTv.setText(episode.getSitcomNO());
            }

            boolean isShow = SuperScriptUtil.getInstance().getSuperScriptLogic(episode.getVOD());
            Log.i(TAG, "onBindViewHolder: isShow"+isShow);
            if (isShow){
                //走PBS角标优化新逻辑
                String url = SuperScriptUtil.getInstance().getSuperScriptByVod(episode.getVOD(),SuperScriptUtil.SCRIPT_SUB_ICON);
                if (!TextUtils.isEmpty(url)){
                    GlideApp.with(holder.itemView.getContext()).load(url).into(holder.vipImg);
                    holder.vipImg.setVisibility(View.VISIBLE);
                }else{
                    holder.vipImg.setVisibility(View.GONE);
                }
            }else{
                //走兼容逻辑，可能展示默认角标
                String fatherPrice = "";
                if (null != utils && null != utils.getmVoddetail()){
                    fatherPrice = utils.getmVoddetail().getPrice();
                }
                boolean needShowVip = SuperScriptUtil.getInstance().isEpisodeShowVip(episode.getVOD(),fatherPrice);
                Log.i(TAG, "onBindViewHolder: needShowVip"+needShowVip);
                if (needShowVip){
                    holder.vipImg.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(R.drawable.superscript_vip));
                    holder.vipImg.setVisibility(View.VISIBLE);
                }else{
                    holder.vipImg.setVisibility(View.GONE);
                }
            }
            if(VodUtil.canPlay(is4KSource)) {
                holder.mark.setVisibility(View.GONE);
            }
            else{
                holder.mark.setVisibility(View.VISIBLE);
            }

            holder.nameTv.setCompoundDrawables(null, null, null, null);//还原所有textview的状态
            holder.parent.setSelected(false);
            if (mSelectEpisode != null) {
                if (mSelectEpisode.getSitcomNO().equals(episode.getSitcomNO())) {
                    recyclerView.setSelectedPosition(position);
                    bookmarkPosition=position;
                    if (showPlaying){
                        holder.playImg.setVisibility(View.VISIBLE);
                        holder.nameTv.setVisibility(View.GONE);
                    }else{
                        if (VodUtil.canPlay(is4KSource)){
                            holder.selectedIcon.setVisibility(View.VISIBLE);
                            holder.playImg.setVisibility(View.GONE);
                            holder.nameTv.setVisibility(View.VISIBLE);
                        }else{
                            holder.playImg.setVisibility(View.GONE);
                            holder.nameTv.setVisibility(View.VISIBLE);
                        }
                    }
                    if (VodUtil.canPlay(is4KSource)){
                        holder.parent.setSelected(true);
                    }

                    GlideApp.with(context).load(R.drawable.detail_playing).into(holder.playImg);
                    if(needRequestFocus){
                        holder.itemView.requestFocus();
                        needRequestFocus = false;
                    }
                }else{
                    holder.playImg.setVisibility(View.GONE);
                    holder.nameTv.setVisibility(View.VISIBLE);
                }
            }else{
                holder.playImg.setVisibility(View.GONE);
                holder.nameTv.setVisibility(View.VISIBLE);
            }
        }else{
            String sitNum = mEpisodeCountList.get(position);
            holder.vipImg.setVisibility(View.GONE);
            holder.nameTv.setVisibility(View.VISIBLE);
            holder.nameTv.setText(sitNum);
            holder.parent.setSelected(false);
        }

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

    private static final String TAG = "NewChooseEpisodeAdapter";
    @Override
    public void setDataSource(List<Episode> episodes) {
        if (!CollectionUtil.isEmpty(episodes) && episodeTag.equals(episodes.get(0).getSitcomNO())){
            //防止重复刷新，覆盖记录的焦点
            return;
        }
        Log.i(TAG, "VoddetailEpsiodesUtils: "+episodes.size());
        this.episodeTag = episodes.get(0).getSitcomNO();
        this.mEpisodeList = episodes;
        selectedPosition = -1;
        recyclerView.setSelectedPosition(0);
        notifyDataSetChanged();
    }

    @Override
    public void setDataEpisodesSource(List<String> episodes) {
        if (episodeTag.equals(episodes.get(0))){
            //防止重复刷新，覆盖记录的焦点
            return;
        }
        episodeTag = episodes.get(0);
        mEpisodeCountList = episodes;
        mEpisodeList = utils.getEpisodes(Integer.valueOf(episodes.get(0)));
        selectedPosition = -1;
        recyclerView.setSelectedPosition(0);

        if (null != mEpisodeList && mEpisodeList.size() > 0){
            notifyDataSetChanged();
            refreshHeight(episodes.size());
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
                        refreshHeight(episodes.size());
                    }
                }

                @Override
                public void getEpisodeListFail() {

                }
            });
        }
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

        private ImageView selectedIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv  = (TextView) itemView.findViewById(R.id.episodes_name);
            vipImg  = (ImageView) itemView.findViewById(R.id.vipimg);
            playImg = (ImageView) itemView.findViewById(R.id.playimg);
            parent  = (RelativeLayout) itemView.findViewById(R.id.parent);
            mark    = (ImageView) itemView.findViewById(R.id.episodes_mark);
            selectedIcon = itemView.findViewById(R.id.detail_selected_icon);
            if (mOnKeyListener != null) {
                itemView.setOnKeyListener(mOnKeyListener);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (null == mEpisodeList || mEpisodeList.size() == 0){
                        return;
                    }

                    if (!VodUtil.canPlay(is4KSource)){
                        //不支持播放，不响应
                        return;
                    }

                    if (!canclick){
                        return;
                    }

                    if (!avoidMutiClick){
                        return;
                    }
                    if (avoidMutiClick){
                        avoidMutiClick = false;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                avoidMutiClick = true;
                            }
                        },1500);
                    }

                    if (canclick){
                        canclick = false;
                        //保护措施，10秒后重新置为可点击，防止接口出错卡死不能点击
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "run: 保护措施，20秒后重新置为可点击，防止接口出错卡死不能点击");
                                canclick = true;
                            }
                        },20000);
                    }


                    Episode episode = (Episode) v.getTag();

                    if (null != clickListener){
                        if (playImg.getVisibility() == View.VISIBLE){
                            clickListener.onClick(episode,true,v);
                        }else{
                            clickListener.onClick(episode,false,v);
                        }

                    }
                }
            });
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        int position = recyclerView.getChildPosition(v);
                        recyclerView.setSelectedPosition(position);
                        selectedPosition = position;
                        canMove = false;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                canMove = true;
                            }
                        },350);

                        nameTv.setTextColor(mPresenter.getRxAppCompatActivity().getResources().getColor(R.color.c21_color));
                    }else{
                        nameTv.setTextColor(mPresenter.getRxAppCompatActivity().getResources().getColor(R.color.c24_color));
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

    public List<Episode> getmEpisodeList() {
        return mEpisodeList;
    }

    public List<String> getmEpisodeCountList() {
        return mEpisodeCountList;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public interface ClickListener{
        void onClick(Episode episode,boolean isSelected,View view);
    }

    public void resetState(){
        canclick = true;
    }

    public void setRecyclerView(HorizontalGridView recyclerView) {
        this.recyclerView = recyclerView;
    }

    //根据剧集数目适应高度
    private void refreshHeight(int size){
        if (null == recyclerView){
            return;
        }
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        if (size > 10) {
            layoutParams.height = OTTApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.margin_152);
        } else {
            layoutParams.height = OTTApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.margin_80);
        }
        recyclerView.setLayoutParams(layoutParams);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
