package com.pukka.ydepg.common.report.ubd.scene;

import android.text.TextUtils;

import com.huawei.ott.sdk.ubd.Common;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.UBDService;
import com.pukka.ydepg.common.report.ubd.UBDTool;
import com.pukka.ydepg.common.report.ubd.extension.PurchaseData;
import com.pukka.ydepg.common.report.ubd.extension.SwitchData;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewMyPayModeActivity;

public class UBDPurchase {

    //H5 pageName
    private static String pageName;

    //H5页面订购上报参数
    private static String srcch = "";

    //引流页面
    private static String fromActivity;

    //引流位置信息
    private static SwitchData leadSubscribeData = new SwitchData();





    private static String getPageName(String url) {
        int startPos = url.lastIndexOf("/") + 1;
        int endPos = url.lastIndexOf(".jsp");
        return url.substring(startPos, endPos);
    }





    public static void cachePageName(String url) {
        try {
            pageName = getPageName(url);
        } catch (Exception e) {
            pageName = null;
        }
    }

    public static void setSrcch(String src) {
        srcch = src;
    }

    public static void clearH5Info() {
        SuperLog.debug(UBDConstant.TAG, "clearH5Info");
        srcch = null;
        pageName = null;
    }


    public static void setFromActivity(String fromActivity) {
        UBDPurchase.fromActivity = fromActivity;
    }

    public static SwitchData getLeadSubscribeData() {
        return leadSubscribeData;
    }

    //成功订购上报
    public static void record(Product mProduct, String vodId, String isSuccess) {
        Common common = new Common();
        PurchaseData field = new PurchaseData();
        field.setIsSubscriptionSucceed(isSuccess);
        field.setUserID(SessionService.getInstance().getSession().getUserId());
        String time = UBDTool.getInstance().getUBDFormatTime(DateCalendarUtils.PATTERN_A);
        field.setTimestamp(time);
        field.setPackageID(mProduct.getID());
        field.setContentID(vodId);
        if (TextUtils.isEmpty(srcch)){
            //srcch渠道码为空时来源为EPG
            field.setSource(PurchaseData.SOURCE_EPG);
        } else {
            //srcch渠道码不为空时来源为H5
            field.setSource(PurchaseData.SOURCE_H5);
            field.setSrcch(srcch);
            field.setPageName(pageName);
        }
        common.setlabel(JsonParse.object2String(field, PurchaseData.class));
        common.setActionType(UBDConstant.ActionType.SUBSCRIBE);
        UBDService.recordCommon(common);

        //订购引流上报
        //1 点击资源位 信息从UBDMainOnClick.recordMainOnclick中获取
        //2 没有点击资源位(比如首页点击导航栏/搜索)信息通过本类的setFromNavId方法设置,调用处请自行搜索
        leadSubscribeData.setActionID(UBDConstant.ACTION.LEAD_SUBSCRIBE);
        leadSubscribeData.setVersion(LauncherService.getInstance().getDesktopVersion());
        leadSubscribeData.setUserID(SessionService.getInstance().getSession().getUserId());
        leadSubscribeData.setSystemTime(time);
        UBDService.recordSwitchPage(
                MainActivity.class.getSimpleName()+"_"+leadSubscribeData.getFromNavID(),
                NewMyPayModeActivity.class.getSimpleName(),
                JsonParse.object2String(leadSubscribeData));
    }

    //用于从首页没有点击任何资源进入其他页面时保存引流信息,比如首页点击导航栏/搜索
    static void setFromNavId(String navId){
        leadSubscribeData.setFromNavID(navId);
        leadSubscribeData.setNavIndex(null);
        leadSubscribeData.setPageID(null);

        leadSubscribeData.setGroupIndex(null);
        leadSubscribeData.setGroupID(null);
        leadSubscribeData.setControlID(null);

        leadSubscribeData.setElementID(null);
        leadSubscribeData.setElementIndex(null);
        leadSubscribeData.setElementType(null);

        leadSubscribeData.setContentID(null);
        leadSubscribeData.setContentName(null);
        leadSubscribeData.setContentType(null);

        leadSubscribeData.setEpisodeID(null);
        leadSubscribeData.setRecommendType(null);
    }

    public static String getSrcch() {
        return srcch;
    }
}