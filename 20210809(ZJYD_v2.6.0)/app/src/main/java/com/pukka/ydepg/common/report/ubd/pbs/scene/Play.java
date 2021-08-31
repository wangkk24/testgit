package com.pukka.ydepg.common.report.ubd.pbs.scene;

import android.text.TextUtils;

import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;

import java.util.HashMap;
import java.util.Map;

public class Play {
    //从播放页进PBS订购页面
    public static Map<String,String> getPurchaseData(String contentId){
        Map<String,String> data = new HashMap<>();
        data.put("actionChannel",PbsUaConstant.ActionChannel.PLAY);//"00_02"
        data.put("actionType", PbsUaConstant.ActionType.CLICK);
        data.put("contentId",contentId);
        return data;
    }

    /*详情页推荐曝光或点击
     * actionType              : [普通影片播放]PbsUaConstant.ActionType.NORMAL_PLAY  [推荐影片播放]PbsUaConstant.ActionType.RECOMMEND_PLAY
     * contentId               : [曝光]当前浏览影片ID  [点击]用户点击影片ID
     * appointedId  (仅推荐播放) : [相关推荐]relatedReco25 [猜你爱看]guessReco25
     * recommendType(仅推荐播放) : [智能推荐]0 [人工推荐]1
     * sceneId      (仅推荐播放) : [智能推荐]705猜你爱看 215相关推荐 [人工推荐]推荐栏目的ID
     * subContentId (仅连续剧)   : 连续剧子集ID
     */
    public static Map<String,String> getPlayData(
            String actionType,
            String contentId,
            String appointedId,
            String recommendType,
            String sceneId,
            String subContentId){
        Map<String,String> data = new HashMap<>();
        data.put("actionChannel", PbsUaConstant.ActionChannel.PLAY);//00_02
        data.put("actionType", actionType);
        data.put("contentId",contentId);
        if(actionType.equals(PbsUaConstant.ActionType.RECOMMEND_PLAY)){
            data.put("appointedId",appointedId);
            data.put("recommendType",recommendType);
            data.put("sceneId",sceneId);
        }
        if(!TextUtils.isEmpty(subContentId)){
            data.put("subContentId",subContentId);
        }
        return data;
    }
}