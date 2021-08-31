package com.pukka.ydepg.common.http.bean.node;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liudo on 2018/9/13.
 */

public class Marketing
{
    @SerializedName("offerID")
    private String offerID="";

    @SerializedName("tag")
    private String tag;

    @SerializedName("effTime")
    private String effTime;

    @SerializedName("expTime")
    private String expTime;

    public String getStartTime()
    {
        return effTime;
    }

    public void setStartTime(String startTime)
    {
        this.effTime = startTime;
    }

    public String getEndTime()
    {
        return expTime;
    }

    public void setEndTime(String endTime)
    {
        this.expTime = endTime;
    }

    public String getId()
    {
        return offerID;
    }

    public void setId(String id)
    {
        this.offerID = id;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }


}
