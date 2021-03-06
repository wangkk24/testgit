package com.pukka.ydepg.common.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import com.pukka.ydepg.ExoMediaPlayer;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.dialog.ParentSetCenterDialog;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.ui.ChildLauncherActivity;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.activity.AllAppInfoActivity;
import com.pukka.ydepg.launcher.ui.activity.TopicActivity;
import com.pukka.ydepg.launcher.ui.fragment.WebActivity;
import com.pukka.ydepg.launcher.view.topic.TopicViewManager;
import com.pukka.ydepg.moudule.catchup.activity.CatchUpActivity;
import com.pukka.ydepg.moudule.catchup.common.TVODDataUtil;
import com.pukka.ydepg.moudule.children.activity.ParentSetCenterActivity;
import com.pukka.ydepg.moudule.children.report.ChildrenConstant;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.featured.view.TopicService;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.mytv.AccountManagerActivity;
import com.pukka.ydepg.moudule.mytv.MessageActivity;
import com.pukka.ydepg.moudule.mytv.NewMyMovieActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewOrderCenterActivity;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.search.SearchActivity;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.VodMainActivity;
import com.pukka.ydepg.moudule.vod.utils.MultViewUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * activity??????????????????
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.common.utils.uiutil.ZJRoute.java
 * @date: 2017-12-18 17:14
 * @version: V1.0 activity??????????????????
 */

public class ZJVRoute {

    private static final String TAG = ZJVRoute.class.getSimpleName();
    //????????????pkg
    private static final String JHXC_PKG = "com.chinamobile.mcloudtv2";

    //??????????????????????????????????????????????????????
    public static final String H5_URL = "H5Url";

    public static class LauncherElementDataType {

        public static final String STATIC_ITEM = "staticItem";

        public static final String VOD = "vod";

        public static final String VOD_HISTORY = "vod_history";

        public static final String CHANNEL_SUBJECT = "channel_subject";

        public static final String VOD_SUBJECT = "vod_subject";

        public static final String TVOD_PROGRAM = "tvod_program";

        public static final String NEXT_PROGRAM = "next_program";

        public static final String CURRENT_PROGRAM = "current_program";

        public static final String MYTV_ITEM = "mytv_item";
    }

    public static class ActionUrlKeyType {
        public static final String CONTENT_TYPE = "ContentType";
        public static final String SUB_CONTENT_TYPE = "SubContentType";
        public static final String CONTENT_ID = "ContentID";
        public static final String SUBJECT_ID = "SubjectID";
        public static final String FOCUS_COTENTID = "FocusContentID";
        public static final String APP_PKG = "AppPkg";
        public static final String APP_CLASS = "AppClass";
        public static final String VERSION = "Version";
        public static final String APK_URL = "ApkUrl";
        public static final String ACTION = "Action";
        public static final String CONTENT_CODE = "ContentCode";
        public static final String CLASS_NAME = "ClassName";
        public static final String CHANNEL_MINI_TYPE = "ChannelMiniType";//==-1,????????????????????????Mini EPG???????????????;==0:??????????????????
        public static final String FOURK_CONTENT_ID = "4kContentID";//???4kContentID?????????????????????4kContentID????????????????????????????????????????????????4kContentID?????????ContentID????????????
        public static final String VODID = "VODId";
        public static final String TYPE = "type";//==apk,????????????????????????????????????app???????????????
        public static final String KEY = "key";//?????????????????????element???id,???PHM?????????????????????????????????element??????????????????????????????app????????????????????????id???Nav?????????????????????key????????????
        public static final String THIRDAPK = "ThirdApk";//UBD??????????????????APK??? ??????toActivity
        public static final String MULTI_CAST_ID = "MultiCastID";//????????????????????????????????????????????????id???ContentID??????????????????Id

        /**
         * ???????????????????????????????????????
         * actionurl??????switchingTime???????????????????????????switchingTime???
         * ???starttime???endtime???????????????????????????????????????
         * */
        public static final String START_TIME = "startTime";//
        public static final String END_TIME = "endTime";
        public static final String SWITCHING_TIME = "switchingTime";

    }

    public static class LauncherElementActionType {
        public static final String TYPE_0 = "0";//????????????          --???????????????
        public static final String TYPE_1 = "1";//????????????????????????   --?????????H5??????
        public static final String TYPE_2 = "2";//????????????          --??????????????????????????????????????????????????????VOD??????????????????
        public static final String TYPE_3 = "3";//????????????APP       --???????????????APK
    }

    public static class LauncherElementContentType {
        public static final String INNER_ACTION = "Inner";//?????????APP???????????????????????????

        public static final String VOD_CATEGORY = "VODCategory";//??????

        public static final String CHANNEL = "CHANNEL";//??????

        public static final String STATICVOD = "STATICVOD";//VOD??????

        public static final String TVOD = "TVOD";//???????????????

        public static final String TV_GUIDE = "TVGuide";//???????????????

        public static final String STATIC_VOD_SUBJECT = "StaticVodSubject";//

        public static final String STATIC_VOD = "StaticVod";//VOD??????

        public static final String MULTIPLE = "Multiple";//???????????????

        public static final String MULTIINTERACT = "Multiinteract";//????????????

        public static final String APK = "APK";//?????????apk

        public static final String LOCAL_APK = "Local_APK";//??????????????????

        public static final String VIP = "VIP";//??????????????????

        public static final String HISTORY = "History";//????????????

        public static final String FAVORITES = "Favorites";//??????

        public static final String TVWARCH = "TVWarch";//????????????

        public static final String TTVWARCH = "TTVWarch";//????????????

        public static final String PAGE = "PAGE";//??????????????????

        public static final String QUERYMYCONTENT = "QUERYMYCONTENT";//??????????????????

        public static final String SEARCH = "SEARCH";//????????????

        public static final String CATCHUP = "CATCHUP";//????????????

        public static final String SWITCH_TO_NORMAL_EPG = "SWITCH_TO_EPG";//???????????????epg

        public static final String SWITCH_TO_SIMPLE_EPG = "SWTICH_TO_SIMPLE";//???????????????epg

        public static final String SWITCH_TO_CHILD_EPG  = "SWITCH_TO_CHILD_EPG";//???????????????

        public static final String SWITCH_TO_PARENT = "SWITCH_TO_PARENT";//??????????????????

        public static final String VOD_DETAIL_SWITCH_PARENT="vod_detail_switch_parent";//??????????????????????????????

        public static final String SWITCH_EPG_FROM_CHILDREN = "SWITCH_EPG_FROM_CHILDREN";//????????????????????????????????????-1???

        public static final String SWITCH_EPG = "SWITCH_EPG";//EPG???????????????????????????-1???

        public static final String SETTING = "SETTING";//????????????

        public static final String SWITCH_PROFILE = "switch_profile";//????????????

        public static final String LOCAL_PLAY = "local_play";//????????????

        public static final String MESSAGE = "MESSAGE";//????????????

        public static final String BIND_ACCOUNT = "BIND_ACCOUNT";//????????????

        public static final String NAVIGATE = "NAVIGATE";//????????????????????????????????????

        public static final String TYPE_BOOKMARK = "BOOKMARK";//????????????

        public static final String TYPE_FAVORITE = "FAVORITE";//????????????

        public static final String REFRESH = "REFRESH";//??????
    }

    private static String mClassName = "";

    public static String getClassName(){
        return mClassName;
    }

    public static void route(Context context, String dataType, String actionUrl, String actionType, String contentId, VOD vod, Map<String, String> extraData) {
        SuperLog.debug(TAG, "DATA TYPE: " + dataType);
        SuperLog.debug(TAG, "actionUrl=" + actionUrl);
        mClassName = "";
        Intent intent = getIntent(context, dataType, actionUrl, actionType, contentId, vod, extraData);
        SuperLog.debug(TAG, "getIntent intent: " + intent);
        try {
            if (intent != null) {
                // ?????????????????????????????????????????????????????????intent???????????????????????????
                String isStartService = intent.getStringExtra(APPUtils.KEY_IS_START_SERVICE);
                SuperLog.debug(TAG, "getIntent KEY_IS_START_SERVICE: " + isStartService);
                if ("true".equals(isStartService)) {
                    SuperLog.debug(TAG, "start service");
                    context.startService(intent);
                } else {
                    SuperLog.debug(TAG, "start activity");
                    if (null != intent.getComponent() && !TextUtils.isEmpty(intent.getComponent().getClassName())){
                        mClassName = intent.getComponent().getClassName().substring(intent.getComponent().getClassName().lastIndexOf(".")+1,
                                intent.getComponent().getClassName().length());
                    }
                    context.startActivity(intent);
                }
            }
        } catch (Exception e) {
            SuperLog.error(TAG, "ZJVRoute start app error ," + e.getLocalizedMessage());
        }
    }

    private static Intent getIntent(Context context, String dataType, String actionUrl, String actionType, String contentId, VOD vod, Map<String, String> extraData) {
        return dataType.equals(LauncherElementDataType.STATIC_ITEM) ? getStaticIntent(context,
                actionUrl, actionType, extraData) : getDynamicIntent(context, dataType, contentId, vod);
    }

    private static Intent getStaticIntent(Context context, String actionUrl, String actionType, Map<String, String> extraData) {
        String playTracker = getContentValue(actionUrl, NewVodDetailActivity.JIUTIAN_TRACKER_URL);
        String itemId = getContentValue(actionUrl, NewVodDetailActivity.JIUTIAN_ITEM_ID);
        String contentType = getContentValue(actionUrl, ActionUrlKeyType.CONTENT_TYPE);
        String subContentType = getContentValue(actionUrl, ActionUrlKeyType.SUB_CONTENT_TYPE);
        String contentId = getContentValue(actionUrl, ActionUrlKeyType.CONTENT_ID);
        String subjectId = getContentValue(actionUrl, ActionUrlKeyType.SUBJECT_ID);
        String focusContentID = getContentValue(actionUrl, ActionUrlKeyType.FOCUS_COTENTID);
        String multiCastId = getContentValue(actionUrl, ActionUrlKeyType.MULTI_CAST_ID);
        if (!TextUtils.isEmpty(actionUrl) && (actionUrl.startsWith("http") || actionUrl.startsWith("https"))) {
            Intent intent = new Intent();
            intent.setClass(context, WebActivity.class);
            intent.putExtra("url", actionUrl);
            return intent;
        } else if (!TextUtils.isEmpty(contentType)){
            if (contentType.equalsIgnoreCase(LauncherElementContentType.INNER_ACTION)) {
                String className = getContentValue(actionUrl, ActionUrlKeyType.CLASS_NAME);
                if (!TextUtils.isEmpty(className)) {
                    Intent intent = new Intent();
                    //??????1.5???1.6??????????????????????????????????????????
                    if(className.contains("MyOrderActivity")&&(packageCode(context)>279)){
                        className="com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewOrderCenterActivity";
                    }
                    intent.setClassName(context.getPackageName(), className);
                    if (null != extraData) {
                        for (String key : extraData.keySet()) {
                            intent.putExtra(key, extraData.get(key));
                        }
                    }
                    return intent;
                }
                return null;
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.VOD_CATEGORY)) {
                return getVodCategoryIntent(context, contentId, focusContentID);
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.APK)) {
                String contentCode = getContentValue(actionUrl, ActionUrlKeyType.CONTENT_CODE);
                String appPkg = getContentValue(actionUrl, ActionUrlKeyType.APP_PKG);
                String className = getContentValue(actionUrl, ActionUrlKeyType.APP_CLASS);
                String version = getContentValue(actionUrl, ActionUrlKeyType.VERSION);
                String apkUrl = getContentValue(actionUrl, ActionUrlKeyType.APK_URL);
                String action = getContentValue(actionUrl, ActionUrlKeyType.ACTION);
                String vodId = getContentValue(actionUrl, ActionUrlKeyType.VODID);
                SuperLog.debug(TAG, "AppPkg:" + appPkg + "; className:" + className);
                if (!TextUtils.isEmpty(appPkg) && appPkg.equalsIgnoreCase(JHXC_PKG)){
                    OTTApplication.getContext().setIsJumpToThirdApp(true);
                }
                Intent intent = APPUtils.startAPP(context, appPkg, className, version, apkUrl,action,vodId,extraData);
                if (null != intent){
                    if (null != extraData) {
                        for (String key : extraData.keySet()) {
                            if (TextUtils.isEmpty(key)){
                                continue;
                            }
                            intent.putExtra(key, extraData.get(key));
                        }
                    }
                    intent.putExtra(ActionUrlKeyType.CONTENT_CODE, contentCode);
                    intent.putExtra(ActionUrlKeyType.ACTION, action);
                    intent.putExtra(ActionUrlKeyType.CONTENT_ID, contentId);
                }
                return intent;
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.FAVORITES)) {
                return getFavoritesIntent(context);
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.HISTORY)) {
                return getHistoryIntent(context);
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.MULTIPLE)) {

            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.CHANNEL)) {
                //?????????????????????????????????
                ChannelDetail channelDetail;
                if (SharedPreferenceUtil.getInstance().getMulticastSwitch()){
                    channelDetail = LiveUtils.findScheduleFromCanPlayById(multiCastId);
                }else{
                    channelDetail = LiveUtils.findScheduleFromCanPlayById(contentId);
                }
                if (null != channelDetail && channelDetail.getPhysicalChannels() != null
                        && channelDetail.getPhysicalChannels().size() > 0) {
                    if(MultViewUtils.getChannelMultType(channelDetail,context))
                    {
                        Intent intent = new Intent(context, LiveTVActivity.class);
                        if (null != channelDetail && channelDetail.getPhysicalChannels() != null
                                && channelDetail.getPhysicalChannels().size() > 0) {
                            if (!TextUtils.isEmpty(getContentValue(actionUrl, ActionUrlKeyType.CHANNEL_MINI_TYPE)) && getContentValue(actionUrl, ActionUrlKeyType.CHANNEL_MINI_TYPE).equals("-1")){
                                //true:??????????????????????????????????????????????????????MiniEpg
                                intent.putExtra(LiveTVActivity.CHANNEL_FINAL, true);
                                //?????????????????????????????????????????????channelID???MediaId
                                LiveDataHolder.get().setChannelAndMediaId(channelDetail.getID(), channelDetail.getPhysicalChannels().get(0).getID());
                                LiveDataHolder.get().setIsShowingSkip(true);
                            }else{
                                //?????????????????????????????????,??????,????????????????????????
                                LiveTVCacheUtil.getInstance().recordPlayChannelInfo(channelDetail.getID(), channelDetail.getPhysicalChannels().get(0).getID());
                            }
                            intent.putExtra(LiveTVActivity.VIDEO_TYPE, LiveTVActivity.VIDEO_TYPE_LIVETV);
                            return intent;
                        }else{
                            EpgToast.showLongToast(context,"?????????????????????");
                            return null;
                        }
                    }
                }else
                {
                    EpgToast.showLongToast(context,"?????????????????????");
                    return null;
                }


            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.STATICVOD)) {
                return getVodDetailIntent(context, contentId, null,playTracker,itemId);
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.STATIC_VOD)) {
                return getVodDetailIntent(context, contentId, null,playTracker,itemId);

            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.STATIC_VOD_SUBJECT)) {

            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.TTVWARCH)) {
                return getTtvWarchIntent(context);
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.TV_GUIDE)) {

            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.TVWARCH)) {
                return getTvWarchIntent(context,contentId,extraData,subjectId);// ??????
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.VIP)) {

            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.TVOD)) {

            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.MULTIINTERACT)) {
                //????????????
                return getAccountBind(context);
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.PAGE)) {
                return getPageIntent(context, contentId);
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.QUERYMYCONTENT)) {
                return getOrderListActivity(context);
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.SEARCH) || contentType.equalsIgnoreCase("Search")) {
                return getSearchItent(context);
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.CATCHUP)) {
                return getCatchUpActivity(context);
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.SETTING)) {
                return new Intent(Settings.ACTION_SETTINGS); //?????????????????????????????????

            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.SWITCH_TO_NORMAL_EPG) &&
                    (TextUtils.isEmpty(subContentType) || !subContentType.equalsIgnoreCase(LauncherElementContentType.SWITCH_EPG))) {
                ExoMediaPlayer.setIsReleaseSurface(false);
                ((MainActivity) OTTApplication.getContext().getMainActivity()).switchLauncher(Constant.DesktopType.NORMAL);
                return null;
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.SWITCH_TO_SIMPLE_EPG) && !SharedPreferenceUtil.getInstance().getIsSimpleEpg()) {
                ExoMediaPlayer.setIsReleaseSurface(true);
                ((MainActivity) OTTApplication.getContext().getMainActivity()).switchLauncher(Constant.DesktopType.SIMPLE);
                return null;
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.SWITCH_TO_CHILD_EPG) && !SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
                ExoMediaPlayer.setIsReleaseSurface(true);
                ((MainActivity) OTTApplication.getContext().getMainActivity()).switchLauncher(Constant.DesktopType.CHILD);
                return null;
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.SWITCH_PROFILE)) {
                SuperLog.info2SD(TAG, "Switch login user. Begin to open CMCC [login] UI.");
                //??????????????????????????????????????????????????????????????????EPGAPK????????????????????????????????????????????????????????????????????????
                OTTApplication.getContext().setNeedShowNetworkExceptionDialog(false);
                //?????????????????????TVOD????????????
                TVODDataUtil.getInstance().setListSubject(null);
                return new Intent("com.chinamobile.middleware.auth.loginui").putExtra("type", "jar");
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.LOCAL_PLAY)) {
                return localPlay();
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.MESSAGE)) {
                return new Intent(context, MessageActivity.class);
            } else if (contentType.equalsIgnoreCase(LauncherElementContentType.BIND_ACCOUNT)) {
                return new Intent(context, AccountManagerActivity.class);
            } else if(contentType.equalsIgnoreCase(LauncherElementContentType.LOCAL_APK)){
                return new Intent(context, AllAppInfoActivity.class);
            } else if(contentType.equalsIgnoreCase(LauncherElementContentType.SWITCH_EPG_FROM_CHILDREN)
                    || contentType.equalsIgnoreCase(LauncherElementContentType.SWITCH_EPG)
                    || subContentType.equalsIgnoreCase(LauncherElementContentType.SWITCH_EPG)){
                ParentSetCenterDialog parentSetCenterDialog = new ParentSetCenterDialog(context, ChildrenConstant.VIEWTYPE.SWITCHEPG);
                parentSetCenterDialog.setTypeForToActivity(contentType);
                parentSetCenterDialog.show();
                return null;
            } else if(contentType.equalsIgnoreCase(LauncherElementContentType.SWITCH_TO_PARENT)){
                //??????????????????
                Intent intent = new Intent();
                intent.setClass(context, ParentSetCenterActivity.class);
                return intent;
            } else if(contentType.equalsIgnoreCase(LauncherElementContentType.NAVIGATE)){
                //????????????????????????????????????
                if (!TextUtils.isEmpty(contentId)){
                    ((MainActivity) OTTApplication.getContext().getMainActivity()).navItemGetFocus(contentId);
                }
                return null;
            }else if(contentType.equalsIgnoreCase(LauncherElementContentType.TYPE_BOOKMARK)){
                //???????????????,??????????????????
                Intent intent = new Intent(context,NewMyMovieActivity.class).putExtra("id", "1");
                return intent;
            }else if(contentType.equalsIgnoreCase(LauncherElementContentType.TYPE_FAVORITE)){
                //???????????????,??????????????????
                Intent intent = new Intent(context,NewMyMovieActivity.class).putExtra("id", "2");
                return intent;
            }
        }
        return null;
    }

    private static Intent localPlay() {
        Bundle data = new Bundle();
        data.putString("action", "videoPlayer");
        data.putString("data", "");
        Intent mIntent = new Intent();
        ComponentName comp = new ComponentName("tv.icntv.vendor",
                "tv.icntv.vendor.Main");
        mIntent.setComponent(comp);
        mIntent.setAction("android.intent.action.VIEW");
        mIntent.putExtras(data);
        return mIntent;
    }

    private static Intent getOrderListActivity(Context context) {
        return new Intent(context, NewOrderCenterActivity.class);

    }
    private static Intent getCatchUpActivity(Context context) {
        return new Intent(context, CatchUpActivity.class);

    }

    private static Intent getSearchItent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    private static Intent getPageIntent(Context context, String contentId) {
        if (TextUtils.isEmpty(contentId)) {
            return null;
        }
        Intent intent = new Intent(context, ChildLauncherActivity.class);
        intent.putExtra(ChildLauncherActivity.KEY_PAGE_ID, contentId);
        return intent;
    }

    private static Intent getDynamicIntent(Context context, String dataType, String contentId, VOD vod) {
        SuperLog.debug(TAG, "contentId: " + contentId);
        if (dataType.equalsIgnoreCase(LauncherElementDataType.VOD)) {
            return getVodDetailIntent(context, contentId, vod,null,null);
        } else if (dataType.equalsIgnoreCase(LauncherElementDataType.VOD_HISTORY)) {
            return getVodDetailIntent(context, contentId, vod,null,null);
        } else if (dataType.equalsIgnoreCase(LauncherElementDataType.CHANNEL_SUBJECT)) {

        }  else if (dataType.equalsIgnoreCase(LauncherElementDataType.VOD_SUBJECT)) {

        } else if (dataType.equalsIgnoreCase(LauncherElementDataType.TVOD_PROGRAM)) {

        } else if (dataType.equalsIgnoreCase(LauncherElementDataType.NEXT_PROGRAM)) {

        } else if (dataType.equalsIgnoreCase(LauncherElementDataType.CURRENT_PROGRAM)) {

        } else if (dataType.equalsIgnoreCase(LauncherElementDataType.MYTV_ITEM)) {

        }
        return null;
    }

    public static String getContentValue(String url, String keyWord) {
        if (TextUtils.isEmpty(url))return "";
        for (String keyValue : url.split("&")) {
            String[] keyValues = keyValue.split("=");
            if (keyValues.length == 2) {
                String key = keyValues[0];
                String value = keyValues[1];
                if (keyWord.equalsIgnoreCase(key)) {
                    return value;
                }
            }
        }
        return "";
    }

    private static Intent getVodDetailIntent(Context context, String contentId, VOD vod,String playTracker,String itemId) {
        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
            if (null != vod){
                return new Intent(context, ChildModeVodDetailActivity.class).putExtra(NewVodDetailActivity.VOD_ID, contentId)
                        .putExtra(NewVodDetailActivity.ORGIN_VOD, vod)
                        .putExtra(NewVodDetailActivity.JIUTIAN_TRACKER_URL,vod.getFeedback().getPlay_tracker())
                        .putExtra(NewVodDetailActivity.JIUTIAN_ITEM_ID, vod.getItemid());
            }else{
                return new Intent(context, ChildModeVodDetailActivity.class).putExtra(NewVodDetailActivity.VOD_ID, contentId)
                        .putExtra(NewVodDetailActivity.ORGIN_VOD, vod)
                        .putExtra(NewVodDetailActivity.JIUTIAN_TRACKER_URL,playTracker)
                        .putExtra(NewVodDetailActivity.JIUTIAN_ITEM_ID,itemId);
            }
        }else{
            if (null != vod){
                return new Intent(context, NewVodDetailActivity.class).putExtra(NewVodDetailActivity.VOD_ID, contentId)
                        .putExtra(NewVodDetailActivity.ORGIN_VOD, vod)
                        .putExtra(NewVodDetailActivity.JIUTIAN_TRACKER_URL,vod.getFeedback().getPlay_tracker())
                        .putExtra(NewVodDetailActivity.JIUTIAN_ITEM_ID, vod.getItemid())
                        .putExtra(NewVodDetailActivity.FROM_MAIN, true);
            }else{
                return new Intent(context, NewVodDetailActivity.class).putExtra(NewVodDetailActivity.VOD_ID, contentId)
                        .putExtra(NewVodDetailActivity.ORGIN_VOD, vod)
                        .putExtra(NewVodDetailActivity.JIUTIAN_TRACKER_URL,playTracker)
                        .putExtra(NewVodDetailActivity.JIUTIAN_ITEM_ID,itemId)
                        .putExtra(NewVodDetailActivity.FROM_MAIN, true);
            }
        }
    }

    private static Intent getVodCategoryIntent(Context context, String categoryId, String focusCategoryId) {
        SuperLog.info("info", "categoryId=" + categoryId);
        SuperLog.info("info", "focusCategoryId=" + focusCategoryId);
        Intent intent = new Intent(context, VodMainActivity.class);
        Topic topic = TopicService.getInstance().getTopicMap().get(categoryId);
        SuperLog.info2SD("getVodCategoryIntent", "Topic is null :" + (null == topic) + ",???????????????:" + categoryId + " ,??????????????????" + TopicService.getInstance().getTopicMap().keySet().toString() + ",");
        if (null != topic) {
            SuperLog.info2SD("getVodCategoryIntent", "????????????categoryId:" + categoryId + ",????????????styleID:" + topic.getTopicStyleId());
        }
        if (topic != null && TopicViewManager.isStyleIdValid(topic.getTopicStyleId())) {
            intent = new Intent(context, TopicActivity.class);
            intent.putExtra(VodMainActivity.CATEGORY_ID, categoryId);
            intent.putExtra(TopicActivity.TOPIC_OBJECT, (Serializable) topic);
            return intent;
        }
        intent.putExtra(VodMainActivity.CATEGORY_ID, categoryId);
        intent.putExtra(VodMainActivity.FOCUS_CATEGORY_ID, focusCategoryId);
        return intent;
    }

    private static Intent getTvWarchIntent(Context context,String contentId,Map<String,String> extraData,String subjectId) {
        String channelSubjects = "";
        if (null != LauncherService.getInstance().getLauncher().getExtraData()) {
            channelSubjects = LauncherService.getInstance().getLauncher().getExtraData().get(LiveTVActivity.KEY_CHANNEL_SUBJECTS);
        }
        String isQuerySubject = extraData.get(LiveTVActivity.KEY_QUERY_SUBJECT);
        String isShowChannelNo = extraData.get(LiveTVActivity.KEY_SHOW_CHANNEL_NO);
        SuperLog.debug(TAG,"query_subject = " + isQuerySubject + ";show_channel_no = " + isShowChannelNo);
        return new Intent(context, LiveTVActivity.class)
                .putExtra(LiveTVActivity.VIDEO_TYPE,LiveTVActivity.VIDEO_TYPE_LIVETV)
                .putExtra(LiveTVActivity.KEY_CHANNEL_SUBJECTS, channelSubjects)
                .putExtra(LiveTVActivity.KEY_SUBJECT_ID, subjectId)
                .putExtra(LiveTVActivity.KEY_QUERY_SUBJECT, isQuerySubject)
                .putExtra(LiveTVActivity.KEY_SHOW_CHANNEL_NO, isShowChannelNo);
    }

    private static Intent getTtvWarchIntent(Context context) {
        return new Intent(context, CatchUpActivity.class);
    }

    /**
     * ??????
     **/
    private static Intent getFavoritesIntent(Context context) {
        return new Intent(context, NewMyMovieActivity.class).putExtra("id", "2");
    }

    /**
     * ??????
     */
    private static Intent getHistoryIntent(Context context) {
        return new Intent(context, NewMyMovieActivity.class).putExtra("id", "1");
    }

    /***
     * ????????????
     * */
    private static Intent getAccountBind(Context context) {
        return new Intent(context, AccountManagerActivity.class);
    }

    public static int packageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            SuperLog.error(TAG,e);
        }
        return code;
    }
}