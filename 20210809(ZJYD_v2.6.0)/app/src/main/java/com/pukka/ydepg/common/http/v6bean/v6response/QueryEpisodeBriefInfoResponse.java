package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;

import java.util.List;

public class QueryEpisodeBriefInfoResponse {

    @SerializedName("result")
    private Result result;

    /*
     *子集号
     */
    @SerializedName("sitcomNOs")
    private List<String> sitcomNOs;

    /*
     *子集的预发布状态。
     *取值包括：
     *0: 非预发布VOD
     *1: 预发布VOD
     */
    @SerializedName("preReleases")
    private int[] preReleases;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<String> getSitcomNOs() {
        return sitcomNOs;
    }

    public void setSitcomNOs(List<String> sitcomNOs) {
        this.sitcomNOs = sitcomNOs;
    }

    public int[] getPreReleases() {
        return preReleases;
    }

    public void setPreReleases(int[] preReleases) {
        this.preReleases = preReleases;
    }
}
