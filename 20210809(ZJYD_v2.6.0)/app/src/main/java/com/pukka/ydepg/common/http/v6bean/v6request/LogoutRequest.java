package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class LogoutRequest {

    /**
     * type :
     * deviceStatus :
     * physicalDeviceId :
     * extensionFields :
     */

    /**
     *退出类型。取值范围：

     0：用户注销
     1：用户退出，类似PC的关机
     默认值为0。

     */
    @SerializedName("type")
    private String type;

    /**
     *设备状态，取值包括：

     0：离线
     1：在线
     2：待机
     3：休眠
     默认值为0。

     说明：
     DT STB才需要2和3

     */
    @SerializedName("deviceStatus")
    private String deviceStatus;

    /**
     *当Type=1且deviceStatus=0时，通知指定的物理设备已下线。

     对于STB设备，此参数为MAC地址。
     对于OTT设备，此参数为VMX插件生成的clientId。
     */
    @SerializedName("physicalDeviceId")
    private String physicalDeviceId;

    /**
     *扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getPhysicalDeviceId() {
        return physicalDeviceId;
    }

    public void setPhysicalDeviceId(String physicalDeviceId) {
        this.physicalDeviceId = physicalDeviceId;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
