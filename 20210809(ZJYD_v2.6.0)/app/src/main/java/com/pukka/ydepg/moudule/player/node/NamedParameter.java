package com.pukka.ydepg.moudule.player.node;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NamedParameter implements Serializable
{


    @SerializedName("value")
    private String value;
    @SerializedName("key")
    private String key;


    public NamedParameter(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public NamedParameter()
    {

    }


    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

}
