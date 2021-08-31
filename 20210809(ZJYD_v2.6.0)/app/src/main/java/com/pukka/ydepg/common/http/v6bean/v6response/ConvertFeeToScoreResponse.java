package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.vss.node.ResultInfo;

import java.util.List;
import java.util.Map;

public class ConvertFeeToScoreResponse {

    /**
     * 换算结果
     * result.resultCode取值说明
     * 0：成功
     * 其它：失败
     */
    @SerializedName("result")
    private ResultInfo result;

    /**
     *换算积分结果，result.resultCode=0必填。
     */
    @SerializedName("score")
    private String score;

    /**
     *支付交易信息
     */
    @SerializedName("extentionInfo")
    private Map<String,String> extentionInfo;

    public ResultInfo getResult() {
        return result;
    }

    public void setResult(ResultInfo result) {
        this.result = result;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Map<String, String> getExtentionInfo() {
        return extentionInfo;
    }

    public void setExtentionInfo(Map<String, String> extentionInfo) {
        this.extentionInfo = extentionInfo;
    }
}
