package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuerySTBOrderInfoResponse {
    /*
     *订购场景
     * 0:会员升级；1：会员订购；2：青铜/白银会员权益充足；3.非会员未订购
     */
    @SerializedName("sceneType")
    private String sceneType;

    /*
     *订购提示语
     */
    @SerializedName("tips")
    private String tips;

    /*
     *弹窗左侧按钮名称，例升级、确定、订购
     */
    @SerializedName("botton")
    private String botton;

    /*
     *升级场景展示的已订购的产品id，sceneType为0时必传
     */
    @SerializedName("subProduct")
    private String subProduct;

    /*
     *用户已订购的产品列表
     */
    @SerializedName("subProductList")
    private List<String> subProductList;

    /*
     *场景对应的可订购产品列表
     */
    @SerializedName("productList")
    private List<String> productList;

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getBotton() {
        return botton;
    }

    public void setBotton(String botton) {
        this.botton = botton;
    }

    public String getSubProduct() {
        return subProduct;
    }

    public void setSubProduct(String subProduct) {
        this.subProduct = subProduct;
    }

    public List<String> getSubProductList() {
        return subProductList;
    }

    public void setSubProductList(List<String> subProductList) {
        this.subProductList = subProductList;
    }

    public List<String> getProductList() {
        return productList;
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }
}
