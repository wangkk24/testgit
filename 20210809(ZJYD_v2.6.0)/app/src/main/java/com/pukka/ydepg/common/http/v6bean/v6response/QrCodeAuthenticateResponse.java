package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6response.QrCodeAuthenticateResponse.java
 * @author:xj
 * @date: 2018-01-09 14:53
 */

public class QrCodeAuthenticateResponse extends BaseResponse{
    @SerializedName("QRCode")
    private String qrCode;
    @SerializedName("validTime")
    private String validTime;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getQrCode() {
        return qrCode;
    }

    public String getValidTime() {
        return validTime;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
