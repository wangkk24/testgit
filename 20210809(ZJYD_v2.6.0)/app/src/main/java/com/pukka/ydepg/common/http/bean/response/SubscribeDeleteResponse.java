package com.pukka.ydepg.common.http.bean.response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6response.BaseResponse;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: SubscribeDeleteResponse.java
 * @author: yh
 * @date: 2016-11-15 14:24
 */

public class SubscribeDeleteResponse extends BaseResponse{
    private static final String TAG = "SubscribeDeleteResponse";
    @SerializedName("originInfo")
    private NamedParameter[] originInfo;
    @SerializedName("extensionFields")
    private NamedParameter[] extensionFields;

    public NamedParameter[] getOriginInfo() {
        return originInfo;
    }

    public void setOriginInfo(NamedParameter[] originInfo) {
        this.originInfo = originInfo;
    }

    public NamedParameter[] getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(NamedParameter[] extensionFields) {
        this.extensionFields = extensionFields;
    }
}