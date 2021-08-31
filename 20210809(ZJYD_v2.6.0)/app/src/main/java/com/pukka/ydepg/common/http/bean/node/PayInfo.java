package com.pukka.ydepg.common.http.bean.node;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.launcher.bean.node.NamedParameter;

import java.util.List;

/**
 * 支付信息
 * Created by liudo on 2018/9/14.
 */

public class PayInfo
{
    /**
     *1:直接支付
     * 3：页面支付
     */
    @SerializedName("payType")
    private String payType;

    /**
     * 币种Currency code
     *参考ISO 4217规范定义
     *比如：CNY
     *
     */
    @SerializedName("currency")
    private String curreny;

    /**
     * 支付系统用户编号
     *即PayId，没有则填空
     *
     */
    @SerializedName("paymentUserId")
    private String paymentUserId;
    /**
     * 第三方支付时，用户支付完成后跳转的商户页面URL
     */
    @SerializedName("backURL")
    private String backURL;
    /**
     * 支付取消页面
     */
    @SerializedName("cancelURL")
    private String cancelURL;
    /**
     * 支付渠道
     */

    @SerializedName("payChannels")
    private List<PayChannel> payChannels;

    @SerializedName("mobileNum")
    private String mobileNum;

    @SerializedName("verifiedCode")
    private String verifiedCode;

    @SerializedName("extentionInfo")
    private List<NamedParameter> extentionInfo;

    public String getPayType()
    {
        return payType;
    }

    public void setPayType(String payType)
    {
        this.payType = payType;
    }

    public String getCurreny()
    {
        return curreny;
    }

    public void setCurreny(String curreny)
    {
        this.curreny = curreny;
    }

    public String getPaymentUserId()
    {
        return paymentUserId;
    }

    public void setPaymentUserId(String paymentUserId)
    {
        this.paymentUserId = paymentUserId;
    }

    public String getBackURL()
    {
        return backURL;
    }

    public void setBackURL(String backURL)
    {
        this.backURL = backURL;
    }

    public String getCancelURL()
    {
        return cancelURL;
    }

    public void setCancelURL(String cancelURL)
    {
        this.cancelURL = cancelURL;
    }

    public List<PayChannel> getPayChannels()
    {
        return payChannels;
    }

    public void setPayChannels(List<PayChannel> payChannels)
    {
        this.payChannels = payChannels;
    }

    public List<NamedParameter> getExtentionInfo()
    {
        return extentionInfo;
    }

    public void setExtentionInfo(List<NamedParameter> extentionInfo)
    {
        this.extentionInfo = extentionInfo;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getVerifiedCode() {
        return verifiedCode;
    }

    public void setVerifiedCode(String verifiedCode) {
        this.verifiedCode = verifiedCode;
    }
}
