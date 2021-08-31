package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.bean;

import com.google.gson.annotations.SerializedName;

public class ProductForH5Order {

    //产品策划ID（必填）
    @SerializedName("productId")
    private String productId;

    //0 包月 1包年 2单点 3包季（必填）
    @SerializedName("productType")
    private String productType;

    //内容ID （详情页和播放页订购传内容外部ID）
    @SerializedName("contentId")
    private String contentId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
}
