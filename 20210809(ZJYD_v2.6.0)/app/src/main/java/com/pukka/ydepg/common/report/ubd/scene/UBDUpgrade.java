package com.pukka.ydepg.common.report.ubd.scene;

import com.huawei.ott.sdk.ubd.Common;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.UBDService;
import com.pukka.ydepg.common.report.ubd.extension.UpgradeData;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.launcher.session.SessionService;

public class UBDUpgrade {
    //202002297634_(20200217)浙江移动手机视频项目FRS 【EPG】EPG版本号上报统计
    public static void record(String version,String force,String reason){
        Common common = new Common();
        common.setActionType(UBDConstant.ActionType.UPGRADE);
        UpgradeData field = new UpgradeData();
        //field.userID     = AuthenticateManager.getInstance().getUserInfo().getUserId();
        field.setUserID(SessionService.getInstance().getSession().getUserId());
        field.setBillID(SessionService.getInstance().getSession().getAccountName());
        field.setEpgVersion(CommonUtil.getVersionName());
        field.setStbModel(CommonUtil.getDeviceType());
        field.setUpgradeVersion( version==null ? "unknown":version );
        field.setForce( force==null ? "unknown":force );
        field.setReason(reason);
        common.setlabel(JsonParse.object2String(field, UpgradeData.class));
        UBDService.recordCommon(common);
    }
}
