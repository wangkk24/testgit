package com.pukka.ydepg.common.report.ubd.scene;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.UBDService;
import com.pukka.ydepg.common.report.ubd.UBDTool;
import com.pukka.ydepg.common.report.ubd.extension.SMSData;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.launcher.session.SessionService;

public class UBDSMSVerify {

    //跳转到播放界面上报
    public static void record(String optType,String phoneNumber){
        SMSData info = new SMSData();
        info.setStreamNo(System.currentTimeMillis()+ CommonUtil.getMac());
        info.setOptTime(UBDTool.getInstance().getUBDFormatTime(DateCalendarUtils.PATTERN_B));
        info.setOptType(optType);
        info.setStbUser(SessionService.getInstance().getSession().getUserId());
        info.setMobilePhone(phoneNumber);
        info.setDeviceType(UBDConstant.DEVICE_STB);
        String extensionField = JsonParse.object2String(info, SMSData.class);
        UBDService.recordSwitchPage(UBDConstant.Function.SMS_VERIFY,UBDConstant.Function.SMS_VERIFY,extensionField);
    }
}
