package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.multplay.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.multplay.Subscription;

import java.util.List;

/**
 * 作者：panjw on 2021/6/10 10:34
 * <p>
 * 邮箱：panjw@easier.cn
 */
public class MultiMediaAuthorizeResult {
    @SerializedName("result")
    private Result result;
    @SerializedName("productID")
    private String productID;
    @SerializedName("pricedProducts")
    private List<Product> products;
    @SerializedName("unenforcedProducts")
    private List<Subscription> unenforcedProducts;
    @SerializedName("productRestriction")
    private ProductRestriction productRestriction;
    @SerializedName("triggers")
    private List<GetLicenseTrigger> triggers;
    @SerializedName("entitlementEndTime")
    private String entitlementEndTime;
    @SerializedName("contentToken")
    private String contentToken;
    @SerializedName("novelData")
    private String novelData;
    @SerializedName("playURL")
    private String playURL;
    @SerializedName("rentPeriod")
    private long rentPeriod;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Subscription> getUnenforcedProducts() {
        return unenforcedProducts;
    }

    public void setUnenforcedProducts(List<Subscription> unenforcedProducts) {
        this.unenforcedProducts = unenforcedProducts;
    }

    public ProductRestriction getProductRestriction() {
        return productRestriction;
    }

    public void setProductRestriction(ProductRestriction productRestriction) {
        this.productRestriction = productRestriction;
    }

    public List<GetLicenseTrigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<GetLicenseTrigger> triggers) {
        this.triggers = triggers;
    }

    public String getEntitlementEndTime() {
        return entitlementEndTime;
    }

    public void setEntitlementEndTime(String entitlementEndTime) {
        this.entitlementEndTime = entitlementEndTime;
    }

    public String getContentToken() {
        return contentToken;
    }

    public void setContentToken(String contentToken) {
        this.contentToken = contentToken;
    }

    public String getNovelData() {
        return novelData;
    }

    public void setNovelData(String novelData) {
        this.novelData = novelData;
    }

    public String getPlayURL() {
        return playURL;
    }

    public void setPlayURL(String playURL) {
        this.playURL = playURL;
    }

    public long getRentPeriod() {
        return rentPeriod;
    }

    public void setRentPeriod(long rentPeriod) {
        this.rentPeriod = rentPeriod;
    }
}
