package com.pukka.ydepg.common.http.bean.node;

import com.google.gson.annotations.SerializedName;

public class LogDesc {

    @SerializedName("KeyName")
    private String KeyName="";

    @SerializedName("Value")
    private String Value="";

    public String getKeyName() {
        return KeyName;
    }

    public void setKeyName(String keyName) {
        KeyName = keyName;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
