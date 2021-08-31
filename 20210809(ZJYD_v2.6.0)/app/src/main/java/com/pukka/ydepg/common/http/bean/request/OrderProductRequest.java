package com.pukka.ydepg.common.http.bean.request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.bean.node.ChannelInfo;
import com.pukka.ydepg.common.http.bean.node.Marketing;
import com.pukka.ydepg.common.http.bean.node.PayInfo;
import com.pukka.ydepg.common.http.bean.node.PromSubInfo;
import com.pukka.ydepg.common.http.bean.node.SubProdInfo;
import com.pukka.ydepg.common.http.bean.node.UnsubProdInfo;
import com.pukka.ydepg.common.http.bean.node.User;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.bean.request.BaseRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by liudo on 2018/9/14.
 */

public class OrderProductRequest extends BaseRequest
{

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("userID")
    private User userID;

    @SerializedName("subInfo")
    private SubProdInfo subInofo;

    @SerializedName("unSubInfo")
    private List<UnsubProdInfo> unSubInfo;

    @SerializedName("payInfo")
    private PayInfo payInfo;

    @SerializedName("promSubInfo")
    private PromSubInfo promSubInfo;

    @SerializedName("channelInfo")
    private ChannelInfo channelInfo;

    @SerializedName("effectTime")
    private String effectTime;

    @SerializedName("expiredTime")
    private String expiredTime;

    @SerializedName("notifyURL")
    private String notifyURL;

    @SerializedName("deviceIds")
    private List<String> deviceIds;

    @SerializedName("extentionInfo")
    private Map<String ,String> extentionInfo;

    @SerializedName("promListBoss")
    private List<Marketing> promListBoss;

    public String getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }

    public User getUserID()
    {
        return userID;
    }

    public void setUserID(User userID)
    {
        this.userID = userID;
    }

    public SubProdInfo getSubInofo()
    {
        return subInofo;
    }

    public void setSubInofo(SubProdInfo subInofo)
    {
        this.subInofo = subInofo;
    }

    public List<UnsubProdInfo> getUnSubInfo()
    {
        return unSubInfo;
    }

    public void setUnSubInfo(List<UnsubProdInfo> unSubInfo)
    {
        this.unSubInfo = unSubInfo;
    }

    public PayInfo getPayInfo()
    {
        return payInfo;
    }

    public void setPayInfo(PayInfo payInfo)
    {
        this.payInfo = payInfo;
    }

    public PromSubInfo getPromSubInfo()
    {
        return promSubInfo;
    }

    public void setPromSubInfo(PromSubInfo promSubInfo)
    {
        this.promSubInfo = promSubInfo;
    }

    public ChannelInfo getChannelInfo()
    {
        return channelInfo;
    }

    public void setChannelInfo(ChannelInfo channelInfo)
    {
        this.channelInfo = channelInfo;
    }

    public String getEffectTime()
    {
        return effectTime;
    }

    public void setEffectTime(String effectTime)
    {
        this.effectTime = effectTime;
    }

    public String getExpiredTime()
    {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime)
    {
        this.expiredTime = expiredTime;
    }

    public String getNotifyURL()
    {
        return notifyURL;
    }

    public void setNotifyURL(String notifyURL)
    {
        this.notifyURL = notifyURL;
    }

    public List<String> getDeviceIds()
    {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds)
    {
        this.deviceIds = deviceIds;
    }

    public Map<String, String> getExtentionInfo()
    {
        return extentionInfo;
    }

    public void setExtentionInfo(Map<String, String> extentionInfo)
    {
        this.extentionInfo = extentionInfo;
    }

    public List<Marketing> getPromListBoss()
    {
        return promListBoss;
    }

    public void setPromListBoss(List<Marketing> promListBoss)
    {
        this.promListBoss = promListBoss;
    }
}
