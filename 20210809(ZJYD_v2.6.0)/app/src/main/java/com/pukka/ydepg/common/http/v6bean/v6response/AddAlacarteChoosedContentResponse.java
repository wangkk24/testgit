package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.vss.node.ResultInfo;

public class AddAlacarteChoosedContentResponse {

    private final static String ERROR_COED_UNKNOWN = "E999999";

    /*
     *扣减自选包的订购关系ID
     */
    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
