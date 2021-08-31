package com.pukka.ydepg.common.http.bean.node;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.launcher.bean.node.NamedParameter;

import java.util.List;

/**
 * Created by liudo on 2018/9/14.
 */

public class ChannelInfo
{
    /**
     * 订购渠道类型
     * 1 Client
     * 2 CRM/BOSS
     * 3 SMS
     * 4 USSD
     * 5 AdminPortal
     * 填写1
     */

    @SerializedName("channelType")
    private String channelType;
    /**
     * 渠道商Id
     *填写SPID
     *
     */
    @SerializedName("channelId")
    private String channelId;

    @SerializedName("extentionInfo")
    private List<NamedParameter> extentionInfo;

    public String getChannelType()
    {
        return channelType;
    }

    public void setChannelType(String channelType)
    {
        this.channelType = channelType;
    }

    public String getChannelId()
    {
        return channelId;
    }

    public void setChannelId(String channelId)
    {
        this.channelId = channelId;
    }

    public List<NamedParameter> getExtentionInfo()
    {
        return extentionInfo;
    }

    public void setExtentionInfo(List<NamedParameter> extentionInfo)
    {
        this.extentionInfo = extentionInfo;
    }
}
