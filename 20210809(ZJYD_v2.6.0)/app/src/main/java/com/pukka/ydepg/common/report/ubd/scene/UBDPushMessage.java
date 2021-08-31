package com.pukka.ydepg.common.report.ubd.scene;

import android.text.TextUtils;

import com.huawei.ott.sdk.ubd.Common;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.UBDService;
import com.pukka.ydepg.common.report.ubd.extension.PushMessageData;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.xmpp.utils.XmppUtil;

import org.json.JSONObject;

public class UBDPushMessage {
    /**
     * 推送消息到达上报
     * 2.1需求  201906275341
     * */
    public static void recordArrived(JSONObject msgBody){
        String messageId =  getMessageId(msgBody);
        if (TextUtils.isEmpty(messageId)){
            SuperLog.info2SDDebug(UBDConstant.TAG,"messageId is empty, no need to report push message.");
            return;
        }

        Common common = new Common();
        common.setActionType(UBDConstant.ActionType.MESSAGE_ARRIVED);
        PushMessageData data = new PushMessageData();
        data.setUserID(SessionService.getInstance().getSession().getUserId());
        data.setMessageId(messageId);
        common.setlabel(JsonParse.object2String(data, PushMessageData.class));
        UBDService.recordCommon(common);
    }

    public static void recordImpression(JSONObject msgBody) {
        String messageId =  getMessageId(msgBody);
        if (TextUtils.isEmpty(messageId)){
            SuperLog.info2SDDebug(UBDConstant.TAG,"messageId is empty, no need to report push message.");
            return;
        }

        Common common = new Common();
        common.setActionType(UBDConstant.ActionType.MESSAGE_IMPRESSION);
        PushMessageData data = new PushMessageData();
        data.setUserID(SessionService.getInstance().getSession().getUserId());
        data.setMessageId(messageId);
        common.setlabel(JsonParse.object2String(data, PushMessageData.class));
        UBDService.recordCommon(common);
    }

    /**
     * 推送消息转化上报
     * 2.1需求  201906275341
     * */
    public static void recordConversion(String messageId, String pageToActivity){
        Common common = new Common();
        common.setActionType(UBDConstant.ActionType.MESSAGE_CONVERT);
        PushMessageData data = new PushMessageData();
        data.setUserID(SessionService.getInstance().getSession().getUserId());
        data.setMessageId(messageId);
        data.setPageTo(pageToActivity);
        common.setlabel(JsonParse.object2String(data, PushMessageData.class));
        UBDService.recordCommon(common);
    }

    private static String getMessageId(JSONObject msgBody){
        String messageId = null;
        try{
            if ( "5".equals(XmppUtil.getString(msgBody,"mode")) ){
                JSONObject objectContent   = msgBody.getJSONObject("content");
                JSONObject extensionObject = new JSONObject(objectContent.getString("extensionInfo"));
                messageId = XmppUtil.getString(extensionObject,"messageId");
                if (TextUtils.isEmpty(messageId)){
                    messageId = XmppUtil.getString(objectContent,"linkURL");
                }
            } else if ("6".equals(XmppUtil.getString(msgBody,"mode"))){
                String content = XmppUtil.getString(msgBody,"content");
                if (!TextUtils.isEmpty(content) && content.length() > 10){
                    messageId = content.substring(0,10);
                }else{
                    messageId = content;
                }
            } else {
                messageId = null;
            }
        } catch (Exception e){
            SuperLog.error(UBDConstant.TAG,e);
        }
        return messageId;
    }
}
