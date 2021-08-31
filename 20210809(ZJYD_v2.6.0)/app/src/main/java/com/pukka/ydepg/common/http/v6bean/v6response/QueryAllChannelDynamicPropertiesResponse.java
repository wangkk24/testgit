package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDynamicProperties;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hasee on 2017/11/6.
 */

public class QueryAllChannelDynamicPropertiesResponse extends BaseResponse{

    @SerializedName("total")
    String total;

    @SerializedName("channelDynamaicProp")
    List<ChannelDynamicProperties> channelDynamaicProp;

    @SerializedName("extensionFields")
    List<NamedParameter> extensionFields;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ChannelDynamicProperties> getChannelDynamaicProp() {
        return channelDynamaicProp;
    }

    public void setChannelDynamaicProp(List<ChannelDynamicProperties> channelDynamaicProp) {
        this.channelDynamaicProp = channelDynamaicProp;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
