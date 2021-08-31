package com.pukka.ydepg.common.report.ubd.scene;

import com.huawei.ott.sdk.ubd.Common;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.UBDService;
import com.pukka.ydepg.common.report.ubd.UBDTool;
import com.pukka.ydepg.common.report.ubd.extension.TopFunctionData;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.featured.view.LauncherService;

public class UBDTopFunction {

    public static void reportTopFunction(String displayName,String buttonType){
        UBDSwitch.getInstance().isEPGTopOnClick = true;
        Common common = new Common();
        TopFunctionData field = new TopFunctionData();
        field.setDisplayName(displayName);
        field.setButtonType(buttonType);
        field.setUserID(SessionService.getInstance().getSession().getUserId());
        field.setDesktopVersion(LauncherService.getInstance().getDesktopVersion());
        field.setTimestamp(UBDTool.getInstance().getUBDFormatTime(DateCalendarUtils.PATTERN_C));
        common.setlabel(JsonParse.object2String(field, TopFunctionData.class));
        common.setActionType(UBDConstant.ActionType.TOP_FUNCTION);
        UBDService.recordCommon(common);
    }
}
