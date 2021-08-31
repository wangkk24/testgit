package com.pukka.ydepg.customui.adpter;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.utils.HeartBeatUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.PlayerAttriUtil;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.moudule.featured.view.LauncherService;

import java.util.List;

/**
 * Created by ld on 2017/8/30.
 */

public class VodEpisodeAdapter extends CommonRecyclerAdapter<Episode> {

    private  static final String TAG=VodEpisodeAdapter.class.getName();

    private String fatherPrice = "";

    private String showEpsiodeVIPMark;

    public VodEpisodeAdapter(RecyclerView v, List<Episode> datas, String fatherPrice) {
        super(v, datas, R.layout.item_vod_episode);
        this.fatherPrice=fatherPrice;
        showEpsiodeVIPMark= LauncherService.getInstance().getLauncher().getExtraData().get(Constant.SHOW_EPSIODE_VIP_MARK);
    }

    @Override
    public void convert(RecyclerHolder helper, Episode item, int position) {
        SuperLog.debug(TAG,"convert:" + item.getSitcomNO());
        if (!PlayerAttriUtil.isEmpty(item.getSitcomNO())) {
            helper.setText(R.id.ondemand_episodes_name, item.getSitcomNO());
            helper.itemView.setTag(item);
        }
        // HeartBeatUtil.getInstance().isEpisodeShowVip(item.getVOD(),fatherPrice)&&(TextUtils.isEmpty(showEpsiodeVIPMark)||"1".equals(showEpsiodeVIPMark))
        SuperLog.info2SDDebug("EpisodeVIP", "showEpsiodeVIPMark is " + showEpsiodeVIPMark);
//        if(null!=item.getVOD()&& SuperScriptUtil.getInstance().isShowVipMark(item.getVOD(), fatherPrice)&&(TextUtils.isEmpty(showEpsiodeVIPMark)||"1".equals(showEpsiodeVIPMark))){
//            helper.getView(R.id.vipimg).setVisibility(View.VISIBLE);
//        }else{
//            helper.getView(R.id.vipimg).setVisibility(View.GONE);
//        }

        ImageView imageView = helper.getView(R.id.vipimg);
        if (null != imageView){
            boolean isShow = SuperScriptUtil.getInstance().getSuperScriptLogic(item.getVOD());
            if (isShow){
                //走PBS角标优化新逻辑
                String url = SuperScriptUtil.getInstance().getSuperScriptByVod(item.getVOD(),SuperScriptUtil.SCRIPT_PLAY_SUB_ICON);
                if (!TextUtils.isEmpty(url)){
                    GlideApp.with(mContext).load(url).into(imageView);
                    imageView.setVisibility(View.VISIBLE);
                }else{
                    imageView.setVisibility(View.GONE);
                }
            }else{
                //走兼容逻辑，可能展示默认角标
                boolean needShowVip = SuperScriptUtil.getInstance().isEpisodeShowVip(item.getVOD(),fatherPrice);
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
        if(TextUtils.equals(item.getSitcomNO(), currentSitNum)){
            helper.itemView.requestFocus();
        }

    }
}
