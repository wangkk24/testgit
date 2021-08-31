package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 设置当前二维码认证用户
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6request.SetQrCodeSubscriberRequest.java
 * @author:xj
 * @date: 2018-01-23 09:35
 */

public class SetQrCodeSubscriberRequest {
    @SerializedName("qrCodeAuthenticatedSubscriberId")
    String qrCodeAuthenticatedSubscriberId;

    @SerializedName("extensionFields")
    List<NamedParameter> extensionFields;

    public String getQrCodeAuthenticatedSubscriberId() {
        return qrCodeAuthenticatedSubscriberId;
    }

    public void setQrCodeAuthenticatedSubscriberId(String qrCodeAuthenticatedSubscriberId) {
        this.qrCodeAuthenticatedSubscriberId = qrCodeAuthenticatedSubscriberId;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
