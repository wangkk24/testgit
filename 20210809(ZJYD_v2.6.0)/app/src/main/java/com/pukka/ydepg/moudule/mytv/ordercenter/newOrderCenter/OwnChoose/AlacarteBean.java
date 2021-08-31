package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.OwnChoose;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlacarteBean {

    @SerializedName("alacarteInfo")
    private List<AlacarteInfo> alacarteInfo;

    public List<AlacarteInfo> getAlacarteInfo() {
        return alacarteInfo;
    }

    public void setAlacarteInfo(List<AlacarteInfo> alacarteInfo) {
        this.alacarteInfo = alacarteInfo;
    }
}
