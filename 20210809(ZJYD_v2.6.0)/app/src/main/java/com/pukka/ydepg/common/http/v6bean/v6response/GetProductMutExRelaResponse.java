package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.SubMutex;
import com.pukka.ydepg.common.http.vss.node.ResultInfo;

import java.util.List;

public class GetProductMutExRelaResponse {

    //订购产品
    @SerializedName("result")
    private ResultInfo result;

    //订购产品
    @SerializedName("subMutexs")
    private List<SubMutex> subMutexs;

    public ResultInfo getResult() {
        return result;
    }

    public void setResult(ResultInfo result) {
        this.result = result;
    }

    public List<SubMutex> getSubMutexs() {
        return subMutexs;
    }

    public void setSubMutexs(List<SubMutex> subMutexs) {
        this.subMutexs = subMutexs;
    }
}
