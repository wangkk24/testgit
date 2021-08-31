package com.pukka.ydepg.common.report.ubd.scene;

import android.text.TextUtils;

import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.UBDService;
import com.pukka.ydepg.common.report.ubd.UBDTool;
import com.pukka.ydepg.common.report.ubd.extension.SwitchData;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.activity.TopicActivity;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.VodDetailActivity;

//播放功能相关上报
public class UBDPlay {

    //播放引流页面
    private static String playSourceActivity;

    //记录播放引流页面
    public static void cachePlaySource(String fromActivity){
        playSourceActivity = fromActivity;
    }

    //播放引流上报
    private static void recordSource(VOD vod, String episodeId, boolean isPreview, String recommendType,String toActivity){
        SwitchData data = new SwitchData();
        if(null==vod){
            return;
        }

        data.setContentID(vod.getID());
        data.setContentName(vod.getName());
        data.setContentType(vod.getContentType());
        data.setActionID( isPreview ? UBDConstant.ACTION.PLAY_PREVIEW:UBDConstant.ACTION.PLAY_NORMAL);
        data.setRecommendType(recommendType);
        data.setExtraOne(UBDConstant.BillType.SOURCE);//代表引流话单
        data.setSystemTime(UBDTool.getInstance().getUBDFormatTime(DateCalendarUtils.PATTERN_A));//解决上报时间相同问题
        data.setEpisodeID(episodeId);

        UBDService.recordSwitchPage(
                playSourceActivity,
                toActivity,
                JsonParse.object2String(data));

    }

    //跳转到播放界面上报
    @Deprecated
    public static void record(VOD vod,String episodeId,boolean isPreview, String recommendType,String toActivity){
        if(null==vod){
            return;
        }

        String fromActivity = getFromActivity();

        SwitchData data = new SwitchData();
        data.setContentID(vod.getID());
        data.setContentName(vod.getName());
        data.setContentType(vod.getContentType());
        data.setEpisodeID(episodeId);
        data.setActionID( isPreview ? UBDConstant.ACTION.PLAY_PREVIEW:UBDConstant.ACTION.PLAY_NORMAL);

        data.setVersion(LauncherService.getInstance().getDesktopVersion());
        data.setUserID(SessionService.getInstance().getSession().getUserId());
        data.setSystemTime(UBDTool.getInstance().getUBDFormatTime(DateCalendarUtils.PATTERN_A));
        UBDService.recordSwitchPage(
                fromActivity,
                toActivity,
                JsonParse.object2String(data));

        //感觉引流上报没有意义 暂时注释
//        //儿童版详情页面直接播放,不需要引流信息
//        if(!toActivity.equals(ChildModeVodDetailActivity.class.getSimpleName())){
//            recordSource(vod,episodeId,isPreview,recommendType,toActivity);
//        }
    }

    //跳转到播放界面上报
    public static void record(String vodId,String vodName,String vodType,String episodeId,boolean isPreview,String recommendType,String sceneId,String appPointedId){

        String fromActivity = getFromActivity();

        SwitchData data = new SwitchData();
        data.setContentID(vodId);
        data.setContentName(vodName);
        data.setContentType(vodType);
        data.setEpisodeID(episodeId);
        data.setActionID( isPreview ? UBDConstant.ACTION.PLAY_PREVIEW:UBDConstant.ACTION.PLAY_NORMAL);

        data.setAppointedId(appPointedId);
        data.setRecommendType(recommendType);
        data.setSceneId(sceneId);

        data.setVersion(LauncherService.getInstance().getDesktopVersion());
        data.setUserID(SessionService.getInstance().getSession().getUserId());
        data.setSystemTime(UBDTool.getInstance().getUBDFormatTime(DateCalendarUtils.PATTERN_A));
        UBDService.recordSwitchPage(
                fromActivity,
                NewVodDetailActivity.class.getSimpleName(),//播放页面的Activity必定为详情页面
                JsonParse.object2String(data));

        //感觉引流上报没有意义 暂时注释
//        //儿童版详情页面直接播放,不需要引流信息
//        if(!toActivity.equals(ChildModeVodDetailActivity.class.getSimpleName())){
//            recordSource(vod,episodeId,isPreview,recommendType,toActivity);
//        }
    }

    private static String getFromActivity(){
        if (TopicActivity.class.getSimpleName().equalsIgnoreCase(UBDSwitch.getInstance().getFromActivity()) && !TextUtils.isEmpty(TopicActivity.getTopicId())){
            return UBDSwitch.getInstance().getFromActivity() + "_" + TopicActivity.getTopicId();
        }else if (NewVodDetailActivity.class.getSimpleName().equalsIgnoreCase(UBDSwitch.getInstance().getFromActivity()) && !TextUtils.isEmpty(NewVodDetailActivity.getTopicId())){
            return UBDSwitch.getInstance().getFromActivity() + "_" + NewVodDetailActivity.getTopicId();
        }else {
            return UBDSwitch.getInstance().getFromActivity();
        }
    }

    //子集切换上报
    public static void recordSwitchEpisode(VOD vod, String episodeId, String action){
        if(null == vod){
            return;
        }
        SwitchData data = new SwitchData();
        data.setContentID(vod.getID());
        data.setContentName(vod.getName());
        data.setContentType(vod.getContentType());
        data.setEpisodeID(episodeId);
        data.setActionID( action );

        data.setVersion(LauncherService.getInstance().getDesktopVersion());
        data.setUserID(SessionService.getInstance().getSession().getUserId());
        data.setSystemTime(UBDTool.getInstance().getUBDFormatTime(DateCalendarUtils.PATTERN_A));

        UBDService.recordSwitchPage(
                UBDConstant.Function.SWITCH_SOURCE,
                UBDConstant.Function.SWITCH_SOURCE,
                JsonParse.object2String(data));
    }
}