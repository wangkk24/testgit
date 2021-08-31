package com.pukka.ydepg.common.http.bean.request;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.PriceObject;
import com.google.gson.annotations.SerializedName;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 */
public class CancelSubscribeRequest {
    private static final String TAG = "CancelSubscribeRequest";

    @SerializedName("cancelSubscribe")
    private CancelSubscibe cancelSubscribe;

    @SerializedName("extensionFields")
    private NamedParameter[] extensionFields;

    public static class CancelSubscibe{
        @SerializedName("productID")
        private String productID;
        @SerializedName("cancelType")
        private int cancelType;
//        @SerializedName("startTime")
//        private long startTime;
//        @SerializedName("deviceID")
//        private String deviceID;
//        @SerializedName("extensionFields")
//        private NamedParameter[] extensionFields;
//        @SerializedName("priceObject")
//        private PriceObject priceObject;

//        public CancelSubscibe(String productID,long startTime,int cancelType,PriceObject priceObject,NamedParameter[] extensionFields) {
//            this.productID = productID;
//            this.startTime=startTime;
//            this.cancelType = cancelType;
//            this.extensionFields=extensionFields;
//            if(null!=priceObject){
//                this.priceObject=priceObject;
//            }
//        }

        public CancelSubscibe(String productID,int cancelType) {
            this.productID = productID;
            this.cancelType = cancelType;
        }

        public String getProductID() {
            return productID;
        }

        public int getCancelType() {
            return cancelType;
        }

//        public long getStartTime() {
//            return startTime;
//        }
//
//        public String getDeviceID() {
//            return deviceID;
//        }
//
//        public NamedParameter[] getExtensionFields() {
//            return extensionFields;
//        }
//
//        public PriceObject getPriceObject() {
//            return priceObject;
//        }
    }

    public CancelSubscribeRequest(CancelSubscibe cancelSubscribe,
        NamedParameter[] extensionFields) {
        this.cancelSubscribe = cancelSubscribe;
        this.extensionFields = extensionFields;
    }
    public CancelSubscribeRequest(CancelSubscibe cancelSubscribe) {
        this.cancelSubscribe = cancelSubscribe;
    }

    public CancelSubscibe getCancelSubscribe() {
        return cancelSubscribe;
    }

    public NamedParameter[] getExtensionFields() {
        return extensionFields;
    }
}