package com.pukka.ydepg.common.report.ubd.scene;

import android.text.TextUtils;

import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.Navigate;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.UBDService;
import com.pukka.ydepg.common.report.ubd.UBDTool;
import com.pukka.ydepg.common.report.ubd.extension.SwitchData;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.ChildLauncherActivity;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.activity.TopicActivity;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;
import com.pukka.ydepg.moudule.search.SearchActivity;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.VodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.VodMainActivity;

import java.util.Date;
import java.util.List;

/**
 * Created by Eason on 26-Apr-20.
 */
public class UBDSwitch {

    /********* 变量区 *********/
    private static final String TAG = UBDSwitch.class.getSimpleName();

    private static UBDSwitch instance;

    //用于首页点击事件 组织navIndex
    private int navPosition = 0;

    //首页的扩展信息
    private final SwitchData mainData = new SwitchData();

    //详情页的扩展信息
    private final SwitchData detailData = new SwitchData();

    //非首页非详情页的扩展信息
    private final SwitchData otherData = new SwitchData();

    //启动后第一个页面是MainActivity
    private String fromActivity = MainActivity.class.getSimpleName();

    private String toApkActivity;



    /********* 静态方法区 *********/
    public static UBDSwitch getInstance(){
        if( instance == null ){
            instance = new UBDSwitch();
        }
        return instance;
    }



    /********* 私有方法区 *********/
    private UBDSwitch(){}

    Navigate getNavigate(int navPosition){
        if(navPosition>=0){
            List<Navigate> navigates = LauncherService.getInstance().getLauncher().getNavigateList();
            if(navigates == null || navPosition >=navigates.size()){
                SuperLog.error(TAG,"Nav position out of range, no need to report the ubd report.");
                return null;
            }
            return navigates.get(navPosition);
        } else if(navPosition == UBDConstant.NavPosition.SIMPLE){
            return LauncherService.getInstance().getNavigateSimple();
        } else if(navPosition == UBDConstant.NavPosition.CHILD){
            return LauncherService.getInstance().getNavigateChild();
        } else {
            SuperLog.error(TAG,"Nav position info error, no need to report the ubd report.");
            return null;
        }
    }



    /********* 公有方法区 *********/
    public void setFromActivity(String fromActivity) {
        this.fromActivity = fromActivity;
    }

    public String getFromActivity() {
        return fromActivity;
    }

    public void setNavPosition(int navPosition){
        this.navPosition = navPosition;
    }
    int getNavPosition(){
        return navPosition;
    }

    public SwitchData getMainExtensionField(){
        return mainData;
    }

    //是否是功能键和跑马灯的点击事件
    boolean isEPGTopOnClick = false;

    //组织首页静态、动态、我的收藏、观看历史资源位点击事件上报UBD数据
    public void recordMainOnclick(String actionUrl, Element element, Group group,VOD vod,String sequence){
        isEPGTopOnClick = false;
        Navigate navigate = getNavigate(navPosition);
        if(navigate == null){
            return;
        }

        mainData.setFromNavID(navigate.getId());
        mainData.setNavIndex(navigate.getIndex());
        mainData.setPageID(navigate.getPageList().get(0).getId());
        mainData.setSequence(sequence);
        if (null != group){
            mainData.setGroupIndex(group.getIndex());
            mainData.setGroupID(group.getId());
            mainData.setControlID(group.getControlInfo().getControlId());
        } else {
            mainData.setGroupIndex(null);
            mainData.setGroupID(null);
            mainData.setControlID(null);
        }

        //elementID != null且 groupID=null 无效话单 (即有elementID则一定有groupID)
        if (null != element){
            mainData.setElementID(element.getId());
            mainData.setElementIndex(element.getIndex());
            mainData.setElementType(element.getType());
        } else {
            mainData.setElementID(null);
            mainData.setElementIndex(null);
            mainData.setElementType(null);
        }

        if (!TextUtils.isEmpty(actionUrl)){
            mainData.setContentID(ZJVRoute.getContentValue(actionUrl,ZJVRoute.ActionUrlKeyType.CONTENT_ID));
            mainData.setContentName(ZJVRoute.getContentValue(actionUrl,ZJVRoute.ActionUrlKeyType.CONTENT_CODE));
            mainData.setContentType(ZJVRoute.getContentValue(actionUrl,ZJVRoute.ActionUrlKeyType.CONTENT_TYPE));
        }else if (null != vod){
            mainData.setContentID(vod.getID());
            mainData.setContentName(vod.getName());
            mainData.setContentType(vod.getContentType());
        }else{
            mainData.setContentID(null);
            mainData.setContentName(null);
            mainData.setContentType(null);
        }

        //保存订购引流信息
        UBDPurchase.getLeadSubscribeData().setFromNavID(mainData.getFromNavID());
        UBDPurchase.getLeadSubscribeData().setNavIndex(mainData.getNavIndex());
        UBDPurchase.getLeadSubscribeData().setPageID(mainData.getPageID());

        UBDPurchase.getLeadSubscribeData().setGroupIndex(mainData.getGroupIndex());
        UBDPurchase.getLeadSubscribeData().setGroupID(mainData.getGroupID());
        UBDPurchase.getLeadSubscribeData().setControlID(mainData.getControlID());

        UBDPurchase.getLeadSubscribeData().setElementID(mainData.getElementID());
        UBDPurchase.getLeadSubscribeData().setElementIndex(mainData.getElementIndex());
        UBDPurchase.getLeadSubscribeData().setElementType(mainData.getElementType());

        UBDPurchase.getLeadSubscribeData().setContentID(mainData.getContentID());
        UBDPurchase.getLeadSubscribeData().setContentName(mainData.getContentName());
        UBDPurchase.getLeadSubscribeData().setContentType(mainData.getContentType());

        //判断是否是跳转到第三方apk,如果是直接上报
        if (isApk(actionUrl)){
            toApkActivity = getApkPkg(actionUrl);
        }
    }

    //供开机启动完成后上报UBD的定制方法
    public void reportStartupInMainActivity(int navPosition){
        this.navPosition = navPosition;
        SwitchData data = new SwitchData();
        data.setActionID(UBDConstant.ACTION.START);
        data.setVersion(LauncherService.getInstance().getDesktopVersion());
        data.setUserID(SessionService.getInstance().getSession().getUserId());
        String extensionData = JsonParse.object2String(data);
        UBDService.recordSwitchPage(MainActivity.class.getSimpleName()+"_ad",MainActivity.class.getSimpleName()+"_"+ getNavigate(navPosition).getId(), extensionData);
    }

    //供BaseActivity上报UBD的定制方法
    public void reportInBaseActivity(String toActivity){
        if (isEPGTopOnClick){
            isEPGTopOnClick = false;
            return;
        }
        if(UBDTool.getInstance().isReport(toActivity)){//在过滤列表中的Activity不在BaseActivity中上报UBD,由其自行处理
            report(UBDConstant.ACTION.COMMON_CLICK,toActivity);
        }

        //解决从首页轮播模板直接跳转到点播页面时该方法不上报,由播放页面自行上报,缺少UBD话单首页曝光数据的问题
        if(MainActivity.class.getSimpleName().equals(fromActivity) && OnDemandVideoActivity.class.getSimpleName().equals(toActivity)){
            report(UBDConstant.ACTION.COMMON_CLICK,toActivity);
        }
    }

    //供详情页面上报UBD的定制方法
    @Deprecated
    public void reportInVodDetailActivity(String name, String id, String contentType, String recommendType){

        //如果从首页进入详情页,需要此数据,由于首页点击每次会重新赋值,因此不用担心污染数据
        mainData.setContentName(name);
        mainData.setContentID(id);
        mainData.setContentType(contentType);
        if(!TextUtils.isEmpty(recommendType)){
            mainData.setRecommendType(recommendType);
            UBDPurchase.getLeadSubscribeData().setRecommendType(recommendType);
        }

        //从非首页进入详情页,需要此数据
        detailData.setContentName(name);
        detailData.setContentID(id);
        detailData.setContentType(contentType);
        if(!TextUtils.isEmpty(recommendType)){
            detailData.setRecommendType(recommendType);
            UBDPurchase.getLeadSubscribeData().setRecommendType(recommendType);
        }

        report(UBDConstant.ACTION.VIEW_VOD_DETAIL, ChildModeVodDetailActivity.class.getSimpleName());
    }

    //供详情页面上报UBD的定制方法
    public void reportInVodDetailActivity(
            String name,
            String id,
            String contentType,
            String recommendType, //推荐用上报数据
            String sceneId,       //推荐用上报数据
            String appPointedId,   //推荐用上报数据
            boolean isFromMainActivity //从首页跳转进入详情页，recommendType、sceneId、appPointedId无需重新赋值
    ){
        //如果从首页进入详情页,需要此数据,由于首页点击每次会重新赋值,因此不用担心污染数据
        mainData.setContentName(name);
        mainData.setContentID(id);
        mainData.setContentType(contentType);

        //推荐数据
        if (!isFromMainActivity){
            mainData.setRecommendType(recommendType);
            mainData.setSceneId(sceneId);
            mainData.setAppointedId(appPointedId);
        }

        //从非首页进入详情页,需要此数据
        detailData.setContentName(name);
        detailData.setContentID(id);
        detailData.setContentType(contentType);
        if(!TextUtils.isEmpty(recommendType)){
            detailData.setRecommendType(recommendType);
            detailData.setSceneId(sceneId);
            detailData.setAppointedId(appPointedId);
        }

        UBDPurchase.getLeadSubscribeData().setRecommendType(recommendType);
        UBDPurchase.getLeadSubscribeData().setSceneId(sceneId);
        UBDPurchase.getLeadSubscribeData().setAppointedId(appPointedId);

        report(UBDConstant.ACTION.VIEW_VOD_DETAIL,NewVodDetailActivity.class.getSimpleName());
    }

    //供儿童版详情页面上报UBD的定制方法
    public void reportInVodDetailActivityChild(
            String name,
            String id,
            String contentType,
            String recommendType, //推荐用上报数据
            String sceneId,       //推荐用上报数据
            String appPointedId   //推荐用上报数据
    ){
        SwitchData childData = new SwitchData();
        childData.setContentName(name);
        childData.setContentID(id);
        childData.setContentType(contentType);
        childData.setActionID(UBDConstant.ACTION.VIEW_VOD_DETAIL);

        //推荐数据
        childData.setRecommendType(recommendType);
        childData.setSceneId(sceneId);
        childData.setAppointedId(appPointedId);

        //通用数据
        childData.setVersion(LauncherService.getInstance().getDesktopVersion());
        childData.setUserID(SessionService.getInstance().getSession().getUserId());
        childData.setSystemTime(DateCalendarUtils.getTime(new Date(),DateCalendarUtils.PATTERN_A));

        String extensionData = JsonParse.object2String(childData);

        UBDService.recordSwitchPage(ChildModeVodDetailActivity.class.getSimpleName(), ChildModeVodDetailActivity.class.getSimpleName(), extensionData);
    }

    public void report(String actionID, String toActivity){
        if(TextUtils.isEmpty(fromActivity) || TextUtils.isEmpty(toActivity)  ){
            SuperLog.error(TAG,"Fatal error! FromActivity/ToActivity is empty, no need to report UBD.");
            return;
        }

        if(fromActivity.equals(toActivity) && MainActivity.class.getSimpleName().equals(fromActivity)){
            SuperLog.info2SDDebug(TAG,"FromActivity is the same to ToActivity, no need to report UBD.");
            return;
        }

        String extensionData;
        if(MainActivity.class.getSimpleName().equals(this.fromActivity) ||
                (ChildLauncherActivity.class.getSimpleName().equals(this.fromActivity) && !MainActivity.class.getSimpleName().equals(toActivity))){
            //====<<<===从[首页主动跳转其他页面]从[二级首页主动跳转其他页面](不包括二级首页返回首页的情况)
            //判断逻辑为FromActivity是否是首页MainActivity或二级首页ChildLauncherActivity
            //这种情况要携带资源位信息
            if(MainActivity.class.getSimpleName().equals(this.fromActivity)){
                if(toActivity.equals(VodMainActivity.class.getSimpleName())){
                    //从[首页]点击导航栏进入[VodMainActivity]
                    mainData.setFromNavID(getNavigate(navPosition).getId());
                } else if (toActivity.equals(SearchActivity.class.getSimpleName())){
                    //从[首页]进[搜索]
                    mainData.setFromNavID("search");
                } else {
                    //从首页进其他页面,已经在MainActivity点击对应资源位时通过调用recordMainOnclick方法设置过了无需设置
                }
                UBDPurchase.setFromNavId(mainData.getFromNavID());

                if(TextUtils.isEmpty(mainData.getFromNavID())){
                    SuperLog.error(TAG,"Fatal error! FromActivity is MainActivity and navID is empty, no need to report UBD.");
                    return;
                }
                fromActivity = this.fromActivity + "_" + mainData.getFromNavID();
            }
            mainData.setActionID(actionID);
            mainData.setVersion(LauncherService.getInstance().getDesktopVersion());
            mainData.setUserID(SessionService.getInstance().getSession().getUserId());
            mainData.setSystemTime(DateCalendarUtils.getTime(new Date(),DateCalendarUtils.PATTERN_A));
            extensionData = JsonParse.object2String(mainData);
        } else if(VodDetailActivity.class.getSimpleName().equals(toActivity)
                || NewVodDetailActivity.class.getSimpleName().equals(toActivity)){
            //====<<<===[跳转到详情页面](不包括首页去详情,首页去详情属于从首页跳转其他页面)ToActivity是否是VodDetailActivity
            detailData.setActionID(actionID);
            detailData.setVersion(LauncherService.getInstance().getDesktopVersion());
            detailData.setUserID(SessionService.getInstance().getSession().getUserId());
            detailData.setSystemTime(DateCalendarUtils.getTime(new Date(),DateCalendarUtils.PATTERN_A));
            extensionData = JsonParse.object2String(detailData);
        } else {
            //====<<<===[返回上一级页面]或从[非首页去非详情页面]的情况
            if(MainActivity.class.getSimpleName().equals(toActivity) && !TextUtils.isEmpty(mainData.getFromNavID())){
                //从任意页面返回首页要携带首页的navID
                toActivity = toActivity + "_" + mainData.getFromNavID();
            }
            otherData.setActionID(actionID);
            otherData.setVersion(LauncherService.getInstance().getDesktopVersion());
            otherData.setUserID(SessionService.getInstance().getSession().getUserId());
            otherData.setSystemTime(DateCalendarUtils.getTime(new Date(),DateCalendarUtils.PATTERN_A));
            extensionData = JsonParse.object2String(otherData);
        }

        if (TopicActivity.class.getSimpleName().equalsIgnoreCase(toActivity) && !TextUtils.isEmpty(TopicActivity.getTopicId())){
            toActivity = toActivity + "_" + TopicActivity.getTopicId();
        }

        if (TopicActivity.class.getSimpleName().equalsIgnoreCase(this.fromActivity) && !TextUtils.isEmpty(TopicActivity.getTopicId())){
            this.fromActivity = this.fromActivity + "_" + TopicActivity.getTopicId();
        }

        if (OnDemandVideoActivity.class.getSimpleName().equalsIgnoreCase(this.fromActivity) && NewVodDetailActivity.class.getSimpleName().equals(toActivity) && !TextUtils.isEmpty(NewVodDetailActivity.getTopicId())){
            toActivity = toActivity + "_" + NewVodDetailActivity.getTopicId();
        }

        UBDService.recordSwitchPage(fromActivity, toActivity, extensionData);

        //上报完成后清理脏数据
        mainData.setRecommendType(null);
        mainData.setExtraTwo(null);

        detailData.setRecommendType(null);
        detailData.setExtraTwo(null);
    }

    //上传专题样式类型
    public void setTopicStyleId(String topicStyleId){
        mainData.setExtraTwo(topicStyleId);
    }

    //是否是跳转三方APK
    private boolean isApk(String actionUrl){
        String contentType = ZJVRoute.getContentValue(actionUrl,ZJVRoute.ActionUrlKeyType.CONTENT_TYPE);
        if (!TextUtils.isEmpty(contentType) && contentType.equalsIgnoreCase(ZJVRoute.LauncherElementContentType.APK)){
            return true;
        }else{
            return false;
        }
    }

    //获取第三方apk报名
    private String getApkPkg(String actionUrl){
        return ZJVRoute.getContentValue(actionUrl,ZJVRoute.ActionUrlKeyType.APP_PKG);
    }

    public void reportToOtherApk(){
        if(!TextUtils.isEmpty(toApkActivity) && !TextUtils.isEmpty(mainData.getFromNavID())){
            mainData.setActionID(UBDConstant.ACTION.COMMON_CLICK);
            mainData.setVersion(LauncherService.getInstance().getDesktopVersion());
            mainData.setUserID(SessionService.getInstance().getSession().getUserId());
            mainData.setSystemTime(DateCalendarUtils.getTime(new Date(),DateCalendarUtils.PATTERN_A));
            String extensionData = JsonParse.object2String(mainData);
            UBDService.recordSwitchPage(MainActivity.class.getSimpleName()+"_"+mainData.getFromNavID(), toApkActivity, extensionData);
            mainData.setRecommendType(null);
            mainData.setSceneId(null);
            mainData.setAppointedId(null);
            mainData.setExtraTwo(null);
            fromActivity = toApkActivity;
            toApkActivity = null;
        }
    }
}