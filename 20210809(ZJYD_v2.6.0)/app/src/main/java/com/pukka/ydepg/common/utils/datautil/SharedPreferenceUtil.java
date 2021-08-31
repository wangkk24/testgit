package com.pukka.ydepg.common.utils.datautil;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.huawei.ott.sdk.encrypt.MsaSecurityStorage;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.UtilBase;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.moudule.player.node.Schedule;
import com.pukka.ydepg.service.HeartBeatService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * 用于存储新增的profile
 * SharedPreference 加密现只对string做了 可以调用通用的getStringData 和 setStringData  若要单独写 参考下面的例子
 *
 * @author
 */
public final class SharedPreferenceUtil {

    private static final String TAG = SharedPreferenceUtil.class.getSimpleName();
    private static SharedPreferenceUtil mSharedPreferenceUtil;

    private String sessionID = "";

    //本地内置launcher的版本号
    private static final String LOCAL_LAUNCHER_VERSION = "0";

    //SharedPreference存储文件名称
    private static final String PREFERENCE_FILE_NAME = "OTT_SharedPreference";

    //静态图片路径保存
    public static final String PHM_LAUNCHER_LINK = "phm_launcher_link";

    private SharedPreferenceUtil() {}

    public static synchronized SharedPreferenceUtil getInstance() {
        if (null == mSharedPreferenceUtil) {
            mSharedPreferenceUtil = new SharedPreferenceUtil();
        }
        return mSharedPreferenceUtil;
    }

    public boolean isPause() {
        return getInstance().sharedPreferences.getBoolean("ISPAUSE", true);
    }

    //存储用户声音
    private final SharedPreferences sharedPreferences = UtilBase.getApplicationContext().getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);

    public static void remove(String key) {
        SharedPreferences.Editor editor = getInstance().sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    //由于影响性能，因此该方法去掉解密，为兼容老用户，因此key+new
    public static String getStringData(String key, String defaultValue) {
        //String value = MsaSecurityStorage.read(key);
        String value = getInstance().sharedPreferences.getString(key+"new", null);
        return TextUtils.isEmpty(value) ? defaultValue : value;
    }

    //由于影响性能，因此该方法去掉加密，为兼容老用户，因此key+new
    public void putString(String key, String value) {
        //MsaSecurityStorage.write(key,value);
        sharedPreferences.edit().putString(key+"new", value).apply();
    }

    public static boolean getBoolData(String key, boolean defaultValue) {
        return getInstance().sharedPreferences.getBoolean(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public void saveNotification(String notification) {
        StringBuffer sb = new StringBuffer();
        String notifications = getNotifitions();
        if (TextUtils.isEmpty(notifications)) {
            sb.append(notification);
        } else {
            sb.append(notifications);
            sb.append(";");
            sb.append(notification);
        }
        String notificationString = sb.toString();

        //最多只能存三十条
        String[] split = notificationString.split(";");
        if (split.length > 30) {
            StringBuffer sbSencond = new StringBuffer();
            List<String> messages = new ArrayList<>();
            messages.addAll(Arrays.asList(split));
            messages.remove(0);
            Observable.fromIterable(messages).subscribe(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    if (sbSencond.length() > 0) {
                        sbSencond.append(";").append(s);
                    } else {
                        sbSencond.append(s);
                    }
                }
            });
            notificationString = sbSencond.toString();
        }
        putString(Key.XMPP_MESSAGE + "", notificationString);
    }

    /**
     * get current app language
     * the language is set by user
     *
     * @return language string
     */
    public String getCurrentLanguage() {
        return getStringData(Key.APP_CURRENT_LANGUAGE + "", "zh");
    }

    public void setAllChannel(List<Schedule> scheduleList) {
        String string = JsonParse.listToJsonString(scheduleList);
        putString(Key.QUERYALLCHANNEL + "", string);
    }

    public List<Schedule> getAllChannel() {
        String data = getStringData(Key.QUERYALLCHANNEL + "", "");
        return JsonParse.jsonToClassList(data, Schedule.class);
    }

    public void setSaveAllChannel(List<ChannelDetail> channelDetailList) {
        String string = JsonParse.listToJsonString(channelDetailList);
        putString(Key.SAVEALLCHANNEL + "", string);
    }

    public List<ChannelDetail> geSavetAllChannel() {
        String data = getStringData(Key.SAVEALLCHANNEL + "", "");
        return JsonParse.jsonToClassList(data, ChannelDetail.class);
    }

    public void setSaveAllChannelMap(Map<String, ChannelDetail> channelDetailList) {
        String string = JsonParse.object2String(channelDetailList);
        putString(Key.SAVEALLCHANNELMAP + "", string);
    }

    public Map<String, ChannelDetail> geSavetAllChannelMap() {
        String data = getStringData(Key.SAVEALLCHANNELMAP + "", "");
        return JsonParse.json2Object(data, new TypeToken<Map<String, ChannelDetail>>() {}.getType());
    }

    public void setChannelPlay(List<ChannelDetail> channelDetailList) {
        String string = "";
        if (channelDetailList != null && channelDetailList.size() > 0){
            string = JsonParse.listToJsonString(channelDetailList);
        }
        putString(Key.SAVECHANNELPLAY + "", string);
    }

    public List<ChannelDetail> getChannelPlay() {
        String data = getStringData(Key.SAVECHANNELPLAY + "", "");
        return JsonParse.jsonToClassList(data, ChannelDetail.class);
    }

    /**
     * 本地缓存收藏，书签，频道，锁,profile,order:版本号
     */
    public void updateLocalVersion(Key key, String version) {
        getInstance().putString(key + "", version);
    }

    public void saveBindedSubscribers(List<String> subscribers) {
        StringBuilder sb = new StringBuilder();
        for (String subscriber : subscribers) {
            sb.append(subscriber).append(';');
        }
        if (!sb.toString().isEmpty()) {
            getInstance().putString(Key.SHARE_BINDED_INFO + "", sb.substring(0, sb.length() - 1));
        } else {
            getInstance().putString(Key.SHARE_BINDED_INFO + "", "");
        }
    }

    public String getBindedSubscribers() {
        return getStringData(Key.SHARE_BINDED_INFO + "", "");
    }

    //华为安全需要,不允许保存sessionID,因此更换为内存变量
    public String getSeesionId() {
        return sessionID;
        //return getStringData(Key.SESSION_ID + "", "");
    }

    //华为安全要求不允许明文存储sessionID 改为内存存储
    public void saveSessionId(String sessionID) {
        SuperLog.info2SD(TAG, "SessionID = ***************"/* + sessionID*/);
        this.sessionID = sessionID;
        //putString(Key.SESSION_ID + "", sessionID);
    }

    public boolean isCirclePlay(){
        return getInstance().getBoolData(Key.IS_CIRCLE_PLAY+"",false);
    }

    public void setCirclePlay(boolean circlePlay){
        putBoolean(Key.IS_CIRCLE_PLAY+"",circlePlay);
    }

    public String getNotifitions() {
        return getStringData(Key.XMPP_MESSAGE + "", "");
    }

    public List<String> getNotifitionList() {
        List<String> messages = new ArrayList<>();
        String notifitions = getNotifitions();
        if (!TextUtils.isEmpty(notifitions)) {
            String[] split = notifitions.split(";");
            messages.addAll(Arrays.asList(split));
        }
        return messages;

    }

    public String getLauncherVersion() {
        return getStringData(Key.LAUNCHER_VESION + "", LOCAL_LAUNCHER_VERSION);
    }

    public void saveLauncherVersion(String launcherVersion) {
        putString(Key.LAUNCHER_VESION + "", launcherVersion);
    }

    public String getLauncherVersionForChild() {
        return getStringData(Key.LAUNCHER_VESION_FOR_CHILDLAUNCHER + "", LOCAL_LAUNCHER_VERSION);
    }

    public void saveLauncherVersionForChild(String launcherVersion) {
        putString(Key.LAUNCHER_VESION_FOR_CHILDLAUNCHER + "", launcherVersion);
    }

    public String getLauncherDeskTopIdForChild() {
        return getStringData(Key.DESKTOP_ID + "", LOCAL_LAUNCHER_VERSION);
    }

    public void saveLauncherDeskTopIdForChild(String deskTopId) {
        putString(Key.DESKTOP_ID + "", deskTopId);
    }

    public void saveLauncherLink(String launcherLink) {
        putString(Key.LAUNCHER_LINK + "", launcherLink);
    }

    public String getLauncherLink() {
        return getStringData(Key.LAUNCHER_LINK + "", PHM_LAUNCHER_LINK);
    }

    public void saveLauncherNewLink(String launcherLink) {
        putString(Key.LAUNCHER_NEW_LINK + "", launcherLink);
    }

    public String getLauncherNewLink() {
        return getStringData(Key.LAUNCHER_NEW_LINK + "", PHM_LAUNCHER_LINK);
    }

    public void saveLauncherLinkJson(String launcherLink) {
        putString(Key.LAUNCHER_LINK_JSON + "", launcherLink);
    }

    public String getLauncherLinkJson() {
        return getStringData(Key.LAUNCHER_LINK_JSON + "", "");
    }

    public void saveChildLauncherLink(Map<String,String> map) {
        String hashMapString = JsonParse.object2String(map);
        putString(Key.CHILDLAUNCHER_LINK_JSON + "", hashMapString);
    }

    public Map<String,String> getChildLauncherLink() {
        String data = getStringData(Key.CHILDLAUNCHER_LINK_JSON + "", "");
        return JsonParse.json2Object(data, new TypeToken<Map<String, String>>() {}.getType());
    }

    public void saveLauncherUpdate(boolean launcherUpdate) {
        putBoolean(Key.LAUNCHER_UPDATE + "", launcherUpdate);
        if (launcherUpdate){
            saveChildLauncherUpdate(launcherUpdate);
        }
    }

    public boolean getLauncherUpdate() {
        return getBoolData(Key.LAUNCHER_UPDATE + "", false);
    }

    public void saveChildLauncherUpdate(boolean launcherUpdate) {
        putBoolean(Key.CHILD_LAUNCHER_UPDATE + "", launcherUpdate);
    }

    public boolean getChildLauncherUpdate() {
        return getBoolData(Key.CHILD_LAUNCHER_UPDATE + "", false);
    }

    public void saveTopicData(String topicData) {
        putString(Key.TOPIC_DATA + "", topicData);
    }

    public String getTopicData() {
        return getStringData(Key.TOPIC_DATA + "", "");
    }

    public void saveStartResource(String resourceName) {
        putString(Key.START_RESOURCE + "", resourceName);
    }

    public String getStartResource() {
        return getStringData(Key.START_RESOURCE + "", "");
    }

    public void savePreStartResource(String resourceName) {
        putString(Key.PRE_START_RESOURCE + "", resourceName);
    }

    public String getPreStartResource() {
        return getStringData(Key.PRE_START_RESOURCE + "", "");
    }

    public void saveUnZipStartResource(boolean isUnZip) {
        putBoolean(Key.UNZIP_START_RESOURCE + "", isUnZip);
    }

    public boolean getUnZipStartResource() {
        return getBoolData(Key.UNZIP_START_RESOURCE + "", false);
    }

    public void setSaveHashMap(HashMap<String, List<ChannelDetail>> hashMap) {
        String hashMapString = "";
        if (null != hashMap && hashMap.size() > 0) {
            hashMapString = JsonParse.object2String(hashMap);
        }
        putString(Key.SAVELIVEHASHMAP + "", hashMapString);
    }

    public HashMap<String, List<ChannelDetail>> getSaveHashMap() {
        String data = getStringData(Key.SAVELIVEHASHMAP + "", "");
        HashMap<String, List<ChannelDetail>> testHashMap2 = JsonParse.json2Object(data,new TypeToken<HashMap<String, List<ChannelDetail>>>(){}.getType());
        return testHashMap2;
    }

    public void setSaveSubject(List<Subject> mSubjectList) {
        String string = "";
        if (null != mSubjectList && mSubjectList.size() > 0){
            string = JsonParse.listToJsonString(mSubjectList);
        }
        putString(Key.SAVESUBJECTLIST + "", string);
    }

    public List<Subject> getSaveSubject() {
        String data = getStringData(Key.SAVESUBJECTLIST + "", "");
        return JsonParse.jsonToClassList(data, Subject.class);
    }

    public int getSelectChannel() {
        String id = getStringData(Key.SAVESELECTCHANNELID + "", "0");
        return Integer.parseInt(id);
    }

    public void saveEpgGuideVersion(String version) {
        putString(Key.EPG_GUIDE_VERSION + "", version);
    }

    public String getEpgGuideVersion() {
        return getStringData(Key.EPG_GUIDE_VERSION + "", "");
    }

    public void saveXmppMessageH5(List<String> messageList) {
        if(!CollectionUtil.isEmpty(messageList)){
            String body = JsonParse.object2String(messageList);
            putString(Key.XMPP_MESSAGE_H5 + "", body);
        }else{
            putString(Key.XMPP_MESSAGE_H5 + "", "");
        }
    }

    public List<String> getXmppMessageH5() {
        String body = getStringData(Key.XMPP_MESSAGE_H5 + "", "");
        if (TextUtils.isEmpty(body)){
            return null;
        }else{
            return JsonParse.json2Object(body,new TypeToken<List<String>>(){}.getType());
        }
    }

    /*
     * 缓存选择的频道position
     * */
    public void setSelectChannel(int channel) {
        putString(Key.SAVESELECTCHANNELID + "", String.valueOf(channel));
    }

    public int getSelectCulomn() {
        String id = getStringData(Key.SAVESELECTCULOMNID + "", "0");
        return Integer.parseInt(id);
    }

    /*
     * 缓存选择的栏目position
     * */
    public void setSelectCulomn(int culomn) {
        putString(Key.SAVESELECTCULOMNID + "", String.valueOf(culomn));
    }

    public void setRefreshLauncherTime(String time){
        putString(Key.REFRESH_LAUNCHER_TIME+"",time);
    }
    public String getRefreshLauncherTime(){
        return getStringData(Key.REFRESH_LAUNCHER_TIME+"","");
    }

    public void setParentCenterData(HashMap<String, String> hashMap) {
        String hashMapString = JsonParse.object2String(hashMap);
        putString(Key.SAVE_PARENT_CENTER_DATA + "", hashMapString);
    }

    public HashMap<String, String> getParentCenterData() {
        String data = getStringData(Key.SAVE_PARENT_CENTER_DATA + "", "");
        HashMap<String, String> hashMap = JsonParse.json2Object(data,new TypeToken<HashMap<String, String>>(){}.getType());
        return hashMap;
    }

    public String getParentCenterDataStr() {
        return getStringData(Key.SAVE_PARENT_CENTER_DATA + "", "");
    }

    /*
     * 缓存重启或覆盖安装的用户
     * */
    public void setUserId() {
        putString(Key.SAVEUSERID + "", SessionService.getInstance().getSession().getUserId());
    }

    public boolean getIsSimpleEpg() {
        return getBoolData(Key.IS_SIMPLEEPG + "", false);
    }

    public boolean getIsChildrenEpg() {
        return getBoolData(Key.IS_CHILDRENEPG + "", false);
    }

    public String getUserId() {
        return getStringData(Key.SAVEUSERID + "", "");
    }

    public void setIsSimpleEpg(boolean isSimpleEpg) {
        putBoolean(Key.IS_SIMPLEEPG + "", isSimpleEpg);
    }
    public void setIsChildrenEpg(boolean isSimpleEpg) {
        putBoolean(Key.IS_CHILDRENEPG + "", isSimpleEpg);
    }

    public void setCurrentAllTime(long alltime){
        putString(Key.CURRENT_ALL_TIME+"",String.valueOf(alltime));
    }

    public String  getLockScreenPoint(){
        return getStringData(Key.LOCK_SCREEN_POINT+"","");
    }

    public void setLockScreenPoint(String time){
        putString(Key.LOCK_SCREEN_POINT+"",time);
    }

    public String  getlockScreenType(){
        return getStringData(Key.IS_LOCK_SCREEN+"","");
    }

    public void setLockScreenType(String  type){
        putString(Key.IS_LOCK_SCREEN+"",type);
    }

    public String getCurrentAllTime(){
        return getStringData(Key.CURRENT_ALL_TIME+"","0");
    }

    public void setCurrentSingleTime(long singleTime){
        putString(Key.CURRENT_SINGLE_TIME+"",String.valueOf(singleTime));
    }

    public String getYesterdayTime(){
        return getStringData(Key.YESTERDAYTIME_TIME+"","");
    }

    public void setYesterdayTime(String timeStr){
        putString(Key.YESTERDAYTIME_TIME+"",timeStr);
    }

    public String getCurrentSingleTime(){
     return   getStringData(Key.CURRENT_SINGLE_TIME+"","0");
    }

    public void setMulticastSwitch(boolean multicastSwitch){
        LiveDataHolder.get().setIsChangeMulticastSwitch(true);
        putBoolean(Key.MULTICAST_SWITCH + "", multicastSwitch);
    }
    public boolean getMulticastSwitch(){
        return getBoolData(Key.MULTICAST_SWITCH + "", true);
    }

    public void clearCache() {
        SuperLog.info2SD(TAG, "clear cache info: /data/data/com.pukka.ydepg/shared_prefs/OTT_SharedPreference.xml");
        SharedPreferences.Editor editor = getInstance().sharedPreferences.edit();
        editor.remove(Key.QUERYALLCHANNEL +"new");
        editor.remove(Key.SAVESUBJECTLIST + "new");
        //切换，可能请求频道列表失败，导致直播黑屏
        //editor.remove(Key.SAVEALLCHANNEL  + "new");
        editor.remove(Key.FAVORITE_VERSION  + "new");
        editor.remove(Key.HISTORY_VERSION   + "new");
        editor.remove(Key.SHARE_BINDED_INFO + "new");

        editor.remove(Key.SESSION_ID + "new");
        editor.remove(Key.LAUNCHER_LINK + "new");
        editor.remove(Key.TOPIC_DATA + "new");
        editor.remove(Key.SAVECHANNELPLAY + "new");
        editor.remove(Key.SAVESELECTCULOMNID + "new");
        editor.remove(Key.START_PICTURE_NAME + "new");
        editor.remove(Key.SAVELIVEHASHMAP + "new");
        editor.remove(Key.IS_SIMPLEEPG + "new");
        editor.remove(LiveTVCacheUtil.CACHE_COLUMN_KEY + "new");
        editor.remove("channelnew");
        editor.remove(HeartBeatService.VersionType.VERSION_BOOKMARK  + "new");
        editor.remove(HeartBeatService.VersionType.VERSION_SUBSCRIBE + "new");
        editor.remove(HeartBeatService.VersionType.VERSION_CHANNEL   + "new");
        editor.remove(HeartBeatService.VersionType.VERSION_FAVORITE  + "new");
        editor.remove(LiveTVCacheUtil.CACHE_COLUMN_KEY);
        editor.remove(LiveTVCacheUtil.RECORD_CHANNELINFO);
        editor.remove(Key.SAVEUSERID+"new");
        editor.remove(Key.REFRESH_LAUNCHER_TIME+"new");
        editor.remove(Key.SAVE_PARENT_CENTER_DATA+"new");
        editor.remove(Key.ADVERT_URL+"new"); //弃用key
        editor.remove(SessionService.getInstance().getSession().getUserId() + ""); //弃用key
        editor.remove(SessionService.getInstance().getSession().getUserId() + "message"); //弃用key
        editor.remove(Key.XMPP_MESSAGE + "new");
        editor.remove(Key.CHILDLAUNCHER_LINK_JSON + "new");
        editor.remove(Key.EPG_GUIDE_VERSION + "new");
        editor.apply();


        MsaSecurityStorage.delete(Key.QUERYALLCHANNEL + "");
        MsaSecurityStorage.delete(Key.SAVESUBJECTLIST + "");
        //切换，可能请求频道列表失败，导致直播黑屏
        //MsaSecurityStorage.delete(Key.SAVEALLCHANNEL + "");
        MsaSecurityStorage.delete(Key.FAVORITE_VERSION + "");
        MsaSecurityStorage.delete(Key.HISTORY_VERSION + "");
        MsaSecurityStorage.delete(Key.SHARE_BINDED_INFO + "");
        MsaSecurityStorage.delete(Key.LAUNCHER_VESION + "");
        MsaSecurityStorage.delete(Key.LAUNCHER_VESION_FOR_CHILDLAUNCHER + "");
        MsaSecurityStorage.delete(Key.DESKTOP_ID + "");
        MsaSecurityStorage.delete(Key.SESSION_ID + "");
        MsaSecurityStorage.delete(Key.LAUNCHER_LINK + "");
        MsaSecurityStorage.delete(Key.TOPIC_DATA + "");
        MsaSecurityStorage.delete(Key.SAVECHANNELPLAY + "");
        MsaSecurityStorage.delete(Key.SAVESELECTCULOMNID + "");
        MsaSecurityStorage.delete(Key.START_PICTURE_NAME + "");
        MsaSecurityStorage.delete(Key.SAVELIVEHASHMAP + "");
        MsaSecurityStorage.delete(Key.IS_SIMPLEEPG + "");
        MsaSecurityStorage.delete(LiveTVCacheUtil.CACHE_COLUMN_KEY + "");
        MsaSecurityStorage.delete("channel");
        MsaSecurityStorage.delete(HeartBeatService.VersionType.VERSION_BOOKMARK);
        MsaSecurityStorage.delete(HeartBeatService.VersionType.VERSION_SUBSCRIBE);
        MsaSecurityStorage.delete(HeartBeatService.VersionType.VERSION_CHANNEL);
        MsaSecurityStorage.delete(HeartBeatService.VersionType.VERSION_FAVORITE);
        MsaSecurityStorage.delete(LiveTVCacheUtil.CACHE_COLUMN_KEY);
        MsaSecurityStorage.delete(LiveTVCacheUtil.RECORD_CHANNELINFO);
        MsaSecurityStorage.delete(Key.SAVEUSERID+"");
        MsaSecurityStorage.delete(Key.REFRESH_LAUNCHER_TIME+"");
        MsaSecurityStorage.delete(Key.SAVE_PARENT_CENTER_DATA+"");
        MsaSecurityStorage.delete(Key.ADVERT_URL+"");//弃用key
        MsaSecurityStorage.delete(SessionService.getInstance().getSession().getUserId() + "");//弃用key
        MsaSecurityStorage.delete(SessionService.getInstance().getSession().getUserId() + "message");//弃用key
        MsaSecurityStorage.delete(Key.XMPP_MESSAGE + "");//切换账号清除推送消息
        MsaSecurityStorage.delete(Key.CHILDLAUNCHER_LINK_JSON + "");//二级页面json的下载地址
        MsaSecurityStorage.delete(Key.EPG_GUIDE_VERSION + "");
    }

    public enum Key {
        APP_CURRENT_LANGUAGE,          //the current app language
        FIRST_TIME_USE_SEARCH,         //user first time use search
        FIRST_TIME_USE_COLLECTANDHISTORY,//user first time use search

        FAVORITE_VERSION,              //收藏版本号
        HISTORY_VERSION,               //书签版本号

        SHARE_BINDED_INFO,             //账号绑定信息
        LAUNCHER_VESION,               //版本号
        LAUNCHER_VESION_FOR_CHILDLAUNCHER,               //版本号为拼接二级桌面地址，（推送消息仅配二级桌面的桌面id）
        DESKTOP_ID,               //桌面id为拼接二级桌面地址，（推送消息仅配二级桌面的桌面id）
        SESSION_ID,
        LAUNCHER_LINK,
        LAUNCHER_NEW_LINK,
        LAUNCHER_UPDATE,
        CHILD_LAUNCHER_UPDATE,

        TOPIC_DATA,
        START_PICTURE,
        START_RESOURCE,
        UNZIP_START_RESOURCE,
        PRE_START_RESOURCE,
        START_PICTURE_NAME,

        QUERYALLCHANNEL,
        SAVEALLCHANNEL,                //所有频道
        SAVEALLCHANNELMAP,                //所有频道
        SAVECHANNELPLAY,               //所有可播放频道
        SAVELIVEHASHMAP,               //直播频道、栏目信息的HashMAP
        SAVESUBJECTLIST,
        SAVESELECTCHANNELID,           //选中的直播频道
        SAVESELECTCULOMNID,            //选中的直播栏目
        SAVEUSERID,
        IS_SIMPLEEPG,

        REFRESH_LAUNCHER_TIME,         //首页launcher刷新时间
        IS_CHILDRENEPG,                //儿童版桌面
        SAVE_PARENT_CENTER_DATA,       //家长中心的data

        IS_CIRCLE_PLAY,                //详情页是否循环播放
        IS_EPSIODE_REST,               //详情页是否看完本集休息
        CURRENT_SINGLE_TIME,           //当前已用单次时长
        CURRENT_ALL_TIME,              //当前已用总时长
        LOCK_SCREEN_POINT,             //锁屏时间点
        IS_LOCK_SCREEN,                //是否需要锁屏
        YESTERDAYTIME_TIME,            //今天日期
        ADVERT_URL,                     //广告url

        XMPP_MESSAGE,                   //xmpp 推送消息
        ONDEMAND_SKIP,                  //跳过片头片尾
        ONDEMAND_SPEED,                 //倍速播放
        MULTICAST_SWITCH,                //组播开关
        ONDEMAND_SPEED_LIST,               //倍速播放速度列表
        SAVESELECTSUBJECTLIST,             //指定的直播栏目
        LAUNCHER_LINK_JSON,             //PHS json下载地址
        EPG_GUIDE_VERSION,              //Guide Version节点指引
        XMPP_MESSAGE_H5,              //推送消息H5类型
        CHILDLAUNCHER_LINK_JSON,             //PHS 二级页面的json下载地址
    }
}