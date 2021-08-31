package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wgy on 2017/4/24.
 */

public class LoginRouteRequest {

    /**
     * subscriberID :
     * disableVSPAddr :
     */

    /**
     * 订户ID，用于基于订户ID的VSP调度
     */
    @SerializedName("subscriberID")
    private String subscriberID;

    /**
     * 本轮调度禁用的VSP的 IP或者域名。
     * 如果VSP是集群部署，返回的是集群的VIP或域名。
     * 该字段的取值就是返回参数里的vspURL的VSPIP或者Domain。当终端发起LoginRoute请求后，如果EDS返回的VSP不可用，
     * 终端需要重新发送LoginRoute请求，同时请求带上上次不可用的VSP地址，以免EDS再次返回这个不可用的VSP地址。
     */
    @SerializedName("disableVSPAddr")
    private String disableVSPAddr;

    public String getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(String subscriberID) {
        this.subscriberID = subscriberID;
    }

    public String getDisableVSPAddr() {
        return disableVSPAddr;
    }

    public void setDisableVSPAddr(String disableVSPAddr) {
        this.disableVSPAddr = disableVSPAddr;
    }
}
