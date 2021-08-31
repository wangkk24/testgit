package com.pukka.ydepg.common.report.ubd.scene;

import com.huawei.ott.sdk.ubd.Common;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.UBDService;
import com.pukka.ydepg.common.report.ubd.extension.VersionData;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.launcher.session.SessionService;

public class UBDVersion {
    public static void record(){
        Common common = new Common();
        common.setActionType(UBDConstant.ActionType.VERSION);
        VersionData field = new VersionData();
        field.setUserID(SessionService.getInstance().getSession().getUserId());
        field.setBillID(SessionService.getInstance().getSession().getAccountName());
        field.setEpgVersion(CommonUtil.getVersionName());
        field.setStbModel(CommonUtil.getDeviceType());
        field.setStbID(CommonUtil.getSTBID());
        field.setAreaID(SessionService.getInstance().getSession().getUserAreaCode());
        common.setlabel(JsonParse.object2String(field, VersionData.class));
        UBDService.recordCommon(common);
    }
}