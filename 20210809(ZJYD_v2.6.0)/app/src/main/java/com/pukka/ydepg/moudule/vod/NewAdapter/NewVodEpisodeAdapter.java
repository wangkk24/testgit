package com.pukka.ydepg.moudule.vod.NewAdapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.PlayerAttriUtil;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.customui.adpter.CommonRecyclerAdapter;
import com.pukka.ydepg.customui.adpter.RecyclerHolder;
import com.pukka.ydepg.customui.adpter.VodEpisodeAdapter;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.moudule.featured.view.LauncherService;

import java.util.List;

public class NewVodEpisodeAdapter extends CommonRecyclerAdapter<String> {
    private  static final String TAG= VodEpisodeAdapter.class.getName();

    private String fatherPrice = "";

    private String showEpsiodeVIPMark;

    private List<Episode> episodes;

    public NewVodEpisodeAdapter(RecyclerView v, List<String> datas, String fatherPrice) {
        super(v, datas, R.layout.item_vod_episode);
        this.fatherPrice=fatherPrice;
        showEpsiodeVIPMark= LauncherService.getInstance().getLauncher().getExtraData().get(Constant.SHOW_EPSIODE_VIP_MARK);
    }

    @Override
    public void setDatas(List<String> objects) {
        this.episodes = null;
        super.setDatas(objects);
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
        notifyDataSetChanged();
    }

    @Override
    public void convert(RecyclerHolder helper, String item, int position) {
        SuperLog.debug(TAG,"convert:" + item);
        if (!PlayerAttriUtil.isEmpty(item)) {
            helper.setText(R.id.ondemand_episodes_name, item);
            helper.itemView.setTag(item);
        }
        helper.getView(R.id.vipimg).setVisibility(View.GONE);
        SuperLog.info2SDDebug("EpisodeVIP", "showEpsiodeVIPMark is " + showEpsiodeVIPMark);

        if (null != episodes && episodes.size()>position){
            Episode episode = episodes.get(position);
            ImageView imageView = helper.getView(R.id.vipimg);
            if (null != imageView){

                boolean isShow = SuperScriptUtil.getInstance().getSuperScriptLogic(episode.getVOD());
                if (isShow){
                    //走PBS角标优化新逻辑
                    String url = SuperScriptUtil.getInstance().getSuperScriptByVod(episode.getVOD(),SuperScriptUtil.SCRIPT_PLAY_SUB_ICON);
                    if (!TextUtils.isEmpty(url)){
                        GlideApp.with(mContext).load(url).into(imageView);
                        imageView.setVisibility(View.VISIBLE);
                    }else{
                        imageView.setVisibility(View.GONE);
                    }
                }else{
                    //走兼容逻辑，可能展示默认角标
                    boolean needShowVip = SuperScriptUtil.getInstance().isEpisodeShowVip(episode.getVOD(),fatherPrice);
                    if (needShowVip){
                        imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.details_episode_vip));
                        imageView.setVisibility(View.VISIBLE);
                    }else{
                        imageView.setVisibility(View.GONE);
                    }
                }
            }else{
                Log.i(TAG, "convert: imageView为空");
                helper.getView(R.id.vipimg).setVisibility(View.GONE);
            }
        }else{
            Log.i(TAG, "convert: episodes为空");
            helper.getView(R.id.vipimg).setVisibility(View.GONE);
        }

        helper.itemView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (onKeyDownListener != null) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        return onKeyDownListener.onKeyDown(v, keyCode, event, position);
                    }
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        return onKeyDownListener.onKeyUp(v, keyCode, event, position);
                    }
                    return false;
                } else {
                    return false;
                }
            }
        });
        if(TextUtils.equals(item, currentSitNum)){
            helper.itemView.requestFocus();
        }

    }
}
