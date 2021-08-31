package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wgy on 2017/4/24.
 */

public class LoginRequest {

    /**
     * 订户ID
     */
    @SerializedName("subscriberID")
    private String subscriberID;

    /**
     * 终端型号
     */
    @SerializedName("deviceModel")
    private String deviceModel;
    public String getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(String subscriberID) {
        this.subscriberID = subscriberID;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

}
