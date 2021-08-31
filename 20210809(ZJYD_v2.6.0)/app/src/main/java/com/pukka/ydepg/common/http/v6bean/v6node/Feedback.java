package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Feedback implements Serializable {

    @SerializedName("position")
    private String position;

    @SerializedName("click_tracker")
    private String click_tracker;

    @SerializedName("play_tracker")
    private String play_tracker;

    @SerializedName("ZJextend1")
    private String ZJextend1;

    @SerializedName("ZJextend2")
    private String ZJextend2;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getClick_tracker() {
        return click_tracker;
    }

    public void setClick_tracker(String click_tracker) {
        this.click_tracker = click_tracker;
    }

    public String getPlay_tracker() {
        return play_tracker;
    }

    public void setPlay_tracker(String play_tracker) {
        this.play_tracker = play_tracker;
    }

    public String getZJextend1() {
        return ZJextend1;
    }

    public void setZJextend1(String ZJextend1) {
        this.ZJextend1 = ZJextend1;
    }

    public String getZJextend2() {
        return ZJextend2;
    }

    public void setZJextend2(String ZJextend2) {
        this.ZJextend2 = ZJextend2;
    }
}
