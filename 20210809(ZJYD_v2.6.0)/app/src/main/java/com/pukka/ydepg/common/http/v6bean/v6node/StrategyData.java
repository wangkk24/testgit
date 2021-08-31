package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6node.StrategyData.java
 * @author: luwm
 * @data: 2018-07-24 11:07
 * @Version V1.0 <描述当前版本功能>
 */
public class StrategyData {
    @SerializedName("resourceID")
    private String resourceID;
    @SerializedName("resStrategyDatas")
    private List<ResStrategyData> resStrategyDatas;
    @SerializedName("extensionInfo")
    private List<NamedParameter> extensionInfo;

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public List<ResStrategyData> getResStrategyDatas() {
        return resStrategyDatas;
    }

    public void setResStrategyDatas(List<ResStrategyData> resStrategyDatas) {
        this.resStrategyDatas = resStrategyDatas;
    }

    public List<NamedParameter> getExtensionInfo() {
        return extensionInfo;
    }

    public void setExtensionInfo(List<NamedParameter> extensionInfo) {
        this.extensionInfo = extensionInfo;
    }

    @Override
    public String toString() {
        return "StrategyData{" +
                "resourceID='" + resourceID + '\'' +
                ", resStrategyDatas=" + resStrategyDatas +
                ", extensionInfo=" + extensionInfo +
                '}';
    }
}
