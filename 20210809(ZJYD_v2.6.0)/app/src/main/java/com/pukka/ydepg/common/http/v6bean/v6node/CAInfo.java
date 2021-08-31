package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wgy on 2017/4/24.
 */

public class CAInfo {

    /**
     * Verimatrix的加密信息
     */
    @SerializedName("verimatrix")
    private Verimatrix verimatrix;

    /**
     * PlayReady的加密信息
     */
    @SerializedName("playReady")
    private PlayReady playReady;

    public Verimatrix getVerimatrix() {
        return verimatrix;
    }

    public void setVerimatrix(Verimatrix verimatrix) {
        this.verimatrix = verimatrix;
    }

    public PlayReady getPlayReady() {
        return playReady;
    }

    public void setPlayReady(PlayReady playReady) {
        this.playReady = playReady;
    }
}
