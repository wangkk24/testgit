package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

public class SubMutex {
    //订购关系ID
    @SerializedName("subscriptionID")
    private String subscriptionID;

    //用户号
    @SerializedName("userID")
    private String userID;

    //订购产品
    @SerializedName("productID")
    private String productID;

    /*
     订购状态：
     1、正常
     9、已退订
     该字段仅用于判定包周期订购实例是否可订购，可退定等操作，不影响包周期订购实例是否可用。
     */
    @SerializedName("status")
    private String status;

    /*
     订购关系开始时间，为本地格式，长度14位，格式：YYYYMMDDHHMMSS
     */
    @SerializedName("startTime")
    private String startTime;

    /*
     订购关系结束时间，为本地格式，长度14位，格式：YYYYMMDDHHMMSS
     */
    @SerializedName("endTime")
    private String endTime;

    /*：
     订购时间，为本地格式，长度14位，格式：YYYYMMDDHHMMSS
     */
    @SerializedName("subTime")
    private String subTime;

    /*
     互斥规则类型
     =1，互斥，即待订购产品与该条订购关系禁止互斥导致无法订购
     =3，替换，即因为待订购产品导致该产品被立即退订
     */
    @SerializedName("ruleType")
    private String ruleType;

    public static final String RULE_TYPE_MUTEX = "1";

    public static final String RULE_TYPE_REPLACE = "3";

    public String getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }
}
