package com.pukka.ydepg.common.report.ubd.scene;

import com.huawei.ott.sdk.ubd.Common;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.UBDService;
import com.pukka.ydepg.common.report.ubd.extension.DesktopTypeData;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.session.SessionService;

public class UBDDesktopType {

    //是否是开机启动上报
    public static boolean isStart = true;

    //上一次使用的桌面类型
    public static String lastType;

    //type:normal/child/simple
    public static void record(String type){
        if(type == null || type.equals(lastType)){
            return;
        }
        lastType = type;

        Common common = new Common();
        common.setActionType(UBDConstant.ActionType.DESKTOP_TYPE);
        DesktopTypeData field = new DesktopTypeData();
        field.setUserID(SessionService.getInstance().getSession().getUserId());
        field.setEpgVersion(CommonUtil.getVersionName());
        if(isStart){
            field.setAction("start");
            isStart = false;
        } else {
            field.setAction("switch");
        }

        field.setDesktopType(type);
        common.setlabel(JsonParse.object2String(field, DesktopTypeData.class));
        UBDService.recordCommon(common);
    }
}