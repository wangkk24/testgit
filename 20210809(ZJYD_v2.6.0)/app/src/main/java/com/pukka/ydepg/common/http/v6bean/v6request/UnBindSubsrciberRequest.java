package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6request.UnBindSubsrciberRequest.java
 * @author:xj
 * @date: 2018-01-10 11:29
 */

public class UnBindSubsrciberRequest {
    @SerializedName("targetSubscriberId")
    private String targetSubscriberId;

    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getTargetSubscriberId() {
        return targetSubscriberId;
    }

    public void setTargetSubscriberId(String targetSubscriberId) {
        this.targetSubscriberId = targetSubscriberId;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
