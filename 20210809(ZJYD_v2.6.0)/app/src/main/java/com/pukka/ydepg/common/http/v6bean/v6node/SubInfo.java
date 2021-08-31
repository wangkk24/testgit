package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/5/19.
 */

public class SubInfo {
    /**
     * code : 000000
     * description : ok
     * subInfoList : {"contentID":"","productID":"111111","productName":"好莱坞20元包月","productType":"1","startTime":"20161220211919","endTime":"20170119235959","buyMode":"1","fee":"2000"}
     */

    @SerializedName("code")
    private String code;

    @SerializedName("description")
    private String description;

    /**
     * 订购列表
     */
    @SerializedName("subInfoList")
    private List<SubInfoListBean> subInfoList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SubInfoListBean> getSubInfoList() {
        return subInfoList;
    }

    public void setSubInfoList(List<SubInfoListBean> subInfoList) {
        this.subInfoList = subInfoList;
    }

    public static class SubInfoListBean {
        /**
         * contentID :
         * productID : 111111
         * productName : 好莱坞20元包月
         * productType : 1
         * startTime : 20161220211919
         * endTime : 20170119235959
         * buyMode : 1
         * fee : 2000
         */

        /**
         * 内容ID，按次产品必填
         */
        @SerializedName("contentID")
        private String contentID;

        /**
         * 产品ID
         */
        @SerializedName("productID")
        private String productID;

        /**
         * 产品名称
         */
        @SerializedName("productName")
        private String productName;

        /**
         * 产品类型名称
         * 0基本包
         * 1增值产品
         */
        @SerializedName("productType")
        private String productType;

        /**
         * 订购开始时间
         */
        @SerializedName("startTime")
        private String startTime;

        /**
         * 订购结束时间
         */
        @SerializedName("endTime")
        private String endTime;

        /**
         * 订购方式名称
         * 1：EPG订购
         * 2：MEM订购
         * 3：CRM订购
         */
        @SerializedName("buyMode")
        private String buyMode;

        /**
         * 包月费用（分）
         */
        @SerializedName("fee")
        private String fee;

        public String getContentID() {
            return contentID;
        }

        public void setContentID(String contentID) {
            this.contentID = contentID;
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

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
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

        public String getBuyMode() {
            return buyMode;
        }

        public void setBuyMode(String buyMode) {
            this.buyMode = buyMode;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }
    }
}
