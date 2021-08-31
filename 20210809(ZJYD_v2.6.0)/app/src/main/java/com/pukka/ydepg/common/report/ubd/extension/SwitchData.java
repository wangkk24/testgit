package com.pukka.ydepg.common.report.ubd.extension;

import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

public class SwitchData implements Cloneable{

    //[序号01]当前页面导航栏id（根据场景可以是PHM的navID，栏目id，和固定枚举值，固定枚举值客户单会根据场景提供）
    private String fromNavID;

    //[序号02]当前父对象的位置序号(1..N)
    private String navIndex;

    //[序号03]页面id
    private String pageID;

    //[序号04]控件位置信息
    private String groupIndex;

    //[序号05]模板ID
    private String groupID;

    //[序号06]模板样式ID
    private String controlID;

    //[序号07]子位ID信息
    private String elementID;

    //[序号08]当前对象的位置序号(1..N)
    private String elementIndex;

    //[序号09]0-视频、1-图片、2-Widget、3-WebView、4-文本
    private String elementType;

    //[序号10]contentID
    private String contentID;

    //[序号11]连续剧子集ID
    private String episodeID;

    //[序号12]内容的名称
    private String contentName;

    //[序号13]内容的类型
    private String contentType;

    //[序号14]推荐类型  CVI_TYPE="0"智能推荐，HAND_TYPE="1"手动配置
    private String recommendType;

    private String appointedId;

    //轮播点击事件上报的序号
    private String sequence;

    //[序号15]
    private String sceneId;

    //[序号16]行为ID
    private String actionID;

    //[序号17]主要每个局点针对不同人群设置有不同的EPG模板页面
    private String version;

    //[序号18]TV用户的标识
    private String userID;

    //[序号19]系统时间
    private String systemTime;

    //[序号20]扩展字段,0:普通话单 1特殊话单(引流话单) 2表示订购成功 3表示订购失败
    private String extraOne = UBDConstant.BillType.NORMAL;

    //[序号21]扩展字段,保存专题样式
    private String extraTwo;

    public void clear(){
        actionID     = null;
        fromNavID    = null;
        navIndex     = null;
        pageID       = null;
        groupID      = null;
        groupIndex   = null;
        controlID    = null;
        elementID    = null;
        elementIndex = null;
        elementType  = null;
        contentID    = null;
        contentName  = null;
        contentType  = null;
        recommendType= null;
        appointedId  = null;
        sequence  = null;
        sceneId      = null;
        version      = null;
        userID       = null;
        systemTime   = null;
        extraOne     = UBDConstant.BillType.NORMAL;
        extraTwo     = null;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder()
                .append("\n\tfromNavID     = ").append(fromNavID)
                .append("\n\tnavIndex      = ").append(navIndex)
                .append("\n\tpageID        = ").append(pageID)
                .append("\n\tgroupID       = ").append(groupID)
                .append("\n\tgroupIndex    = ").append(groupIndex)
                .append("\n\tcontrolID     = ").append(controlID)
                .append("\n\telementID     = ").append(elementID)
                .append("\n\telementIndex  = ").append(elementIndex)
                .append("\n\telementType   = ").append(elementType)
                .append("\n\tcontentID     = ").append(contentID)
                .append("\n\tepisodeID     = ").append(episodeID)
                .append("\n\tcontentName   = ").append(contentName)
                .append("\n\tcontentType   = ").append(contentType)
                .append("\n\trecommendType = ").append(recommendType)
                .append("\n\tappPointedID  = ").append(appointedId)
                .append("\n\tappPointedID  = ").append(sequence)
                .append("\n\tsceneId       = ").append(sceneId)
                .append("\n\tactionID      = ").append(actionID)
                .append("\n\tversion       = ").append(version)
                .append("\n\tuserID        = ").append(userID)
                .append("\n\tsystemTime    = ").append(systemTime)
                .append("\n\textraOne      = ").append(extraOne)
                .append("\n\textraTwo      = ").append(extraTwo);
        return sb.toString();
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getAppointedId() {
        return appointedId;
    }

    public void setAppointedId(String appointedId) {
        this.appointedId = appointedId;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getFromNavID() {
        return fromNavID;
    }

    public void setFromNavID(String fromNavID) {
        this.fromNavID = fromNavID;
    }

    public String getNavIndex() {
        return navIndex;
    }

    public void setNavIndex(String navIndex) {
        this.navIndex = navIndex;
    }

    public String getPageID() {
        return pageID;
    }

    public void setPageID(String pageID) {
        this.pageID = pageID;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(String groupIndex) {
        this.groupIndex = groupIndex;
    }

    public String getControlID() {
        return controlID;
    }

    public void setControlID(String controlID) {
        this.controlID = controlID;
    }

    public String getElementID() {
        return elementID;
    }

    public void setElementID(String elementID) {
        this.elementID = elementID;
    }

    public String getElementIndex() {
        return elementIndex;
    }

    public void setElementIndex(String elementIndex) {
        this.elementIndex = elementIndex;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getEpisodeID() {
        return episodeID;
    }

    public void setEpisodeID(String episodeID) {
        this.episodeID = episodeID;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(String recommendType) {
        this.recommendType = recommendType;
    }

    public String getActionID() {
        return actionID;
    }

    public void setActionID(String actionID) {
        this.actionID = actionID;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(String systemTime) {
        this.systemTime = systemTime;
    }

    public String getExtraOne() {
        return extraOne;
    }

    public void setExtraOne(String extraOne) {
        this.extraOne = extraOne;
    }

    public String getExtraTwo() {
        return extraTwo;
    }

    public void setExtraTwo(String extraTwo) {
        this.extraTwo = extraTwo;
    }
}
