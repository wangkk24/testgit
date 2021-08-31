package com.pukka.ydepg.moudule.vod.cache;

import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6node.VodTimeLadder;
import com.pukka.ydepg.common.http.v6bean.v6response.GetContentConfigResponse;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.session.SessionService;
import java.util.List;

/**
 * Created by liudo on 2017/12/22.
 */

public class VodDetailCacheService {

    private static final String TAG = VodDetailCacheService.class.getSimpleName();

    private static VodDetailCacheService channelCacheService = new VodDetailCacheService();

    private GetContentConfigResponse getContentConfigResponse;

    public static VodDetailCacheService getInstance()
    {
        if (null == channelCacheService)
        {
            channelCacheService = new VodDetailCacheService();
        }
        return channelCacheService;
    }


    public GetContentConfigResponse getGetContentConfigResponse() {
        return this.getContentConfigResponse;
    }

    public void setGetContentConfigResponse(GetContentConfigResponse getContentConfigResponse) {
        this.getContentConfigResponse = getContentConfigResponse;
    }



    public static  int getPreviewDuration(List<NamedParameter> parentParamters,List<NamedParameter> epsiodeParamters, int vodTimeType){
        int  tryToSeeTime = 5 * 60;
        String parentDuration="";
        String epsiodeDuration="";
        String trySee = SessionService.getInstance().getSession().getTerminalConfigurationValue("vod_watch_validTime");
        String isLadderUsable = SessionService.getInstance().getSession().getTerminalConfigurationValue("vod_watch_validTime_ladder_usable");
        if (null != parentParamters && parentParamters.size() != 0) {
            for (int i = 0; i < parentParamters.size(); i++) {
                if ("ZJYDPreviewDuration".equals(parentParamters.get(i).getKey())) {
                    parentDuration= parentParamters.get(i).getFistItemFromValue();
                }
            }
        }
        if (null != epsiodeParamters && epsiodeParamters.size() != 0) {
            for (int i = 0; i < epsiodeParamters.size(); i++) {
                if ("ZJYDPreviewDuration".equals(epsiodeParamters.get(i).getKey())) {
                    epsiodeDuration = epsiodeParamters.get(i).getFistItemFromValue();
                }
            }
        }
        SuperLog.debug(TAG,"getPreviewDuration->"+"tryToSeeTime:"+tryToSeeTime+"|parentDuration:"+parentDuration+"epsiodeDuration:"+epsiodeDuration);
        if (!TextUtils.isEmpty(trySee)) {
            tryToSeeTime = Integer.parseInt(trySee);
        }

        if(!TextUtils.isEmpty(isLadderUsable) && TextUtils.equals(isLadderUsable, "1")){
            tryToSeeTime = getCusPreviewDuration(vodTimeType);
        }
        if(TextUtils.isEmpty(parentDuration)){
            return tryToSeeTime;
        }
        if(TextUtils.isEmpty(epsiodeDuration)){
            return  Integer.parseInt(parentDuration);
        }
        return  Integer.parseInt(epsiodeDuration);

    }

    public static int getPreviewDuration(int vodTimeType){
        int  tryToSeeTime = 5 * 60;
        String trySee = SessionService.getInstance().getSession().getTerminalConfigurationValue("vod_watch_validTime");
        String isLadderUsable = SessionService.getInstance().getSession().getTerminalConfigurationValue("vod_watch_validTime_ladder_usable");
        if (!TextUtils.isEmpty(trySee)) {
            tryToSeeTime = Integer.parseInt(trySee);
        }
        else if(!TextUtils.isEmpty(isLadderUsable) && TextUtils.equals(isLadderUsable, "1")){
            tryToSeeTime = getCusPreviewDuration(vodTimeType);
        }
        return  tryToSeeTime;
    }

    public static int getVodTime(VODMediaFile mediaFile){
        if(mediaFile != null && !TextUtils.isEmpty(mediaFile.getElapseTime())){
            return Integer.parseInt(mediaFile.getElapseTime());
        }
        return -1;
    }

    public static int getVodTimeByMediaFiles(List<VODMediaFile> mediaFiles){
        if(mediaFiles != null && mediaFiles.size() > 0){
            return getVodTime(mediaFiles.get(0));
        }
        else{
            return getVodTime(null);
        }
    }

    public static int getCusPreviewDuration(int elapseTime){
        int previewDuration = 5 * 60;
        if(elapseTime != -1) {
            String ladder = SessionService.getInstance().getSession().getTerminalConfigurationValue("vod_watch_validTime_ladder");
            List<VodTimeLadder> vodTimeLadders = JsonParse.jsonToClassList(ladder, VodTimeLadder.class);
            int cacheVodTime = 0;
            if (vodTimeLadders != null && vodTimeLadders.size() > 0) {
                for (VodTimeLadder vodTimeLadder : vodTimeLadders) {
                    if (vodTimeLadder.getElapseTime() <= elapseTime && vodTimeLadder.getElapseTime() >= cacheVodTime) {
                        cacheVodTime = vodTimeLadder.getElapseTime();
                        previewDuration = vodTimeLadder.getPreviewduration();
                    }
                }
            }
        }
        return previewDuration;
    }
}

