package com.pukka.ydepg.common.http.vss.node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SubProdInfo implements Serializable {

    private static final long serialVersionUID = -7825231853229962543L;

    /**
     * 产品类型
     * 1.一次性产品
     * 2.包周期产品
     */
    @SerializedName("type")
    private String type;

    /**
     * 产品外部ID
     */
    @SerializedName("productId")
    private String productId;

    /**
     * 订购数量
     * 注：不填（当前版本文档批注）
     */
    @SerializedName("amount")
    private String amount;
    /**
     * 自动续订标识
     * =0：非自动续订
     * =1或null 自动续订
     */
    @SerializedName("renewFlag")
    private String renewFlag;

    /**
     * 产品权益列表
     * 包括：内容、媒资、特性、栏目
     * 注：按次产品订购时，订购主键(userID+ secondaryObj（type.Id)组成部分
     * 如果为一次性产品则必填
     */
    @SerializedName("object")
    private PriceObject object;

    /**
     * 扩展字段
     */
    @SerializedName("extensionInfo")
    private Map<String,String> extensionInfo;

    public interface SubProdInfoType {

        /**
         * 大小包产品
         */
        String SUP_SUB_PRODUCT = "0";

        /**
         * 一次性产品
         */
        String ONCE_PRODUCT = "1";

        /**
         * 包周期产品
         */
        String PERIOD_PRODUCT = "2";

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRenewFlag() {
        return renewFlag;
    }

    public void setRenewFlag(String renewFlag) {
        this.renewFlag = renewFlag;
    }

    public PriceObject getObject() {
        return object;
    }

    public void setObject(PriceObject object) {
        this.object = object;
    }

    public Map<String, String> getExtensionInfo() {
        return extensionInfo;
    }

    public void setExtensionInfo(Map<String, String> extensionInfo) {
        this.extensionInfo = extensionInfo;
    }
}
