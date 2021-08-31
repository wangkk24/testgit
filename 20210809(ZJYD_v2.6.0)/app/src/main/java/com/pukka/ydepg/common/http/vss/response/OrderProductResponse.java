package com.pukka.ydepg.common.http.vss.response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.bean.node.PayChnRstInfo;
import com.pukka.ydepg.common.http.vss.node.ResultInfo;
import com.pukka.ydepg.launcher.bean.node.NamedParameter;

import java.io.Serializable;
import java.util.List;

public class OrderProductResponse implements Serializable {

    private static final long serialVersionUID = 5091222900111601388L;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("orderId")
    private String orderId;

    @SerializedName("createTime")
    private String createTime;

    @SerializedName("expireTime")
    private String expireTime;

    @SerializedName("status")
    private String status;

    @SerializedName("fee")
    private String fee;

    @SerializedName("callbackURL")
    private String callbackURL;

    @SerializedName("payChanResInfos")
    private List<PayChnRstInfo> payChanResInfos;

    @SerializedName("extentionInfo")
    private List<NamedParameter> extentionInfo;


    /**
     * 返回结果
     */
    @SerializedName("result")
    private ResultInfo result;

    public ResultInfo getResult() {
        return result;
    }

    public List<NamedParameter> getExtentionInfo() {
        return extentionInfo;
    }

    public void setExtentionInfo(List<NamedParameter> extentionInfo) {
        this.extentionInfo = extentionInfo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }

    public List<PayChnRstInfo> getPayChanResInfos() {
        return payChanResInfos;
    }

    public void setPayChanResInfos(List<PayChnRstInfo> payChanResInfos) {
        this.payChanResInfos = payChanResInfos;
    }
}
