package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

public class VRSSubscription {
    /*
     *用户Id
     */
    @SerializedName("userId")
    private String userId;

    /*
     *产品Id
     */
    @SerializedName("productId")
    private String productId;

    /*
     *内容Id
     */
    @SerializedName("contentId")
    private String contentId;

    /*
     *订购状态：
     *1、已订购
     *2、暂停
     *9、已退订
     *该字段仅用于判定合约是否可订购，可退定等操作，不影响合约是否可用。
     */
    @SerializedName("status")
    private String status;

    /*
     *产品类型：
     *1 一次性产品
     *2 合约产品
     */
    @SerializedName("productType")
    private String productType;

    /*
     *开始时间，为UTC格式，长度14位，格式：YYYYMMDDHHMMSS
     */
    @SerializedName("startTime")
    private String startTime;

    /*
     *结束时间，为UTC格式，长度14位，格式：YYYYMMDDHHMMSS
     */
    @SerializedName("endTime")
    private String endTime;

    /*
     *定购时间，为UTC格式，长度14位，格式：YYYYMMDDHHMMSS
     */
    @SerializedName("subTime")
    private String subTime;

    /*
     *订购渠道
     *0:BSS页面订购(AdminPortal)
     *1:EPG在线订购(Client)
     *2:BOSS后台订购(CRM)
     *3:其他
     */
    @SerializedName("subWay")
    private String subWay;

    /*
     *退订时间，指的是用户退订操作的时间，不是订购关系失效时间。
     */
    @SerializedName("unSubTime")
    private String unSubTime;

    /*
     *支付渠道：
     *话费支付（实时）
     *话费支付（文件）
     *流量支付（实时）
     *第三方支付
     */
    @SerializedName("payWay")
    private String payWay;

    /*
     *价格
     */
    @SerializedName("price")
    private String price;

    /*
     *spId
     */
    @SerializedName("spId")
    private String spId;

    /*
     *订购订单ID（查询未支付订单时返回）
     */
    @SerializedName("orderId")
    private String orderId;

    /*
     *订购关系ID，唯一主键
     */
    @SerializedName("subscriptionID")
    private String subscriptionID;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSubTime() {
        return subTime;
    }

    public void setSubTime(String subTime) {
        this.subTime = subTime;
    }

    public String getSubWay() {
        return subWay;
    }

    public void setSubWay(String subWay) {
        this.subWay = subWay;
    }

    public String getUnSubTime() {
        return unSubTime;
    }

    public void setUnSubTime(String unSubTime) {
        this.unSubTime = unSubTime;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = subscriptionID;
    }
}
