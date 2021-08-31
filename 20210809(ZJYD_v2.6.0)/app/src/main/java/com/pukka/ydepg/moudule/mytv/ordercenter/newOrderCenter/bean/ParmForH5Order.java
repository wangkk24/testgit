package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParmForH5Order {

    //用户标识
    @SerializedName("userID")
    private String userID;

    //渠道ID （必填，默认001，可以是CPID+APK标识）华为EPG传值002
    @SerializedName("channelId")
    private String channelId = "002";

    //0链接 1能力端方法，如果为1,传参JSON值不用加双引号"",（必填）
    @SerializedName("backType")
    private String backType = "1";

    //订购前置页面（1详情页 2播放页 3订购中心，4订购 华为EPG必填）
    @SerializedName("frontpage")
    private String frontpage;

    //回跳方法（订购完成或返回的链接与方法）（必填）
    @SerializedName("backWay")
    private String backWay;

    //产品信息,跳转订购用这个
    @SerializedName("product")
    private List<ProductForH5Order> product;

    //产品信息,跳转支付用这个
    @SerializedName("productId")
    private String productId;
    @SerializedName("productType")
    private String productType;
    @SerializedName("contentId")
    private String contentId;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getBackType() {
        return backType;
    }

    public void setBackType(String backType) {
        this.backType = backType;
    }

    public String getFrontpage() {
        return frontpage;
    }

    public void setFrontpage(String frontpage) {
        this.frontpage = frontpage;
    }

    public String getBackWay() {
        return backWay;
    }

    public void setBackWay(String backWay) {
        this.backWay = backWay;
    }

    public List<ProductForH5Order> getProduct() {
        return product;
    }

    public void setProduct(List<ProductForH5Order> product) {
        this.product = product;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
}
