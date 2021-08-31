package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;

import java.util.List;

public class QueryChannelStcPropsBySubjectResponse extends BaseResponse{
    @SerializedName("total")
    String total;

    @SerializedName("channelDetails")
    List<ChannelDetail> channelDetails;

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
}
