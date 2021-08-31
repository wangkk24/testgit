package com.pukka.ydepg.common.report.ubd.scene;

import com.huawei.ott.sdk.ubd.Common;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.UBDService;
import com.pukka.ydepg.common.report.ubd.extension.RecommendImpressionData;
import com.pukka.ydepg.common.utils.JsonParse;

import java.util.List;

public class UBDRecommendImpression {

    //仅供新播放页面使用的推荐数据的UBD上报信息
    public static String sceneId_play;
    public static String recommendType_play;

    //仅供新播放页面使用的推荐数据的UBD上报信息
    public static String sceneId_vod;
    public static String recommendType_vod;

    //仅供儿童版播放页面使用的推荐数据的UBD上报信息
    public static String sceneId_child;
    public static String recommendType_child;

    public static void record(
            String appPointedId,
            String sceneId,
            String contentIds,
            String recommendType,
            String packageId,
            String contentId)
    {
        Common common = new Common();
        common.setActionType(UBDConstant.ActionType.RECOMMEND_IMPRESSION);
        RecommendImpressionData data = new RecommendImpressionData();
        data.setAppPointedId(appPointedId);
        data.setSceneId(sceneId);
        data.setContentIds(contentIds);
        data.setRecommendType(recommendType);
        data.setPackageId(packageId);
        data.setContentId(contentId);
        common.setlabel(JsonParse.object2String(data, RecommendImpressionData.class));
        UBDService.recordCommon(common);
    }

    public static String getRecommendContentIDs(List<VOD> listVod){
        String strIDs = "";
        for(VOD vod : listVod){
            strIDs = strIDs.length() == 0? strIDs + vod.getID() : strIDs + "," + vod.getID();
        }
        return strIDs;
    }
}
