package com.pukka.ydepg.common.report.ubd.scene;

import com.huawei.ott.sdk.ubd.Common;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.UBDService;
import com.pukka.ydepg.common.report.ubd.UBDTool;
import com.pukka.ydepg.common.report.ubd.extension.ScreenAdClickData;
import com.pukka.ydepg.common.report.ubd.extension.ScreenAdImpressionData;
import com.pukka.ydepg.common.report.ubd.extension.ConfigBannerImpressionData;
import com.pukka.ydepg.common.screensaver.model.ScreenAdvertContent;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;

public class UBDAdvert {
    public static void reportScreenImpression(ScreenAdvertContent content){
        Common common = new Common();
        ScreenAdImpressionData field = new ScreenAdImpressionData();
        field.setId(content.getContentID());
        field.setName(content.getContentName());
        String time = UBDTool.getInstance().getUBDFormatTime(DateCalendarUtils.PATTERN_C);
        field.setTimestamp(time);
        field.setFromActivity(OTTApplication.getContext().getCurrentActivity().getClass().getSimpleName());
        common.setlabel(JsonParse.object2String(field, ScreenAdImpressionData.class));
        common.setActionType(UBDConstant.ActionType.SCREEN_IMPRESSION);
        UBDService.recordCommon(common);
    }

    public static void reportScreenClick(ScreenAdvertContent content){
        Common common = new Common();
        ScreenAdClickData field = new ScreenAdClickData();
        field.setId(content.getContentID());
        field.setName(content.getContentName());
        String time = UBDTool.getInstance().getUBDFormatTime(DateCalendarUtils.PATTERN_C);
        field.setTimestamp(time);
        field.setUrl(content.getClickUrl());
        field.setPkg(content.getPkg());
        field.setCls(content.getCls());
        field.setExtra(content.getExtra());
        common.setlabel(JsonParse.object2String(field, ScreenAdClickData.class));
        common.setActionType(UBDConstant.ActionType.SCREEN_CLICK);
        UBDService.recordCommon(common);
    }

    //上报配置广告,目前包括[1-VOD banner广告位] [2-退订挽留页顶部Banner]
    public static void reportConfigBannerImpression(String id, String name, String advertUrl,String actionType){
        Common common = new Common();
        ConfigBannerImpressionData field = new ConfigBannerImpressionData();
        field.setId(id);
        field.setName(name);
        field.setUrl(advertUrl);
        common.setlabel(JsonParse.object2String(field, ConfigBannerImpressionData.class));
        common.setActionType(actionType);
        UBDService.recordCommon(common);
    }
}