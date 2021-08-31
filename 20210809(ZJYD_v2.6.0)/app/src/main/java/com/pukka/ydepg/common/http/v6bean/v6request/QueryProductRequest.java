package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liudo on 2018/5/8.
 */

public class QueryProductRequest {

    /**
     * ALL: 查询所有产品
     * UNORDERED: 查询所有未订购产品
     * BY_IDLIST: 根据ID列表查询
     */
    @SerializedName("queryType")
    private String queryType;


    @SerializedName("productIds")
    private List<String> productIds;
    /**
     * 产品类型。
     * 取值包括：
     * ORDER_BY_CYCLE: 包周期
     * PPV: 按次
     * ALL: 全部
     */
    @SerializedName("productType")
    private String productType;
    @SerializedName("count")
    private int count;
    @SerializedName("offset")
    private int offset;
    /**
     * 是否查询基础包。
     * 取值包括：
     * NO: 非基础包
     * YES: 基础包
     * ALL: 所有
     */
    @SerializedName("isMain")
    private String isMain;
    /**
     * 是否返回不支持在线订购的产品。
     * 取值包括：
     * NO: 不返回
     * YES: 返回
     */
    @SerializedName("isEnableOnlinePurchase")
    private String isEnableOnlinePurchase;

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    public String getIsEnableOnlinePurchase() {
        return isEnableOnlinePurchase;
    }

    public void setIsEnableOnlinePurchase(String isEnableOnlinePurchase) {
        this.isEnableOnlinePurchase = isEnableOnlinePurchase;
    }

    public interface QueryType {
        public String ALL = "ALL";
        public String UNORDERED = "UNORDERED";
        public String BY_IDLIST = "BY_IDLIST";
    }

}
