package com.pukka.ydepg.common.http.vss.node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

public class PayChannel implements Serializable {

    private static final long serialVersionUID = 8363692983965283821L;

    @SerializedName("channelType")
    private String channelType;

    @SerializedName("currency")
    private String currency;

    @SerializedName("extentionInfo")
    private Map<String, String> extentionInfo;

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Map<String, String> getExtentionInfo() {
        return extentionInfo;
    }

    public void setExtentionInfo(Map<String, String> extentionInfo) {
        this.extentionInfo = extentionInfo;
    }
}
