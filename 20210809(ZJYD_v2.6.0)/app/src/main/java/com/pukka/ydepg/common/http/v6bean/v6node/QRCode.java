package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/6/21.
 */

public class QRCode {
    /*
     {
            "loginName ": "**** ",
            "subscriberId ": "**** ",
            "deviceId': "**** ",
            "bindCode ": "**** "
      }

     */

    @SerializedName("loginName")
    private String loginName;


    @SerializedName("subscriberId")
    private String subscriberId;

    @SerializedName("deviceId")
    private String deviceId;

    @SerializedName("bindCode")
    private String bindCode;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getBindCode() {
        return bindCode;
    }

    public void setBindCode(String bindCode) {
        this.bindCode = bindCode;
    }
}
