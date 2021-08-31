package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

/**
 * 终端上报设备信息
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6request.SubmitDeviceInfoRequest.java
 * @author:xj
 * @date: 2018-01-30 10:12
 */

public class SubmitDeviceInfoRequest {
    /**
     *设备类型。
     *Push server根据该字段，建立不同的消息通道。比如IOS，则建立APNS消息通道。
     *取值包括：
     *•IOS: iOS系统设备
     *•ANDROID: android系统设备
     *•WP: Windows Phone系统设备（后续扩展用）
     *•HUAWEI: 华为自建推送通道
     */
    @SerializedName("deviceType")
    private  String deviceType;
    /**
     * 如果是使用第三方消息推送平台，则填写第三方消息推送平台（APNS/GCM/FCM/MPNS）的token
     *如果是IMP平台，则填写XMPP的JID
     */
    @SerializedName("deviceToken")
    private  String deviceToken;

    /**
     *    设备应用ID，对iOS设备有效。
     */
    @SerializedName("terminalAppID")
    private  String  terminalAppID;


    /**
     *  从PUSH Server获取的XMPP token的过期时间，取值为距离1970年1月1号的毫秒数。
     */
    @SerializedName("tokenExpireTime")
    private String tokenExpireTime;


    /**
     *  从第三方消息服务系统获取的备用Token，对于代理模式，deviceToken表示第三方系统分配的主用Token，两者用途不同。
     */
    @SerializedName("relayDeviceToken")
    private String  relayDeviceToken;


    /**
     *  备用设备类型参数，和relayDeviceToken成对出现。
     */
    @SerializedName("relayDeviceType")
    private  String relayDeviceType;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getTerminalAppID() {
        return terminalAppID;
    }

    public void setTerminalAppID(String terminalAppID) {
        this.terminalAppID = terminalAppID;
    }

    public String getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(String tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }

    public String getRelayDeviceToken() {
        return relayDeviceToken;
    }

    public void setRelayDeviceToken(String relayDeviceToken) {
        this.relayDeviceToken = relayDeviceToken;
    }

    public String getRelayDeviceType() {
        return relayDeviceType;
    }

    public void setRelayDeviceType(String relayDeviceType) {
        this.relayDeviceType = relayDeviceType;
    }
}
