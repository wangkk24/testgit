package com.pukka.ydepg.common.http.v6bean.v6node.ssp;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertUser;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.Device;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.DistributionChannel;

import java.util.List;

public class QueryAdvertContentRequest {

    /**
     * 媒体编码_当前时间毫秒数_10随机数
     * OTT_EPG_1551283200000_2736546789
     */
    @SerializedName("reqId")
    private String reqId;

    //租户的编码
    @SerializedName("tenantId")
    private String tenantId;

    //在SSP平台创建的推广位编码，支持有多个推广位
    @SerializedName("placementCodes")
    private List<String> placementCodes;

    //分发渠道
    @SerializedName("channel")
    private DistributionChannel channel;

    @SerializedName("user")
    private AdvertUser user;

    @SerializedName("device")
    private Device device;

    //当前时间的时间戳，毫秒数
    @SerializedName("timestamp")
    private Long timestamp;




    public String getReqId()
    {
        return reqId;
    }

    public void setReqId(String reqId)
    {
        this.reqId = reqId;
    }

    public String getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }

    public List<String> getPlacementCodes()
    {
        return placementCodes;
    }

    public void setPlacementCodes(List<String> placementCodes) {
        this.placementCodes = placementCodes;
    }

    public DistributionChannel getChannel() {
        return channel;
    }

    public void setChannel(DistributionChannel channel) {
        this.channel = channel;
    }

    public AdvertUser getUser()
    {
        return user;
    }

    public void setUser(AdvertUser user)
    {
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append("\n\treqId          = ").append(reqId)
                .append("\n\ttenantId       = ").append(tenantId)
                .append("\n\tuser           = ").append(user)
                .append("\n\tplacementCodes = ").append(placementCodes)
                .append("\n\tchannel >>>      ").append(channel)
                .append("\n\tdevice         = ").append(device)
                .append("\n\ttimestamp      = ").append(timestamp)
                .toString();
    }
}