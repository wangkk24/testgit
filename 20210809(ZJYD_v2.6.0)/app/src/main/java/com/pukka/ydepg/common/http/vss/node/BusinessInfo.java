package com.pukka.ydepg.common.http.vss.node;

import com.google.gson.annotations.SerializedName;

public class BusinessInfo {

    @SerializedName("BALANCE")
    String Balance;

    @SerializedName("TOTAL_POINTS")
    String TotalScore;

    public String getBalance() {
        return Balance;
    }

    public String getTotalScore() {
        return TotalScore;
    }
}
