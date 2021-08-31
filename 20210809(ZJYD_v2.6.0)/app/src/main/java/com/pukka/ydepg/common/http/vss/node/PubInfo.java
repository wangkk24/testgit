package com.pukka.ydepg.common.http.vss.node;

import com.google.gson.annotations.SerializedName;

public class PubInfo {
    @SerializedName("RETURN_RESULT")
    String RETURN_RESULT;

    @SerializedName("RETURN_DESC")
    String RETURN_DESC;

    public String getRETURN_RESULT() {
        return RETURN_RESULT;
    }

    public String getRETURN_DESC() {
        return RETURN_DESC;
    }
}
