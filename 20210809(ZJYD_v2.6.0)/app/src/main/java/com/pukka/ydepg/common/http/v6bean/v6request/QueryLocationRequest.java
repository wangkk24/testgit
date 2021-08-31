package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6request.QueryLocationRequest.java
 * @author: yh
 * @date: 2017-09-06 09:10
 */

public class QueryLocationRequest {

    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    public QueryLocationRequest() {
    }
}
