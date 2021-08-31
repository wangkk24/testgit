package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.SingleMark;

import java.util.List;

public class GetSTBSingleMarksResponse {
    //单包角标信息
    @SerializedName("singleMarks")
    private List<SingleMark> singleMarks;


    public List<SingleMark> getSingleMarks() {
        return singleMarks;
    }

    public void setSingleMarks(List<SingleMark> singleMarks) {
        this.singleMarks = singleMarks;
    }
}
