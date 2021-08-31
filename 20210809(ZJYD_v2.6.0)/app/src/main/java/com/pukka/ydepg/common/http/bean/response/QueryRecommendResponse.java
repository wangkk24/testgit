package com.pukka.ydepg.common.http.bean.response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;

import java.util.List;

public class QueryRecommendResponse {

    @SerializedName("totalCount")
    private int  totalCount;

    @SerializedName("count")
    private String count;

    @SerializedName("offset")
    private String offset;

    @SerializedName("VODs")
    private List<VOD> vods;

    public int  getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int  totalCount) {
        this.totalCount = totalCount;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public List<VOD> getVods() {
        return vods;
    }

    public void setVods(List<VOD> vods) {
        this.vods = vods;
    }
}
