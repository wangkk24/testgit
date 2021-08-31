package com.pukka.ydepg.common.http.v6bean.v6node.multplay;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.GetLicenseTrigger;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.PriceObjectDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.ProductRestriction;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: Subscription.java
 * @author: yh
 * @date: 2017-04-24 15:34
 */

public class Subscription {


    /**
     * productOrderKey :
     * productID :
     * productCode
     * productName :
     * productDesc :
     * servicePayType :
     * subServicePayType :
     * price :
     * startTime :
     * endTime :
     * productType :
     * orderTime :
     * isAutoExtend :
     * priceObjectDetail :
     * deviceID :
     * profileID :
     * isMainPackage :
     * orderState :
     * restrictions :
     * triggers :
     * totalChooseNum :
     * residualChooseNum :
     * cycleEndTime :
     * contentRentPeriod :
     * chargeMode :
     * periodLength :
     * picture :
     * extensionFields :
     */
    /**
     * the range of field productType
     */
    public interface ProductType {
        /**
         * product by times
         */
        String BY_TIMES = "1";

        /**
         * product by period
         */
        String BY_PERIOD = "0";
    }

    /**
     * 订购关系主键。
     */
    @SerializedName("productOrderKey")
    private String productOrderKey;


    /**
     * 产品ID。
     */
    @SerializedName("productID")
    private String productID;

    /**
     * 产品外部编号
     */
    @SerializedName("productCode")
    private String productCode;
    /**
     * 产品名称。
     */
    @SerializedName("productName")
    private String productName;

    /**
     * 产品简介。
     */
    @SerializedName("productDesc")
    private String productDesc;

    /**
     * 支付方式。
     * <p>
     * 订购模式，取值包括：
     * <p>
     * CASH：现金订购
     * POINTS：积分订购 [字段预留]
     * PREPAY：预付费订购
     * OSES [字段预留]
     * MOBILE [字段预留]
     * STREAMYX_BILL [字段预留]
     * VOUCHER：优惠劵订购 [字段预留]
     * 默认值为CASH。
     */
    @SerializedName("servicePayType")
    private String servicePayType;

    /**
     * 本参数暂时预留，功能待实现。
     * <p>
     * 支付方式的子分类，取值范围
     * <p>
     * CreditOrDebitCard：信用卡或储蓄卡
     * Maybank2u
     * CIMBClicks
     * Paypal
     * RHB
     * iTalk
     * Digibill
     */
    @SerializedName("subServicePayType")
    private String subServicePayType;

    /**
     * 购买产品实际花费的金额/积分数。
     * <p>
     * 当servicePayType取值为CASH，表示消费的金额，使用货币最小单位。
     * <p>
     * 当servicePaytype取值为POINTS，表示消费的积分数
     */
    @SerializedName("price")
    private String price;

    /**
     * 订购关系生效时间，取值为距离1970年1月1号的毫秒数。
     */
    @SerializedName("startTime")
    private String startTime;

    /**
     * 订购关系失效时间，取值为距离1970年1月1号的毫秒数。
     */
    @SerializedName("endTime")
    private String endTime;

    /**
     * 产品类型，取值范围：
     * <p>
     * 1：按次
     * 0：包周期
     */
    @SerializedName("productType")
    private String productType = "";

    /**
     * 产品订购的时间，取值为距离1970年1月1号的毫秒数。
     */
    @SerializedName("orderTime")
    private String orderTime;

    /**
     * 是否支持自动续订，默认值1。
     * <p>
     * 取值范围：
     * <p>
     * 0：否
     * 1：是
     */
    @SerializedName("isAutoExtend")
    private String isAutoExtend;

    /**
     * 如果订购的是按次产品，表示实际订购的定价对象，定价对象可以是内容、媒资、内容或媒资的特性，目前只支持内容。
     */
    @SerializedName("priceObjectDetail")
    private PriceObjectDetail priceObjectDetail;

    /**
     * 产品绑定的逻辑设备ID，只有非共享产品才有效。
     */
    @SerializedName("deviceID")
    private String deviceID;

    /**
     * 订购该产品的Profile ID。
     */
    @SerializedName("profileID")
    private String profileID;

    /**
     * 是否为基础包，取值包括：
     * <p>
     * 0：非基础包
     * 1：基础包
     * 说明：依据订购的具体产品属性而定
     */
    @SerializedName("isMainPackage")
    private String isMainPackage;

    /**
     * 订购状态，取值包括：
     * <p>
     * 0：订购状态
     * 1：退订状态
     * 说明：依据终端用户的选择而定
     */
    @SerializedName("orderState")
    private String orderState;

    /**
     * 产品支持的限制条件参见2.67-ProductRestriction属性。
     */
    @SerializedName("restrictions")
    private List<ProductRestriction> restrictions;

    /**
     * 客户端集成的是PlayReady且用户已订购成功，下发获取CA License的触发器。
     * <p>
     * [字段预留]
     */
    @SerializedName("triggers")
    private List<GetLicenseTrigger> triggers;

    /**
     * 如果是套餐产品，该属性表示用户可以选订该套餐包含的定价对象个数，如果平台未返回该属性，表示个数不限制，即订购了套餐产品后，套餐中包含的所有定价对象都可以使用。
     * <p>
     * 如果用户已订购了此产品，该属性返回订购时产品的可选内容数量，如果操作员在订购后修改了产品的可选内容数据，该属性值不受影响。
     * <p>
     * [字段预留]
     */
    @SerializedName("totalChooseNum")
    private String totalChooseNum;

    /**
     * 套餐产品的剩余可选内容数量，如果套餐产品未订购，取值totalChooseNum。
     * <p>
     * [字段预留]
     */
    @SerializedName("residualChooseNum")
    private String residualChooseNum;

    /**
     * 对于已订购的包周期续订套餐产品，表示订购关系本周期的结束时间。取值为距离1970年1月1号的毫秒数。
     * <p>
     * [字段预留]
     */
    @SerializedName("cycleEndTime")
    private String cycleEndTime;

    /**
     * 如果是套餐产品且totalChooseNum有值，该属性表示自选内容的租用期，单位为小时。
     * <p>
     * [字段预留]
     */
    @SerializedName("contentRentPeriod")
    private String contentRentPeriod;

    /**
     * 包周期产品的计量方式，取值包括：
     * <p>
     * 0：包月
     * 10：包多天
     * 13 ：包多月
     * 18 ：包天
     * 19 ：包周
     * 20 ：包多周
     */
    @SerializedName("chargeMode")
    private String chargeMode;

    /**
     * 包周期产品的周期长度。
     * <p>
     * 例如，chargeMode取值为13表示包2月时，periodLength取值就是2。
     * <p>
     * 如果是包月、包天或者包周，取值固定是1。
     */
    @SerializedName("periodLength")
    private String periodLength;

    /**
     * 产品的海报路径。
     */
    @SerializedName("picture")
    private Picture picture;
    @SerializedName("isEst")
    private int isEst;
    @SerializedName("isUsed")
    private int isUsed;
    @SerializedName("subscriberId")
    private String subscriberId;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getIsEst() {
        return isEst;
    }

    public void setIsEst(int isEst) {
        this.isEst = isEst;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public long getSubscriberSn() {
        return subscriberSn;
    }

    public void setSubscriberSn(long subscriberSn) {
        this.subscriberSn = subscriberSn;
    }

    public List<NamedParameter> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<NamedParameter> customFields) {
        this.customFields = customFields;
    }

    @SerializedName("subscriberSn")
    private long subscriberSn;
    @SerializedName("customFields")
    private List<NamedParameter> customFields;
    /**
     * 扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getProductOrderKey() {
        return productOrderKey;
    }

    public void setProductOrderKey(String productOrderKey) {
        this.productOrderKey = productOrderKey;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getServicePayType() {
        return servicePayType;
    }

    public void setServicePayType(String servicePayType) {
        this.servicePayType = servicePayType;
    }

    public String getSubServicePayType() {
        return subServicePayType;
    }

    public void setSubServicePayType(String subServicePayType) {
        this.subServicePayType = subServicePayType;
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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getIsAutoExtend() {
        if (TextUtils.isEmpty(isAutoExtend)) {
            isAutoExtend = "1";
        }
        return isAutoExtend;
    }

    public void setIsAutoExtend(String isAutoExtend) {
        this.isAutoExtend = isAutoExtend;
    }

    public PriceObjectDetail getPriceObjectDetail() {
        return priceObjectDetail;
    }

    public void setPriceObjectDetail(PriceObjectDetail priceObjectDetail) {
        this.priceObjectDetail = priceObjectDetail;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getIsMainPackage() {
        return isMainPackage;
    }

    public void setIsMainPackage(String isMainPackage) {
        this.isMainPackage = isMainPackage;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public List<ProductRestriction> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<ProductRestriction> restrictions) {
        this.restrictions = restrictions;
    }

    public List<GetLicenseTrigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<GetLicenseTrigger> triggers) {
        this.triggers = triggers;
    }

    public String getTotalChooseNum() {
        return totalChooseNum;
    }

    public void setTotalChooseNum(String totalChooseNum) {
        this.totalChooseNum = totalChooseNum;
    }

    public String getResidualChooseNum() {
        return residualChooseNum;
    }

    public void setResidualChooseNum(String residualChooseNum) {
        this.residualChooseNum = residualChooseNum;
    }

    public String getCycleEndTime() {
        return cycleEndTime;
    }

    public void setCycleEndTime(String cycleEndTime) {
        this.cycleEndTime = cycleEndTime;
    }

    public String getContentRentPeriod() {
        return contentRentPeriod;
    }

    public void setContentRentPeriod(String contentRentPeriod) {
        this.contentRentPeriod = contentRentPeriod;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Subscription sb = (Subscription) obj;
        if (!productID.equals(sb.getProductID())) {
            return false;
        }
        return true;
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
