package com.pukka.ydepg.common.http.vss.node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NamedParameter implements Serializable {


    private static final long serialVersionUID = 7916373599529678789L;

    @SerializedName("key")
    private String key;

    @SerializedName("value")
    private String value;

    public NamedParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public NamedParameter() {

    }

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
