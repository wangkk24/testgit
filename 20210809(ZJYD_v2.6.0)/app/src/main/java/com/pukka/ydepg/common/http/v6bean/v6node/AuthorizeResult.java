package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */

public class AuthorizeResult {

    /**
     * productID :
     * pricedProducts :
     * unenforcedProducts :
     * reliantProducts :
     * productRestriction :
     * isLocked :
     * isParentControl :
     * contentID :
     * contentType :
     * triggers :
     */

    /**
     * 如果鉴权成功，返回订购的产品ID。
     */
    @SerializedName("productID")
    private String productID;

    /**
     *如果PlayVOD和PlayChannel接口入参isReturnProduct指定要返回内容定价的产品，返回产品列表，默认按照产品价格升序排列。
     */
    @SerializedName("pricedProducts")
    private List<Product> pricedProducts;

    /**
     *返回用户已订购但是未生效的内容定价的产品。
     */
    @SerializedName("unenforcedProducts")
    private List<Subscription> unenforcedProducts;

    /**
     *如果鉴权的内容/媒资依赖其他业务且被依赖的业务没有订购，返回被依赖的业务的可订购产品列表。
     * 该参数当前不支持。预留。
     */
    @SerializedName("reliantProducts")
    private List<Product> reliantProducts;

    /**
     * 如果内容已订购，但是内容定价的产品限制条件检查失败，返回产品的限制条件。
     */
    @SerializedName("productRestriction")
    private ProductRestriction productRestriction;

    /**
     * 如果接口要求检查锁和父母字，返回内容是否被加锁，取值包括：
     * -1：未知
     * 0：未加锁
     * 1：已加锁
     * 如果终端不做父母字和锁检查，返回-1。
     */
    @SerializedName("isLocked")
    private String isLocked;

    /**
     * 如果接口要求检查锁和父母字，返回内容是否被父母字控制，取值包括：
     * -1：未知
     * 0：未控制
     * 1：已控制
     * 如果终端不做父母字和锁检查，返回-1
     */
    @SerializedName("isParentControl")
    private String isParentControl;

    /**
     * 如果内容被加锁或者被父母字控制，返回受限的内容ID
     */
    @SerializedName("contentID")
    private String contentID;

    /**
     * 如果内容被加锁或者被父母字控制，返回受限的内容类型，取值包括：
     * VOD：点播
     * CHANNEL：频道
     * PROGRAM：节目单
     * SUBJECT：栏目
     */
    @SerializedName("contentType")
    private String contentType;

    /**
     *客户端集成的是PlayReady，下发获取CA License的触发器
     */
    @SerializedName("triggers")
    private List<GetLicenseTrigger> triggers;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public List<Product> getPricedProducts() {
        return pricedProducts;
    }

    public void setPricedProducts(List<Product> pricedProducts) {
        this.pricedProducts = pricedProducts;
    }

    public List<Subscription> getUnenforcedProducts() {
        return unenforcedProducts;
    }

    public void setUnenforcedProducts(List<Subscription> unenforcedProducts) {
        this.unenforcedProducts = unenforcedProducts;
    }

    public List<Product> getReliantProducts() {
        return reliantProducts;
    }

    public void setReliantProducts(List<Product> reliantProducts) {
        this.reliantProducts = reliantProducts;
    }

    public ProductRestriction getProductRestriction() {
        return productRestriction;
    }

    public void setProductRestriction(ProductRestriction productRestriction) {
        this.productRestriction = productRestriction;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getIsParentControl() {
        return isParentControl;
    }

    public void setIsParentControl(String isParentControl) {
        this.isParentControl = isParentControl;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<GetLicenseTrigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<GetLicenseTrigger> triggers) {
        this.triggers = triggers;
    }
}
