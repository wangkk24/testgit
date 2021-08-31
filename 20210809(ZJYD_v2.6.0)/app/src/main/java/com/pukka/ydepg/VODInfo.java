package com.pukka.ydepg;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VODInfo implements Serializable {

    @SerializedName("vid")
    private String vid;

    @SerializedName("playState")
    private int playState;

    @SerializedName("dstVid")
    private String dstVid;

    @SerializedName("videoFromUrl")
    private String videoFromUrl;

    @SerializedName("videoUrl")
    private String videoUrl;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public int getPlayState() {
        return playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }

    public String getDstVid() {
        return dstVid;
    }

    public void setDstVid(String dstVid) {
        this.dstVid = dstVid;
    }

    public String getVideoFromUrl() {
        return videoFromUrl;
    }

    public void setVideoFromUrl(String videoFromUrl) {
        this.videoFromUrl = videoFromUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
