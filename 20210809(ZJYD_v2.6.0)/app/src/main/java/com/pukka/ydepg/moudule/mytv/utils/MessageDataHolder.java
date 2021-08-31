package com.pukka.ydepg.moudule.mytv.utils;

import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.bean.node.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eason on 2018/5/30.
 * 多个消息同时推送过来
 * 暂存
 * 挨个弹框显示
 */
public class MessageDataHolder {

    /*
    * 首页手动点击刷新按钮
    * 如果下载了新的launcher
    * 是否立即刷新数据
    * true:刷新
    * false:不刷新
    * */
    private boolean isRefreshLauncherData = false;

    private static volatile MessageDataHolder mInstance = null;

    private List<String> mMessageList = new ArrayList<>();

    private List<String> mMessageH5List = new ArrayList<>();

    //我的播放历史和收藏海报是否显示光效效果
    private boolean isShimmerHistory = false;
    private boolean isShimmerCollect = false;

    //首页点播自动播放是否显示光效效果
    private boolean isShimmerMix = true;

    //开机海报是否隐藏
    private boolean isComplete = false;

    //是否是视频资源位的海报展示点击事件
    private boolean isMixVideoPostImage = false;

    //点击菜单键弹框是焦点正在首页视频播放资源位
    private boolean isFocusVideo = false;

    private String sceneId = "201";

    public void setScenId(String sceneId){
        this.sceneId = sceneId;
        SuperLog.debug("sceneId",sceneId);
    }

    public String getSceneId(){
        SuperLog.debug("sceneId",sceneId);
        return this.sceneId;
    }

    //本地已安装的APK信息
    private List<AppInfo> appInfoList;

    private MessageDataHolder(){}

    public static MessageDataHolder get() {
        if (mInstance == null) {
            synchronized (MessageDataHolder.class) {
                if (mInstance == null) {
                    mInstance = new MessageDataHolder();
                }
            }
        }
        return mInstance;
    }

    public synchronized List<AppInfo> getAppInfoList() {
        return appInfoList == null ? new ArrayList<>() : appInfoList;
    }

    public synchronized void setAppInfoList(List<AppInfo> appInfoList) {
        if (null != appInfoList && appInfoList.size() > 0){
            this.appInfoList = appInfoList;
        }
    }

    //第三方应用点击更新，更换版本号，去掉更新角标
    public synchronized void setAppInfoVersion(String pkgName,int version) {
        for (AppInfo appInfo : appInfoList){
            if (appInfo.getPackageName().equalsIgnoreCase(pkgName)){
                appInfo.setVersion(version);
                break;
            }
        }
    }

    /*
    * 同时多个推送消息
    * 按顺序一个一个取
    * */
    public synchronized String getMessageList() {

        String message = "";
        if (mMessageList != null && mMessageList.size() > 0) {
            message = mMessageList.get(0);
            mMessageList.remove(0);
            return message;
        }
        return message;
    }

    public synchronized void setMessageList(String messageList) {
        if (mMessageList != null && mMessageList.size() > 0) {
            mMessageList.add(0,messageList);
        } else {
            mMessageList = new ArrayList<>();
            mMessageList.add(messageList);
        }
    }

    public synchronized void setMessageMobileAndEpg(String messageList) {
        if (mMessageList != null && mMessageList.size() > 0) {
            mMessageList.add(messageList);
        } else {
            mMessageList = new ArrayList<>();
            mMessageList.add(messageList);
        }
    }

    public synchronized List<String> getMessageAllList() {
        return mMessageList;
    }
    public synchronized void setMessageData(List<String> list) {
        mMessageList = list;
    }
    public synchronized List<String> getMessageH5AllList() {
        if (CollectionUtil.isEmpty(mMessageH5List)){
            List<String> xmppMessageH5 = SharedPreferenceUtil.getInstance().getXmppMessageH5();
            if (!CollectionUtil.isEmpty(xmppMessageH5)){
                mMessageH5List = xmppMessageH5;
            }
        }
        return mMessageH5List;
    }
    public synchronized void setMessageH5Data(List<String> list) {
        mMessageH5List = list;
    }

    public synchronized void setMessageH5List(String messageList) {
        if (mMessageH5List != null && mMessageH5List.size() > 0) {
            mMessageH5List.add(0,messageList);
        } else {
            mMessageH5List = new ArrayList<>();
            mMessageH5List.add(messageList);
        }
    }

    public synchronized String getMessageH5List() {
        if (CollectionUtil.isEmpty(mMessageH5List)){
            List<String> xmppMessageH5 = SharedPreferenceUtil.getInstance().getXmppMessageH5();
            if (!CollectionUtil.isEmpty(xmppMessageH5)){
                mMessageH5List = xmppMessageH5;
            }
        }
        String message = "";
        if (mMessageH5List != null && mMessageH5List.size() > 0) {
            message = mMessageH5List.get(0);
            //SharedPreferenceUtil.getInstance().saveXmppMessageH5(mMessageH5List);
            //mMessageH5List = new ArrayList<>();
            return message;
        }
        return message;
    }


    /*
    * 设置海报是否加载完成
    * */
    public void setIsComplete(boolean b){
        isComplete = b;
    }

    public boolean getIsComplete(){
        return isComplete;
    }

    public void setIsMixVideoPostImage(boolean b){
        isMixVideoPostImage = b;
    }

    public boolean getIsMixVideoPostImage(){
        return isMixVideoPostImage;
    }

    public void setRefreshLauncherData(boolean b){
        this.isRefreshLauncherData = b;
    }

    public boolean getRefreshLauncherData(){
        return this.isRefreshLauncherData;
    }

    public void setIsShimmerHistory(boolean b){
        isShimmerHistory = b;
    }
    public boolean getIsShimmerHistory(){
        return isShimmerHistory;
    }
    public void setIsShimmerCollect(boolean b){
        isShimmerCollect = b;
    }
    public boolean getIsShimmerCollect(){
        return isShimmerCollect;
    }

    public void setIsShimmerMix(boolean b){
        isShimmerMix = b;
    }
    public boolean getIsShimmerMix(){
        return isShimmerMix;
    }

    public void setIsFocusVideo(boolean b){
        isFocusVideo = b;
    }
    public boolean getIsFocusVideo(){
        return isFocusVideo;
    }
}
