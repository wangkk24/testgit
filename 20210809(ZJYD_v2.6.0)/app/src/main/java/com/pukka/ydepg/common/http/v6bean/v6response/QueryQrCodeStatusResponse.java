package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6response.QueryQrCodeStatusResponse.java
 * @author:xj
 * @date: 2018-01-09 16:51
 */

public class QueryQrCodeStatusResponse extends BaseResponse{
    @SerializedName("status")
    private String status;
    @SerializedName("scannedSubscriberId")
    private String scannedSubscriberId;
    @SerializedName("validTime")
    private String validTime;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScannedSubscriberId() {
        return scannedSubscriberId;
    }

    public void setScannedSubscriberId(String scannedSubscriberId) {
        this.scannedSubscriberId = scannedSubscriberId;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    /**
     * 二维码认证状态。
     * 取值包括：
     * •WAITING: 等待中
     * •SCANNED: 扫描
     * •SUCCESSED: 成功
     * •FAILED: 失败
     */
    public  interface Status {
        String WAITING = "WAITING";
        String SCANNED = "SCANNED";
        String SUCCESSED = "SUCCESSED";
        String FAILED = "FAILED";
    }
}
