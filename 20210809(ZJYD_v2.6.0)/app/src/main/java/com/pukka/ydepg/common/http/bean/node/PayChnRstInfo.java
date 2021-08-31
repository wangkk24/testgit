package com.pukka.ydepg.common.http.bean.node;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.launcher.bean.node.NamedParameter;

import java.io.Serializable;
import java.util.List;

/**
 * 支付渠道返回信息
 * Created by liudo on 2018/9/17.
 */

public class PayChnRstInfo implements Serializable
{

    private static final long serialVersionUID = 4007701851691805552L;

    /**
     * 等同于paymentID，queryOrderInfo接口返回该字段
     */
    @SerializedName("payID")
    private String payID;

    /**
     * 第三方支付时用的支付ID
     */
    @SerializedName("paymentID")
    private String paymentID;

    @SerializedName("channelType")
    private String channelType;

    @SerializedName("payURL")
    private String payURL;


    @SerializedName("extentionInfo")
    private List<NamedParameter> extentionInfo;

    public String getPayID() {
        return payID;
    }

    public void setPayID(String payID) {
        this.payID = payID;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getPayURL() {
        return payURL;
    }

    public void setPayURL(String payURL) {
        this.payURL = payURL;
    }

    public List<NamedParameter> getExtentionInfo() {
        return extentionInfo;
    }

    public void setExtentionInfo(List<NamedParameter> extentionInfo) {
        this.extentionInfo = extentionInfo;
    }
}
