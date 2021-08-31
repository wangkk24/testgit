package com.pukka.ydepg.common.http.vss.node;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.bean.node.PayChnRstInfo;

import java.io.Serializable;
import java.util.List;

public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 4919022537680680059L;
    /**
     * 【M】订单ID
     */
    @SerializedName("orderID")
    private String orderID;

    /**
     * 【M】订单创建者ID
     */
    @SerializedName("userID")
    private String userID;

    /**
     * 【O】支付人ID
     */
    @SerializedName("payUserId")
    private String payUserId;

    /**
     * 【M】订单创建时间yyyyMMddhhmmss，本地时间
     */
    @SerializedName("createTime")
    private String createTime;

    /**
     * 【O】订单失效时间yyyyMMddhhmmss，本地时间，预留
     */
    @SerializedName("expireTime")
    private String expireTime;

    /**
     * 订单状态
     * λ	0：草稿状态
     * λ	1：已提交状态
     * λ	2：支付中状态
     * λ	3：已支付状态
     * λ	4：已取消状态
     * λ	5：已完成状态
     * λ	6：履约中状态
     * λ	7：退订中状态
     * λ    8：已退订状态
     */
    @SerializedName("orderStatus")
    private String orderStatus;

    /**
     * 产品价格，单位为分
     */
    @SerializedName("fee")
    private String fee;

    /**
     * 【O】支付结果通知URL，支付网关根据该地址将支付结果通知到VSS
     */
    @SerializedName("callbackURL")
    private String callbackURL;

    /**
     * 【M】支付信息
     */
    @SerializedName("payChannel")
    private List<PayChnRstInfo> payChannel;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPayUserId() {
        return payUserId;
    }

    public void setPayUserId(String payUserId) {
        this.payUserId = payUserId;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public List<PayChnRstInfo> getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(List<PayChnRstInfo> payChannel) {
        this.payChannel = payChannel;
    }

    public interface Status {
        /**
         * 0: 草稿状态
         */
        static final int DRAFT = 0;
        /**
         * 1: 已提交状态
         */
        static final int COMMIT = 1;

        /**
         * 2：支付中状态
         */
        static final int PROCESS_PAY = 2;

        /**
         * 3：已支付状态
         */
        static final int PAYED = 2;
        /**
         * 4：已取消状态
         */
        static final int CANCELED = 4;

        /**
         * 5：已完成状态
         */
        static final int FINISHED = 5;

        /**
         * 6：履约中状态
         */
        static final int PERFORMANCE_STIPULATION = 6;
    }
}