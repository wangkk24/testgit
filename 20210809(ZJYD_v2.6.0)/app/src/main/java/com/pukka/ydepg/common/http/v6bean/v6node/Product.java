package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.bean.node.Marketing;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */

public class Product {

    /**
     * ID :
     * name :
     * introduce :
     * price :
     * startTime :
     * endTime :
     * productType :
     * chargeMode :
     * periodLength :
     * isAutoExtend :
     * isMain :
     * isSubscribed :
     * isOnlinePurchase :
     * rentPeriod :
     * priceObject :
     * productRestriction :
     * picture :
     * extensionFields :
     */

    /**
     *产品ID
     */
    @SerializedName("ID")
    private String ID="";

    /**
     *产品名称
     */
    @SerializedName("name")
    private String name;

    /**
     *产品简介
     */
    @SerializedName("introduce")
    private String introduce;

    /**
     *产品价格，单位为最小单位
     */
    @SerializedName("price")
    private String price;

    /**
     *可订购开始时间，取值为距离1970年1月1号的毫秒数
     */
    @SerializedName("startTime")
    private String startTime;

    /**
     *可订购结束时间，时间格式为距离1970年1月1号的毫秒数
     */
    @SerializedName("endTime")
    private String endTime;

    /**
     *产品类型，取值范围：

     0：包周期
     1：按次
     */
    @SerializedName("productType")
    private String productType;

    /**
     *包周期产品的计量方式，取值包括：

     0：包月
     10：包多天
     13：包多月
     18 ：包天
     19 ：包周
     20 ：包多周
     */
    @SerializedName("chargeMode")
    private String chargeMode;

    /**
     *包周期产品的周期长度。

     例如，当chargeMode取值为13时表示包2月，periodLength取值就是2；如果是包月、包天或者包周，取值固定是1

     */
    @SerializedName("periodLength")
    private String periodLength;

    /**
     *是否支持自动续订。

     取值范围：

     0：否
     1：是
     */
    @SerializedName("isAutoExtend")
    private String isAutoExtend;

    /**
     *如果是包周期产品，表示是否为基础包，取值包括：

     0：非基础包
     1：基础包
     终端可以单独订购基础包，但是订购非基础包前，必须先订购基础包

     */
    @SerializedName("isMain")
    private String isMain;

    /**
     *是否已经订购，取值范围：

     0：未订购
     1：已订购
     */
    @SerializedName("isSubscribed")
    private String isSubscribed;

    /**
     *是否支持在线订购，取值包括：

     0：不支持
     1：支持

     */
    @SerializedName("isOnlinePurchase")
    private String isOnlinePurchase;

    /**
     *按次产品租用期，单位：小时
     */
    @SerializedName("rentPeriod")
    private String rentPeriod;

    /**
     *如果是播放和下载鉴权接口返回的可订购按次产品，返回此按次产品适用的定价对象
     */
    @SerializedName("priceObject")
    private PriceObject priceObject;

    /**
     *产品支持的限制条件参见“ProductRestriction”属性
     */
    @SerializedName("productRestriction")
    private ProductRestriction productRestriction;

    /**
     *产品的海报路径
     */
    @SerializedName("picture")
    private Picture picture;

    /**
     *扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    /**
     * 消费率编号。
     */
    @SerializedName("gstCode")
    private String gstCode;

    /**
     * 新需求，产品排序使用
     */
    private int order=Integer.MAX_VALUE-1000;

    /**
     * 营销策略的id
     */
    @SerializedName("market")
    private Marketing marketing;


    /**
     *VOD的扩展属性，其中扩展属性的Key由局点CMS定制。

     */
    @SerializedName("customFields")
    protected List<NamedParameter> customFields;

    public List<NamedParameter> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<NamedParameter> customFields) {
        this.customFields = customFields;
    }

    public Marketing getMarketing()
    {
        return marketing;
    }

    public void setMarketing(Marketing marketing)
    {
        this.marketing = marketing;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getGstCode() {
        return gstCode;
    }

    public void setGstCode(String gstCode) {
        this.gstCode = gstCode;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    /**
     * 产品类型,0 ：包周期;1 ：按次
     */
    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    public String getPeriodLength() {
        return periodLength;
    }

    public void setPeriodLength(String periodLength) {
        this.periodLength = periodLength;
    }

    public String getIsAutoExtend() {
        return isAutoExtend;
    }

    public void setIsAutoExtend(String isAutoExtend) {
        this.isAutoExtend = isAutoExtend;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    public String getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(String isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public String getIsOnlinePurchase() {
        return isOnlinePurchase;
    }

    public void setIsOnlinePurchase(String isOnlinePurchase) {
        this.isOnlinePurchase = isOnlinePurchase;
    }

    public String getRentPeriod() {
        return rentPeriod;
    }

    public void setRentPeriod(String rentPeriod) {
        this.rentPeriod = rentPeriod;
    }

    public PriceObject getPriceObject() {
        return priceObject;
    }

    public void setPriceObject(PriceObject priceObject) {
        this.priceObject = priceObject;
    }

    public ProductRestriction getProductRestriction() {
        return productRestriction;
    }

    public void setProductRestriction(ProductRestriction productRestriction) {
        this.productRestriction = productRestriction;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
