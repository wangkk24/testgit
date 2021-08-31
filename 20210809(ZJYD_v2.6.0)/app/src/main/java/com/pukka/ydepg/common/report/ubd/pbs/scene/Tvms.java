package com.pukka.ydepg.common.report.ubd.pbs.scene;

import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;

import java.util.HashMap;
import java.util.Map;

public class Tvms {

    private Tvms(){};

    /* TVMS消息弹窗
     * actionChannel_1 : 1级渠道号
     *                      详情页	    1
     *                      播放页	    2(无)
     *                      订购中心	    3
     *                      直播播放	    4
     *                      活动(PBS页面)	5
     *                      其他原生页面  6
     * messageName     : 消息ID
     */
    public static Map<String,String> getMessageDialogData(String actionChannel_1, String messageId){
        Map<String,String> data = new HashMap<>();
        data.put("actionChannel", PbsUaConstant.ActionChannel_0.EPG+"_"+actionChannel_1);
        data.put("messageId",messageId);
        data.put("messageTime",String.valueOf(System.currentTimeMillis()));
        return data;
    }

    public static String addPurchaseInfo(String pbsUrl,String messageId){
        if(pbsUrl.contains("?")){
            pbsUrl = pbsUrl + "&";
        } else {
            pbsUrl = pbsUrl + "?";
        }
        pbsUrl = pbsUrl + "messageId="   + messageId;
        pbsUrl = pbsUrl + "&messageTime=" + System.currentTimeMillis();
        return pbsUrl;
    }
}