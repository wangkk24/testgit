package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 资源位策略匹配数据
 *
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6node.ResStrategyData.java
 * @author: luwm
 * @data: 2018-07-24 11:08
 * @Version V1.0 <描述当前版本功能>
 */
public class ResStrategyData {
    @SerializedName("resItemType")
    private int resItemType;
    @SerializedName("contentURL")
    private String contentURL;
    @SerializedName("textContent")
    private String textContent;
    @SerializedName("actionType")
    private int actionType;
    @SerializedName("actionURL")
    private String actionURL;
    @SerializedName("appParam")
    private String appParam;
    @SerializedName("validStartTime")
    private long validStartTime;
    @SerializedName("validEndTime")
    private long validEndTime;
    @SerializedName("updateTime")
    private String updateTime;
    @SerializedName("introduce")
    private String introduce;
    @SerializedName("extensionInfo")
    private List<NamedParameter> extensionInfo;

    public int getResItemType() {
        return resItemType;
    }

    public void setResItemType(int resItemType) {
        this.resItemType = resItemType;
    }

    public String getContentURL() {
        return contentURL;
    }

    public void setContentURL(String contentURL) {
        this.contentURL = contentURL;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getActionURL() {
        return actionURL;
    }

    public void setActionURL(String actionURL) {
        this.actionURL = actionURL;
    }

    public String getAppParam() {
        return appParam;
    }

    public void setAppParam(String appParam) {
        this.appParam = appParam;
    }

    public long getValidStartTime() {
        return validStartTime;
    }

    public void setValidStartTime(long validStartTime) {
        this.validStartTime = validStartTime;
    }

    public long getValidEndTime() {
        return validEndTime;
    }

    public void setValidEndTime(long validEndTime) {
        this.validEndTime = validEndTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public List<NamedParameter> getExtensionInfo() {
        return extensionInfo;
    }

    public void setExtensionInfo(List<NamedParameter> extensionInfo) {
        this.extensionInfo = extensionInfo;
    }

    @Override
    public String toString() {
        return "ResStrategyData{" +
                "resItemType=" + resItemType +
                ", contentURL='" + contentURL + '\'' +
                ", textContent='" + textContent + '\'' +
                ", actionType=" + actionType +
                ", actionURL='" + actionURL + '\'' +
                ", appParam='" + appParam + '\'' +
                ", validStartTime=" + validStartTime +
                ", validEndTime=" + validEndTime +
                ", updateTime='" + updateTime + '\'' +
                ", introduce='" + introduce + '\'' +
                ", extensionInfo=" + extensionInfo +
                '}';
    }
}
