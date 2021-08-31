package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.bean;

import com.google.gson.annotations.SerializedName;

public class UnsubscribeTip {

    @SerializedName("offerid")
    private String offerid;

    @SerializedName("tip")
    private String tip;

    public String getOfferid() {
        return offerid;
    }

    public void setOfferid(String offerid) {
        this.offerid = offerid;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
