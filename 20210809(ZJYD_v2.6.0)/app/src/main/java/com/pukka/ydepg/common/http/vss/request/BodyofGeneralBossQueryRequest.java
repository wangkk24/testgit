package com.pukka.ydepg.common.http.vss.request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.vss.node.BusiInfo;

public class BodyofGeneralBossQueryRequest {
    @SerializedName("BUSI_INFO")
    BusiInfo busiInfo;

    public BusiInfo getBusiInfo() {
        return busiInfo;
    }

    public void setBusiInfo(BusiInfo busiInfo) {
        this.busiInfo = busiInfo;
    }
}
