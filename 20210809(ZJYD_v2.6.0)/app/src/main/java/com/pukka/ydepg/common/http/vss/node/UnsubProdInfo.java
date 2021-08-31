package com.pukka.ydepg.common.http.vss.node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UnsubProdInfo implements Serializable {

    private static final long serialVersionUID = -9074539857574695442L;

    /**
     * 1：一次性产品
     * 2：包周期产品（大小包场景填0）
     */
    @SerializedName("type")
    private String type;

    @SerializedName("productId")
    private String productId;

    @SerializedName("applyMode")
    private String applyMode;

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

    public String getApplyMode() {
        return applyMode;
    }

    public void setApplyMode(String applyMode) {
        this.applyMode = applyMode;
    }

    public interface UnSubProdInfoType {

        String ONCE_PRODUCT = "1";

        String PERIOD_PRODUCT = "2";

        String BIG_SMAIL_PRODUCT = "0";
    }
}
