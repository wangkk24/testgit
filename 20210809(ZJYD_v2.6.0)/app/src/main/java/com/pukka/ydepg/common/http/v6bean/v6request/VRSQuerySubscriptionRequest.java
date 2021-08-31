package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

public class VRSQuerySubscriptionRequest {
    /*
     *消息唯一标识, 生成规则:
     *appKey(6位)+deviceID(8位)+YYMMDDHHMMSS+4位序列号
     *本地时间.
     *appKey: 获取对应的后6位值.
     *deviceID: 预留给Partner标识服务器节点或者终端编号, 建议数字和字母组合使用.
     *YYMMDDHHMMSS: 本地时间戳
     *4位序列号: 0000 – 9999
     */
    @SerializedName("messageID")
    private String messageID;

    /*
     *用户标识类型
     *=1，表示userId为用户id
     */
    @SerializedName("userType")
    private String userType;

    /*
     *用户标识
     */
    @SerializedName("userId")
    private String userId;

    /*
     *查询产品类型：
     *1 按次产品（特殊点播）
     *2 合约产品（长短期会员、包周期）
     */
    @SerializedName("productType")
    private String productType;

    /*
     *产品Id
     */
    @SerializedName("productId")
    private String productId;

    /*
     *订购关系ID，如果传入该字段时，productType也必填
     */
    @SerializedName("subscriptionId")
    private String subscriptionId;

    /*
     *订购生效时间的开始查询时间，可以不填，生效时间 <= subTime1，与subTime2成对出现
     */
    @SerializedName("subTime1")
    private String subTime1;

    /*
     *订购生效时间的结束查询时间，可以不填，生效时间 >= subTime2，与subTime1成对出现
     */
    @SerializedName("subTime2")
    private String subTime2;

    /*
     *查询方式
     *0：所有
     *1：有效（默认）
     *2：失效
     */
    @SerializedName("queryMode")
    private String queryMode;

    /*
     *条目的开始序号。从1开始。如果小于等于0，则不分页，不填写则全量查询，与endIndex成对出现
     */
    @SerializedName("startIndex")
    private String startIndex;

    /*
     *条目的结束序号，不填写则全量查询，与startIndex成对出现
     */
    @SerializedName("endIndex")
    private String endIndex;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubTime1() {
        return subTime1;
    }

    public void setSubTime1(String subTime1) {
        this.subTime1 = subTime1;
    }

    public String getSubTime2() {
        return subTime2;
    }

    public void setSubTime2(String subTime2) {
        this.subTime2 = subTime2;
    }

    public String getQueryMode() {
        return queryMode;
    }

    public void setQueryMode(String queryMode) {
        this.queryMode = queryMode;
    }

    public String getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(String startIndex) {
        this.startIndex = startIndex;
    }

    public String getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(String endIndex) {
        this.endIndex = endIndex;
    }
}
