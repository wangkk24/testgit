package com.pukka.ydepg.common.http.v6bean.v6node;


import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.report.ubd.extension.RecommendData;

import java.util.List;
import java.util.Map;

/**
 */

public class Element
{
    /**
     * id : 1_1
     * type : 0
     * left : 1
     * top : 1
     * width : 1
     * height : 1
     * groupID :
     * controlType : 0
     * canFocus : true
     * defaultFocus : false
     * autoPlay : false
     * forceDefaultData : false
     * elementDatas : [{"name":"001_1","description":"",
     * "contentURL":"${jmpurl}/ContentCode=${code}&ContentType=VOD&Action=SmallScreen
     * ","textContent":"","elementAction":{"actionType":"0",
     * "actionURL":"${jmpurl}/ContentCode=${code}&ContentType=VOD&Action=FullScreen",
     * "appParam":""}}]
     */
    @SerializedName("id")
    private String id;
    @SerializedName("type")
    private String type;
    @SerializedName("index")
    private String index;
    @SerializedName("left")
    private String left;
    @SerializedName("top")
    private String top;
    @SerializedName("width")
    private String width;
    @SerializedName("height")
    private String height;
    @SerializedName("groupID")
    private String groupID;
    @SerializedName("controlType")
    private String controlType;
    @SerializedName("canFocus")
    private String canFocus;
    @SerializedName("defaultFocus")
    private String defaultFocus;
    @SerializedName("extraData")
    private Map<String,String> ExtraData;

    private String notice;

    private String sceneId;

    private String appointedId;

    private String recommendId;

    private String recommendType = RecommendData.RecommendType.HAND_TYPE;

    private String clickTrackerUrl;

    public String getClickTrackerUrl() {
        return clickTrackerUrl;
    }

    public void setClickTrackerUrl(String clickTrackerUrl) {
        this.clickTrackerUrl = clickTrackerUrl;
    }

    public String getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(String recommendId) {
        this.recommendId = recommendId;
    }

    public String getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(String recommendType) {
        this.recommendType = recommendType;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getAppointedId() {
        return appointedId;
    }

    public void setAppointedId(String appointedId) {
        this.appointedId = appointedId;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    /**
     * the range of field vodType
     */
    public interface DefaultFocus
    {
        String TRUE = "true";

        String FALSE = "false";
    }
    @SerializedName("autoPlay")
    private String autoPlay;
    @SerializedName("forceDefaultData")
    private String forceDefaultData;
    @SerializedName("elementDatas")
    private List<ElementData> elementDataList;

    public Map<String, String> getExtraData() {
        return ExtraData;
    }

    public void setExtraData(Map<String, String> extraData) {
        ExtraData = extraData;
    }

    public String getId() { return id;}

    public void setId(String id) { this.id = id;}

    public String getType() { return type;}

    public void setType(String type) { this.type = type;}

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    public String getLeft() { return left;}

    public void setLeft(String left) { this.left = left;}

    public String getTop() { return top;}

    public void setTop(String top) { this.top = top;}

    public String getWidth() { return width;}

    public void setWidth(String width) { this.width = width;}

    public String getHeight() { return height;}

    public void setHeight(String height) { this.height = height;}

    public String getGroupID() { return groupID;}

    public void setGroupID(String groupID) { this.groupID = groupID;}

    public String getControlType() { return controlType;}

    public void setControlType(String controlType) { this.controlType = controlType;}

    public String getCanFocus() { return canFocus;}

    public void setCanFocus(String canFocus) { this.canFocus = canFocus;}

    public String getDefaultFocus() { return defaultFocus;}

    public void setDefaultFocus(String defaultFocus)
    {
        this.defaultFocus = defaultFocus;
    }

    public String getAutoPlay() { return autoPlay;}

    public void setAutoPlay(String autoPlay) { this.autoPlay = autoPlay;}

    public String getForceDefaultData() { return forceDefaultData;}

    public void setForceDefaultData(String forceDefaultData)
    {
        this.forceDefaultData = forceDefaultData;
    }

    public List<ElementData> getElementDataList() { return elementDataList;}

    public void setElementDataList(List<ElementData> elementDataList)
    {
        this.elementDataList = elementDataList;
    }
}