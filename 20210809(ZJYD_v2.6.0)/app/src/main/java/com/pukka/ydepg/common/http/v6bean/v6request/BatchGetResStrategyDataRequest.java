package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * 资源位详情获取信息
 *
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6request.BatchGetResStrategyDataRequest.java
 * @author: luwm
 * @data: 2018-07-24 11:23
 * @Version V1.0 <描述当前版本功能>
 */
public class BatchGetResStrategyDataRequest {
    @SerializedName("resourceIDs")
    private List<String> resourceIDs;
    @SerializedName("userToken")
    private String userToken;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public List<String> getResourceIDs() {
        return resourceIDs;
    }

    public void setResourceIDs(List<String> resourceIDs) {
        this.resourceIDs = resourceIDs;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
