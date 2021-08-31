package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */

public class ReportQrCodeStateRequest {
    /*
    *二维码信息
        {
        "loginName ": "**** ",
        "subscriberId ": "**** ",
        "deviceId': "**** ",
        "bindCode ": "**** "
        }
    *
    *
    */
    @SerializedName("QRCode")
    private String QRCode;

    /*
    *   当前二维码认证状态：
        SCANNED
        SUCCESSED
        FAILED

    */
    @SerializedName("currentStatus")
    private String currentStatus;

    /*
    *
    *扩展信息
    */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
