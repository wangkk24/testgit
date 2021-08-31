package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.OwnChoose;

import com.pukka.ydepg.common.http.v6bean.v6node.Product;

import java.util.List;

public class OwnChooseEvent {
    //提示展示自选集包的event
    public boolean needShowAlacarteChoosePopWindow;

    //自选集包的产品id
    public String productId = "";

    //自选集包的订购关系id
    public String subscriptionId = "";

    //自选集包剩余集数
    public String total = "";

    //资源关联的产品包
    private List<Product> pricedProducts;

    //自选集包的错误码
    public String code = "";
    public static final String CODE_BRONZE_ENOUGH = "506_600000706846";
    public static final String CODE_SILVER_ENOUGH = "506_600000706826";
    public static final String CODE_BRONZE_NOT_ENOUGH = "507_600000706846";
    public static final String CODE_SILVER_NOT_ENOUGH = "507_600000706826";

    public boolean AlacarteComplete = false;

    public OwnChooseEvent(boolean needShowAlacarteChoosePopWindow) {
        this.needShowAlacarteChoosePopWindow = needShowAlacarteChoosePopWindow;
    }

    public boolean isNeedShowAlacarteChoosePopWindow() {
        return needShowAlacarteChoosePopWindow;
    }

    public void setNeedShowAlacarteChoosePopWindow(boolean needShowAlacarteChoosePopWindow) {
        this.needShowAlacarteChoosePopWindow = needShowAlacarteChoosePopWindow;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public boolean isAlacarteComplete() {
        return AlacarteComplete;
    }

    public void setAlacarteComplete(boolean alacarteComplete) {
        AlacarteComplete = alacarteComplete;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Product> getPricedProducts() {
        return pricedProducts;
    }

    public void setPricedProducts(List<Product> pricedProducts) {
        this.pricedProducts = pricedProducts;
    }
}
