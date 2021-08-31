package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: DownloadVODResponse.java
 * @author: yh
 * @date: 2017-04-25 10:56
 */

public class DownloadVODResponse extends BaseResponse{
    @SerializedName("authorizeResult")
    private AuthorizeResult authorizeResult;
    @SerializedName("downloadURLs")
    private List<String> downloadURLs;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public AuthorizeResult getAuthorizeResult() {
        return authorizeResult;
    }

    public void setAuthorizeResult(AuthorizeResult authorizeResult) {
        this.authorizeResult = authorizeResult;
    }

    public List<String> getDownloadURLs() {
        return downloadURLs;
    }

    public void setDownloadURLs(List<String> downloadURLs) {
        this.downloadURLs = downloadURLs;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
