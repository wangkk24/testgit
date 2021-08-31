package com.pukka.ydepg.common.viewpage2;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6response.BaseResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.Recommend;

import java.util.List;

public class ViewPage2MainEpgResponse extends BaseResponse{

    @SerializedName("recommends")
    private List<Recommend> recommends;

    //轮播间隔，单位：毫秒
    @SerializedName("interval")
    private String interval;

    //滑动方向：0 水平，1 垂直
    @SerializedName("direction")
    private String direction;

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<Recommend> getRecommends() {
        return recommends;
    }

    public void setRecommends(List<Recommend> recommends) {
        this.recommends = recommends;
    }

}
