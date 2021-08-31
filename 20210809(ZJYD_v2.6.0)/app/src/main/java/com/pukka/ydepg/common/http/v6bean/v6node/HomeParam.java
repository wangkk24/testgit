package com.pukka.ydepg.common.http.v6bean.v6node;


import com.google.gson.annotations.SerializedName;

/**
 */

public class HomeParam extends ExtensionField
{
    @SerializedName("type")
    private String type;

    @SerializedName("count")
    private String count;

    public HomeParam()
    {

    }

    public HomeParam(String type, String count)
    {
        this.type = type;
        this.count = count;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getCount()
    {
        return count;
    }

    public void setCount(String count)
    {
        this.count = count;
    }

    @Override
    public String toString()
    {
        return "HomeParam{" +
                "type='" + type + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}