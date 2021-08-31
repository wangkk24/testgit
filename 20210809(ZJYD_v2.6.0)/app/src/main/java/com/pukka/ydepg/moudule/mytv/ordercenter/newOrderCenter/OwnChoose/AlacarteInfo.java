package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.OwnChoose;

import com.google.gson.annotations.SerializedName;

public class AlacarteInfo {

    @SerializedName("productID")
    private String productID;

    @SerializedName("residualChooseNum")
    private int residualChooseNum;

    @SerializedName("subscriptionID")
    private String subscriptionID;

    @SerializedName("userID")
    private String userID;


    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getResidualChooseNum() {
        return residualChooseNum;
    }

    public void setResidualChooseNum(int residualChooseNum) {
        this.residualChooseNum = residualChooseNum;
    }

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
}
