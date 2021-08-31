package com.pukka.ydepg.common.report.ubd.pbs.scene;

import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;

import java.util.HashMap;
import java.util.Map;

public class Detail {

    //从详情页点订购跳转到PBS订购页面
    public static Map<String,String> getPurchaseData(String contentId){
        Map<String,String> data = new HashMap<>();
        data.put("actionChannel", PbsUaConstant.ActionChannel.DETAIL);//00_01
        data.put("actionType", PbsUaConstant.ActionType.CLICK);
        data.put("contentId",contentId);
        return data;
    }

    /*详情页推荐曝光或点击
     * actionType    : [曝光]PbsUaConstant.ActionType.IMPRESSION  [点击]PbsUaConstant.ActionType.CLICK
     * contentId     : [曝光]当前浏览影片ID  [点击]用户点击影片ID
     * appointedId   : [相关推荐]relatedReco25 [猜你爱看]guessReco25
     * recommendType : [智能推荐]0 [人工推荐]1
     * sceneId       : [智能推荐]705猜你爱看 215相关推荐 [人工推荐]推荐栏目的ID
     * contentIds    : [曝光]推荐的影片ID列表，以逗号分隔  [点击]NA
     * identifyType  : [智能推荐]接口返回的identifyType值 [人工推荐]无此字段
     */
    public static Map<String,String> getRecommendData(
            String actionType,
            String contentId,
            String appointedId,
            String recommendType,
            String sceneId,
            String contentIds,
            String identifyType){
        Map<String,String> data = new HashMap<>();
        data.put("actionChannel", PbsUaConstant.ActionChannel.DETAIL);//00_01
        data.put("actionType", actionType);
        data.put("contentId",contentId);
        data.put("appointedId",appointedId);
        data.put("recommendType",recommendType);
        data.put("sceneId",sceneId);
        data.put("identifyType",identifyType);
        if(actionType.equals(PbsUaConstant.ActionType.IMPRESSION)){
            data.put("contentIds",contentIds);
        }
        return data;
    }
}