package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * ----Created By----
 * User: Fang Liang.
 * Date: 2019/1/9.
 * ------------------
 */

public class GetRelatedContentRequest {
    @SerializedName("contentID")
    private String contentID;
    @SerializedName("contentType")
    private String contentType;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public GetRelatedContentRequest(String contentID, String contentType, List<NamedParameter> extensionFields) {
        this.contentID = contentID;
        this.contentType = contentType;
        this.extensionFields = extensionFields;
    }

    public GetRelatedContentRequest() {
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
