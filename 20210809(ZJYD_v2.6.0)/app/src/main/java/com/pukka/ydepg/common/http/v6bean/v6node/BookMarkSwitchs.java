package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookMarkSwitchs implements Serializable {

    public static final String START = "START";
    public static final String SWITCH = "SWITCH";
    public static final String PAUSE = "PAUSE";
    public static final String REPLAY = "REPLAY";
    public static final String FASTFORWARD = "FASTFORWARD";
    public static final String FASTBACK = "FASTBACK";
    public static final String QUIT = "QUIT";
    public static final String DESTORY = "QUIT";


    @SerializedName("start")
    private String start;

    @SerializedName("switch")
    private String switchs;

    @SerializedName("pause")
    private String pause;

    @SerializedName("replay")
    private String replay;

    @SerializedName("fastForward")
    private String fastForward;

    @SerializedName("FastBack")
    private String FastBack;

    @SerializedName("quit")
    private String quit;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getSwitchs() {
        return switchs;
    }

    public void setSwitchs(String switchs) {
        this.switchs = switchs;
    }

    public String getPause() {
        return pause;
    }

    public void setPause(String pause) {
        this.pause = pause;
    }

    public String getReplay() {
        return replay;
    }

    public void setReplay(String replay) {
        this.replay = replay;
    }

    public String getFastForward() {
        return fastForward;
    }

    public void setFastForward(String fastForward) {
        this.fastForward = fastForward;
    }

    public String getFastBack() {
        return FastBack;
    }

    public void setFastBack(String fastBack) {
        FastBack = fastBack;
    }

    public String getQuit() {
        return quit;
    }

    public void setQuit(String quit) {
        this.quit = quit;
    }

    public String getBookmarkSwitchsValue(String type){
        switch(type){
            case START:
                return start;
            case SWITCH:
                return switchs;
            case PAUSE:
                return pause;
            case REPLAY:
                return replay;
            case FASTFORWARD:
                return fastForward;
            case FASTBACK:
                return FastBack;
            case QUIT:
                return quit;
                default:
                    return null;
        }
    }
}
