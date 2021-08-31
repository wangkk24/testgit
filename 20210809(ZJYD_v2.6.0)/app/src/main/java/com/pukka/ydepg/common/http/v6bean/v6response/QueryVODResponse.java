package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;

public class QueryVODResponse {
    @SerializedName("result")
    private Result result;

    @SerializedName("VODDetail")
    private VODDetail VODDetail;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public VODDetail getVODDetail() {
        return VODDetail;
    }

    public void setVODDetail(VODDetail VODDetail) {
        this.VODDetail = VODDetail;
    }
}
