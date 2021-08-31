package com.pukka.ydepg.common.http.vss.response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.vss.node.RespParam;

public class BodyofGeneralBossQueryResponse {

    @SerializedName("RESP_PARAM")
    RespParam RESP_PARAM;

    public RespParam getRESP_PARAM() {
        return RESP_PARAM;
    }


}
