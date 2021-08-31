package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlacarteChoosedContent {

    /*
     *订购关系ID
     */
    @SerializedName("subscriptionID")
    private String subscriptionID;

    /*
     *订购用户
     */
    @SerializedName("userID")
    private String userID;

    /*
     *自选包产品ID
     *如果非自选包产品，则请求拒绝
     */
    @SerializedName("productID")
    private String productID;

    /*
     *自选包自选内容数
     */
    @SerializedName("chooseNum")
    private int chooseNum;

    /*
     *自选包自选内容单周期剩余数
     */
    @SerializedName("residualChooseNum")
    private int residualChooseNum;

    /*
     *当前添加的内容
     *如果不传参或者参数为空，则查询所有的已添加且当前时间有效的内容
     *如果传参且不为空，则只查询已添加且有效的该内容
     */
    @SerializedName("choosedContents")
    private List<ChoosedContent> choosedContents;

    public String getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public List<ChoosedContent> getChoosedContents() {
        return choosedContents;
    }

    public void setChoosedContents(List<ChoosedContent> choosedContents) {
        this.choosedContents = choosedContents;
    }
}
