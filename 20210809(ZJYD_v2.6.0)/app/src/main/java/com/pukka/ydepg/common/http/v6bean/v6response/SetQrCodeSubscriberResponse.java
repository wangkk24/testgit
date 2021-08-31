package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *设置当前二维码认证用户
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6response.SetQrCodeSubscriberResponse.java
 * @author:xj
 * @date: 2018-01-23 09:35
 */

public class SetQrCodeSubscriberResponse {


    @SerializedName("result")
    Result result;
    @SerializedName("extensionFields")
    List<NamedParameter> extensionFields;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
