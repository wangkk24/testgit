package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

public class GetAlacarteChoosedContentsRequest {

    /*
     *订购自选包用户ID
     */
    @SerializedName("userID")
    private String userID;

    /*
     *自选包产品ID
     *如果非自选包产品，则请求拒绝
     */
    @SerializedName("productID")
    private String productID;


    /*
     *内容外部ID
     *和订购时PriceObject.type=200或不填一个对象
     *如果不传参或者参数为空，则查询所有的已添加且当前时间有效的内容
     *如果传参且不为空，则只查询已添加且有效的该内容
     */
    @SerializedName("contentID")
    private String contentID;


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
}
