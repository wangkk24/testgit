package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hasee on 2017/10/30.
 */

public class QueryAllChannelRequest {
    @SerializedName("channelNamespace")
    String channelNamespace;

    @SerializedName("extensionFields")
    List<NamedParameter> extensionFields;

    @SerializedName("userFilter")
    String userFilter;

    public Integer getIsReturnAllMedia() {
        return isReturnAllMedia;
    }

    public void setIsReturnAllMedia(Integer isReturnAllMedia) {
        this.isReturnAllMedia = isReturnAllMedia;
    }

    @SerializedName("isReturnAllMedia")
    Integer isReturnAllMedia;

    public String getChannelNamespace() {
        return channelNamespace;
    }

    public void setChannelNamespace(String channelNamespace) {
        this.channelNamespace = channelNamespace;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    public String getUserFilter() {
        return userFilter;
    }

    public void setUserFilter(String userFilter) {
        this.userFilter = userFilter;
    }
}
