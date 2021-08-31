package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

public class SingleMark {

    //单包角标名称
    @SerializedName("productName")
    private String productName;

    //单包角标图片
    @SerializedName("icon")
    private String icon;

    //单包id
    @SerializedName("productId")
    private String productId;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
