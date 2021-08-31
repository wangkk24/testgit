package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: ProductRestriction.java
 * @author: yh
 * @date: 2017-04-24 15:28
 */

public class ProductRestriction {

    /**
     * net :
     * areaCode :
     * deviceModel :
     * userGroupID :
     * deviceType :
     * definition :
     */

    /**
     *网络接入方式，规则取值包括：

     1：WLAN
     2：Cellular
     */
    @SerializedName("net")
    private Rule net;

    /**
     *区域外部编号。

     */
    @SerializedName("areaCode")
    private Rule areaCode;

    /**
     *设备型号。

     */
    @SerializedName("deviceModel")
    private Rule deviceModel;

    /**
     *用户分组ID。

     */
    @SerializedName("userGroupID")
    private Rule userGroupID;

    /**
     *允许接入的SP设备类型。

     */
    @SerializedName("deviceType")
    private Rule deviceType;

    /**
     *产品Definition限制条件，取值规则为：

     0：2D+HD
     1：2D+SD
     2：3D
     3：4K
     其中，HD+3D为“0,2”， SD+3D为“1,2”。

     */
    @SerializedName("definition")
    private Rule definition;

    public Rule getNet() {
        return net;
    }

    public void setNet(Rule net) {
        this.net = net;
    }

    public Rule getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Rule areaCode) {
        this.areaCode = areaCode;
    }

    public Rule getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(Rule deviceModel) {
        this.deviceModel = deviceModel;
    }

    public Rule getUserGroupID() {
        return userGroupID;
    }

    public void setUserGroupID(Rule userGroupID) {
        this.userGroupID = userGroupID;
    }

    public Rule getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Rule deviceType) {
        this.deviceType = deviceType;
    }

    public Rule getDefinition() {
        return definition;
    }

    public void setDefinition(Rule definition) {
        this.definition = definition;
    }
}
