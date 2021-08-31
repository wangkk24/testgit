package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.AlacarteChoosedContent;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.vss.node.ResultInfo;

import java.util.List;

public class GetAlacarteChoosedContentsResponse {
    /*
     *返回结果
     */
    @SerializedName("result")
    private Result result;

    /*
     *返回的自选内容信息
     */
    @SerializedName("alacarteChoosedContents")
    private List<AlacarteChoosedContent> alacarteChoosedContents;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<AlacarteChoosedContent> getAlacarteChoosedContents() {
        return alacarteChoosedContents;
    }

    public void setAlacarteChoosedContents(List<AlacarteChoosedContent> alacarteChoosedContents) {
        this.alacarteChoosedContents = alacarteChoosedContents;
    }
}
