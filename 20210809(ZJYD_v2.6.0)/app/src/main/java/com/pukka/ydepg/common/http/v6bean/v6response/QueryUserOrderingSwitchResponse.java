package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.vss.node.ResultInfo;

//当前童锁状态查询功能
public class QueryUserOrderingSwitchResponse {

    @SerializedName("result")
    private Result result;

    @SerializedName("orderingSwitch")
    private String orderingSwitch;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getOrderingSwitch() {
        return orderingSwitch;
    }

    public void setOrderingSwitch(String orderingSwitch) {
        this.orderingSwitch = orderingSwitch;
    }
}
