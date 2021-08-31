package com.pukka.ydepg.common.http.vss.response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.vss.node.BusiInfo;

import java.io.Serializable;

public class QueryResultBalResponse implements Serializable {
    @SerializedName("code")
    String code;

    @SerializedName("bodyofGeneralBossQueryResponse")
    BodyofGeneralBossQueryResponse bodyofGeneralBossQueryResponse;

    @SerializedName("description")
    String description;

    public BodyofGeneralBossQueryResponse getBodyofGeneralBossQueryResponse() {
        return bodyofGeneralBossQueryResponse;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
