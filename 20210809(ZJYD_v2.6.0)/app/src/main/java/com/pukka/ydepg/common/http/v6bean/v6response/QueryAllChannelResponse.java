package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hasee on 2017/10/30.
 */

public class QueryAllChannelResponse extends BaseResponse{
    @SerializedName("total")
    String total;

    @SerializedName("channelDetails")
    List<ChannelDetail> channelDetails;

    @SerializedName("channelVersion")
    String channelVersion;

    @SerializedName("extensionFields")
    List<NamedParameter> extensionFields;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ChannelDetail> getChannelDetails() {
        return channelDetails;
    }

    public void setChannelDetails(List<ChannelDetail> channelDetails) {
        this.channelDetails = channelDetails;
    }

    public String getChannelVersion() {
        return channelVersion;
    }

    public void setChannelVersion(String channelVersion) {
        this.channelVersion = channelVersion;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
