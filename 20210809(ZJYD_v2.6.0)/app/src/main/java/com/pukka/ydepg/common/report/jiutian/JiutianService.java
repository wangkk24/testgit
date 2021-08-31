package com.pukka.ydepg.common.report.jiutian;

import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class JiutianService {
    //tracker 曝光服务URL
    //itemIds 曝光资源的媒资ID,多个资源用逗号分隔
    public static void reportDisplay(String tracker,String itemIds){
        String getUrl = tracker;
        if(getUrl.contains("&itemid=")){
            getUrl = getUrl.replace("&itemid=","&itemid="+itemIds);
        } else {
            getUrl = getUrl +"&itemid="+itemIds;
        }

        if(getUrl.contains("&operate_time=")){
            getUrl = getUrl.replace("&operate_time=","&operate_time="+System.currentTimeMillis());
        } else {
            getUrl = getUrl +"&operate_time="+System.currentTimeMillis();
        }
        sendGetRequest(getUrl);
    }

    //tracker 曝光服务URL
    //itemIds 曝光资源的媒资ID,多个资源用逗号分隔
    public static void reportDisplayInDetail(String tracker,String itemIds,String record_albumid){
        String getUrl = tracker;
        if(getUrl.contains("&itemid=")){
            getUrl = getUrl.replace("&itemid=","&itemid="+itemIds);
        } else {
            getUrl = getUrl +"&itemid="+itemIds;
        }

        if(getUrl.contains("&operate_time=")){
            getUrl = getUrl.replace("&operate_time=","&operate_time="+System.currentTimeMillis());
        } else {
            getUrl = getUrl +"&operate_time="+System.currentTimeMillis();
        }

        if(getUrl.contains("&record_albumid=")){
            getUrl = getUrl.replace("&record_albumid=","&record_albumid="+record_albumid);
        } else {
            getUrl = getUrl +"&record_albumid="+record_albumid;
        }

        sendGetRequest(getUrl);
    }

    //tracker 曝光点击URL
    public static void reportClick(String tracker){
        String getUrl = tracker;
        if(getUrl.contains("&operate_time=")){
            getUrl = getUrl.replace("&operate_time=","&operate_time="+System.currentTimeMillis());
        } else {
            getUrl = getUrl +"&operate_time="+System.currentTimeMillis();
        }
        sendGetRequest(getUrl);
    }

    //tracker 开始播放URL
    public static void reportPlay(String tracker){
        String getUrl = tracker;
        if(getUrl.contains("&operate_time=")){
            getUrl = getUrl.replace("&operate_time=","&operate_time="+System.currentTimeMillis());
        } else {
            getUrl = getUrl +"&operate_time="+System.currentTimeMillis();
        }
        sendGetRequest(getUrl);
    }

    //tracker 结束播放URL
    //duration 播放时长,单位毫秒
    public static void reportPlayend(String tracker,long duration){
        String getUrl = tracker;
        if(getUrl.contains("&operate_time=")){
            getUrl = getUrl.replace("&operate_time=","&operate_time="+System.currentTimeMillis());
        } else {
            getUrl = getUrl +"&operate_time="+System.currentTimeMillis();
        }

        if(getUrl.contains("&play_duration=")){
            getUrl = getUrl.replace("&play_duration=","&play_duration="+duration);
        } else {
            getUrl = getUrl +"&play_duration="+duration;
        }

        if(!getUrl.contains("&type=playend")){
            if(getUrl.contains("&type=play")){
                getUrl = getUrl.replace("&type=play","&type=playend");
            } else {
                getUrl = getUrl +"&type=playend";
            }
        }
        sendGetRequest(getUrl);
    }

    private static void sendGetRequest(String url){
        HttpApi.getInstance().getService().sendGetRequest(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        response -> {
                            SuperLog.debug(PbsUaConstant.TAG, "Report recommend action to Jiutian successfully.Response message = " + response.string());
                        },
                        throwable -> {
                            SuperLog.error(PbsUaConstant.TAG,"Report recommend action to Jiutian exception.");
                            SuperLog.error(PbsUaConstant.TAG,throwable);
                        }
                );
    }

    public static String getJiutianRecommendContentIDs(List<VOD> listVod){
        String strIDs = "";
        for(VOD vod : listVod){
            strIDs = strIDs.length() == 0? strIDs + vod.getItemid() : strIDs + "," + vod.getItemid();
        }
        return strIDs;
    }
}