package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.bean;

import com.google.gson.annotations.SerializedName;

public class LogDescExtInfo {

    @SerializedName("keyName")
    private String key;

    @SerializedName("value")
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
