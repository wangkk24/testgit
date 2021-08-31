package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.vss.node.ResultInfo;

public class AddAlacarteChoosedContentRequest {
    /*
     *扣减自选包的订购关系ID
     */
    @SerializedName("subscriptionID")
    private String subscriptionID;

    /*
     *订购自选包用户ID
     */
    @SerializedName("userID")
    private String userID;

    /*
     *自选包产品ID
     *如果不传从订购关系subscriptionID上获取产品号
     */
    @SerializedName("productID")
    private String productID;

    /*
     *内容外部ID
     *和订购时PriceObject.type=200或不填一个对象
     */
    @SerializedName("contentID")
    private String contentID;

    /*
     *申请添加内容用户
     *便于记录自选内容计数扣减的申请用户
     */
    @SerializedName("addUserID")
    private String addUserID;

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

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getAddUserID() {
        return addUserID;
    }

    public void setAddUserID(String addUserID) {
        this.addUserID = addUserID;
    }
}
