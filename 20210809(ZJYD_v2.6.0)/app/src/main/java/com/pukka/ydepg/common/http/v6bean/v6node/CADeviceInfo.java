package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: CADeviceInfo.java
 * @author: yh
 * @date: 2017-04-20 12:23
 */

public class CADeviceInfo {

    /**
     * CADeviceType : CADeviceType
     * CADeviceID : CADeviceID
     * CADeviceIDSignature : CADeviceIDSignature
     * extensionFields :
     */

    /**
     * CA设备类型。

     取值范围：

     1：DTV 硬卡STB
     2：DTV 软卡STB
     3：IPTV STB
     4：OTT STB
     5：HLS OTT client
     6：Playready OTT client
     7：Widevine OTT client

     */
    @SerializedName("CADeviceType")
    private String CADeviceType;

    /**
     * CA设备编号。

     对于非高安STB，取值是STB的MAC地址。
     对于高安STB，取值是STB的chipsetId。

     */
    @SerializedName("CADeviceID")
    private String CADeviceID;

    /**
     * 设备ID签名。

     该参数不为空，即表示终端和VSP平台进行双向认证，该参数用于传输ChipsetID签名。

     */
    @SerializedName("CADeviceIDSignature")
    private String CADeviceIDSignature;

    /**
     * 扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getCADeviceType() {
        return CADeviceType;
    }

    public void setCADeviceType(String CADeviceType) {
        this.CADeviceType = CADeviceType;
    }

    public String getCADeviceID() {
        return CADeviceID;
    }

    public void setCADeviceID(String CADeviceID) {
        this.CADeviceID = CADeviceID;
    }

    public String getCADeviceIDSignature() {
        return CADeviceIDSignature;
    }

    public void setCADeviceIDSignature(String CADeviceIDSignature) {
        this.CADeviceIDSignature = CADeviceIDSignature;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
