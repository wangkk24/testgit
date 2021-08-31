package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: DeviceType.java
 * @author: yh
 * @date: 2017-04-24 12:52
 */

public class DeviceType implements Serializable {


    /**
     * deviceType :
     * deviceTypeName : 123
     * type : 123
     */

    /**
     * 设备类型
     */
    @SerializedName("deviceType")
    private String deviceType;

    /**
     * 设备类型名称。

     */
    @SerializedName("deviceTypeName")
    private String deviceTypeName;

    /**
     * 设备类型所属CP/SP，取值包括：

     0：CP分组
     1：SP分组

     */
    @SerializedName("type")
    private String type;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
