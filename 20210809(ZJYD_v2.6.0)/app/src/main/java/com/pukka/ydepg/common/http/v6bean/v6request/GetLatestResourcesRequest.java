package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * 获取发生变更的资源位列表
 *
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6request.GetLatestResourcesRequest.java
 * @author: luwm
 * @data: 2018-07-24 11:31
 * @Version V1.0 <描述当前版本功能>
 */
public class GetLatestResourcesRequest {
    @SerializedName("version")
    private String version;
    @SerializedName("desktopID")
    private String desktopID;
    @SerializedName("userToken")
    private String userToken;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDesktopID() {
        return desktopID;
    }

    public void setDesktopID(String desktopID) {
        this.desktopID = desktopID;
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
