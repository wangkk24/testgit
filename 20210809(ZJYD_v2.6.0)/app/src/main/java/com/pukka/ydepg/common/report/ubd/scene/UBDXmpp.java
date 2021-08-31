package com.pukka.ydepg.common.report.ubd.scene;

import com.pukka.ydepg.common.report.ubd.UBDService;
import com.pukka.ydepg.common.report.ubd.UBDTool;
import com.pukka.ydepg.common.report.ubd.extension.XmppData;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.launcher.session.SessionService;

public class UBDXmpp {
    //跳转到播放界面上报
    public static  void recordXmppOperation(String optType,String xmppMessage,String streamNo){
        XmppData info = new XmppData();
        String optTime = UBDTool.getInstance().getUBDFormatTime(DateCalendarUtils.PATTERN_B);
        info.setOptTime(optTime);
        info.setStreamNo(streamNo);
        info.setOptType(optType);
        info.setStbUserId(SessionService.getInstance().getSession().getUserId());
        info.setParams(xmppMessage);
        String extensionField = JsonParse.object2String(info, XmppData.class);
        UBDService.recordSwitchPage("appXmpp","epgXmpp",extensionField);
    }
}
