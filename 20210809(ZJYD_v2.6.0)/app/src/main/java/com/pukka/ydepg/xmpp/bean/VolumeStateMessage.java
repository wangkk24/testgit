package com.pukka.ydepg.xmpp.bean;

public class VolumeStateMessage {

    //必选参数
    String respondAction = "getAppState";
    //必选参数
    String stateType     = "playerVolume";
    //必选参数,是否处于播放状态. 0:没有播放 1:正在播放
    String currentVolume = "mute";

    public String getRespondAction() {
        return respondAction;
    }

    public void setRespondAction(String respondAction) {
        this.respondAction = respondAction;
    }

    public String getStateType() {
        return stateType;
    }

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }

    public String getCurrentVolume() {
        return currentVolume;
    }

    public void setCurrentVolume(String currentVolume) {
        this.currentVolume = currentVolume;
    }
}
