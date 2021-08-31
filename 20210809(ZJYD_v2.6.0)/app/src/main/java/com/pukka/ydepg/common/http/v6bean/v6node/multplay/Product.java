package com.pukka.ydepg.common.http.v6bean.v6node.multplay;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.PriceObject;
import com.pukka.ydepg.common.http.v6bean.v6node.ProductRestriction;

import java.util.List;

/**
 * 作者：panjw on 2021/6/10 10:40
 * <p>
 * 邮箱：panjw@easier.cn
 */
public class Product {
    @SerializedName("ID")
    private String ID;
    @SerializedName("name")
    private String name;
    @SerializedName("productCode")
    private String productCode;
    @SerializedName("introduce")
    private String introduce;
    @SerializedName("price")
    private String price;
    @SerializedName("startTime")
    private String startTime;
    @SerializedName("endTime")
    private String endTime;
    @SerializedName("productType")
    private int productType;
    @SerializedName("chargeMode")
    private int chargeMode;
    @SerializedName("periodLength")
    private int periodLength;
    @SerializedName("isAutoExtend")
    private int isAutoExtend;
    @SerializedName("isMain")
    private int isMain;
    @SerializedName("isSubscribed")
    private int isSubscribed;
    @SerializedName("isAlacarte")
    private int isAlacarte;
    @SerializedName("isShareForAccount")
    private int isShareForAccount;
    @SerializedName("rentPeriod")
    private int rentPeriod;
    @SerializedName("priceObject")
    private PriceObject priceObject;
    @SerializedName("priceObjects")
    private List<PriceObject> priceObjects;
    @SerializedName("selectedPriceObjects")
    private List<PriceObject> selectedPriceObjects;
    @SerializedName("chooseNum")
    private int chooseNum;
    @SerializedName("residualChooseNum")
    private int residualChooseNum;
    @SerializedName("productRestriction")
    private ProductRestriction productRestriction;
    @SerializedName("picture")
    private Picture picture;
    @SerializedName("marketInfo")
    private MarketInfo marketInfo;
    @SerializedName("gstRatio")
    private String gstRatio;
    @SerializedName("gstCode")
    private String gstCode;
    @SerializedName("isEst")
    private int isEst;
    @SerializedName("isContentDownload")
    private int isContentDownload;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;
    @SerializedName("customFields")
    private List<NamedParameter> customFields;

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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(int chargeMode) {
        this.chargeMode = chargeMode;
    }

    public int getPeriodLength() {
        return periodLength;
    }

    public void setPeriodLength(int periodLength) {
        this.periodLength = periodLength;
    }

    public int getIsAutoExtend() {
        return isAutoExtend;
    }

    public void setIsAutoExtend(int isAutoExtend) {
        this.isAutoExtend = isAutoExtend;
    }

    public int getIsMain() {
        return isMain;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
    }

    public int getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(int isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public int getIsAlacarte() {
        return isAlacarte;
    }

    public void setIsAlacarte(int isAlacarte) {
        this.isAlacarte = isAlacarte;
    }

    public int getIsShareForAccount() {
        return isShareForAccount;
    }

    public void setIsShareForAccount(int isShareForAccount) {
        this.isShareForAccount = isShareForAccount;
    }

    public int getRentPeriod() {
        return rentPeriod;
    }

    public void setRentPeriod(int rentPeriod) {
        this.rentPeriod = rentPeriod;
    }

    public PriceObject getPriceObject() {
        return priceObject;
    }

    public void setPriceObject(PriceObject priceObject) {
        this.priceObject = priceObject;
    }

    public List<PriceObject> getPriceObjects() {
        return priceObjects;
    }

    public void setPriceObjects(List<PriceObject> priceObjects) {
        this.priceObjects = priceObjects;
    }

    public List<PriceObject> getSelectedPriceObjects() {
        return selectedPriceObjects;
    }

    public void setSelectedPriceObjects(List<PriceObject> selectedPriceObjects) {
        this.selectedPriceObjects = selectedPriceObjects;
    }

    public int getChooseNum() {
        return chooseNum;
    }

    public void setChooseNum(int chooseNum) {
        this.chooseNum = chooseNum;
    }

    public int getResidualChooseNum() {
        return residualChooseNum;
    }

    public void setResidualChooseNum(int residualChooseNum) {
        this.residualChooseNum = residualChooseNum;
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

    public MarketInfo getMarketInfo() {
        return marketInfo;
    }

    public void setMarketInfo(MarketInfo marketInfo) {
        this.marketInfo = marketInfo;
    }

    public String getGstRatio() {
        return gstRatio;
    }

    public void setGstRatio(String gstRatio) {
        this.gstRatio = gstRatio;
    }

    public String getGstCode() {
        return gstCode;
    }

    public void setGstCode(String gstCode) {
        this.gstCode = gstCode;
    }

    public int getIsEst() {
        return isEst;
    }

    public void setIsEst(int isEst) {
        this.isEst = isEst;
    }

    public int getIsContentDownload() {
        return isContentDownload;
    }

    public void setIsContentDownload(int isContentDownload) {
        this.isContentDownload = isContentDownload;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    public List<NamedParameter> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<NamedParameter> customFields) {
        this.customFields = customFields;
    }
}
