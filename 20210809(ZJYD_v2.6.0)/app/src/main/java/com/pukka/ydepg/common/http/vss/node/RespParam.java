package com.pukka.ydepg.common.http.vss.node;

import com.google.gson.annotations.SerializedName;

public class RespParam {
    @SerializedName("BUSI_INFO")
    BusinessInfo BusinessInfo;

    @SerializedName("PUB_INFO")
    PubInfo PubInfo;

    public com.pukka.ydepg.common.http.vss.node.BusinessInfo getBusinessInfo() {
        return BusinessInfo;
    }

    public com.pukka.ydepg.common.http.vss.node.PubInfo getPubInfo() {
        return PubInfo;
    }
}
