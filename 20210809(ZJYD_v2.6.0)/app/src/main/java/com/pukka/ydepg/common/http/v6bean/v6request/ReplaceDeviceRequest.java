package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * Created by wgy on 2017/4/27.
 */

public class ReplaceDeviceRequest {

    /**
     * subscriberID :
     * orgDeviceID :
     * destDeviceID :
     * extensionFields :
     */

    /**
     * 订户ID
     */
    @SerializedName("subscriberID")
    private String subscriberID;

    /**
     * 旧设备的逻辑设备ID，来自Device对象的deviceId属性
     */
    @SerializedName("orgDeviceID")
    private String orgDeviceID;

    /**
     * 目标设备的物理地址，取值包括：
     * 对于STB终端，取值为设备的MAC地址。
     * 对于OTT设备，取值为CA客户端插件的caDeviceId，如VMX插件生成的clientId。
     */
    @SerializedName("destDeviceID")
    private String destDeviceID;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(String subscriberID) {
        this.subscriberID = subscriberID;
    }

    public String getOrgDeviceID() {
        return orgDeviceID;
    }

    public void setOrgDeviceID(String orgDeviceID) {
        this.orgDeviceID = orgDeviceID;
    }

    public String getDestDeviceID() {
        return destDeviceID;
    }

    public void setDestDeviceID(String destDeviceID) {
        this.destDeviceID = destDeviceID;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
