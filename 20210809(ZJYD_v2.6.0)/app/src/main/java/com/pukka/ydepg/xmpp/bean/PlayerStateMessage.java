package com.pukka.ydepg.xmpp.bean;

public class PlayerStateMessage {
    //1必选参数
    String respondAction = "getAppState";
    //2必选参数
    String stateType     = "playerState";
    //3必选参数,是否处于播放状态. 0:没有播放 1:正在播放
    int    playBackState;
    //4可选参数,播放器ID,处于播放状态才需要返回【当前不返回】
    int    playerInstance;
    //5可选参数,播放状态 0:正在播放 1:暂停 2:快进/快退
    int    trickPlayMode;
    //6可选参数,快进快退倍速,仅trickPlayMode=2快进快退时才需要返回
    int    fastSpeed;
    //7可选参数,当前播放位置,仅单播类节目(不含时移)正在播放时才需要返回
    long   playPosition;
    //8可选参数,播放类型,仅处于播放状态时才需要返回(正在时移时也返回频道类型)
    String mediaType;
    //9可选参数,播放内容的ID(外部资源ID,即VOD对象中的code字段)
    String mediaCode;
    //10可选参数,如播放为频道则需要返回此参数
    int    chanKey;
    //11可选参数,媒体总时长.当媒体类型mediaType=TYPE_VOD/TYPE_TVOD/TYPE_LOCAL需要返回此参数
    long   duration;
    //12播放地址
    String playUrl;

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

    public int getPlayBackState() {
        return playBackState;
    }

    public void setPlayBackState(int playBackState) {
        this.playBackState = playBackState;
    }

    public int getPlayerInstance() {
        return playerInstance;
    }

    public void setPlayerInstance(int playerInstance) {
        this.playerInstance = playerInstance;
    }

    public int getTrickPlayMode() {
        return trickPlayMode;
    }

    public void setTrickPlayMode(int trickPlayMode) {
        this.trickPlayMode = trickPlayMode;
    }

    public int getFastSpeed() {
        return fastSpeed;
    }

    public void setFastSpeed(int fastSpeed) {
        this.fastSpeed = fastSpeed;
    }

    public long getPlayPosition() {
        return playPosition;
    }

    public void setPlayPosition(long playPosition) {
        this.playPosition = playPosition;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaCode() {
        return mediaCode;
    }

    public void setMediaCode(String mediaCode) {
        this.mediaCode = mediaCode;
    }

    public int getChanKey() {
        return chanKey;
    }

    public void setChanKey(int chanKey) {
        this.chanKey = chanKey;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }
}
