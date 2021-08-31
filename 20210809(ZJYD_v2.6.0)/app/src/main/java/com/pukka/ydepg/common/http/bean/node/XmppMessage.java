package com.pukka.ydepg.common.http.bean.node;

import java.io.Serializable;

/**
 * @author liudong Email :liudong@easier.cn
 * @desc
 * @date 2018/1/31.
 */

public class XmppMessage implements Serializable {

    private String mediaCode;

    private  String mediaType;

    private  String playByBookMark;

    private  String playByTime;

    private  String actionSource;

    private String delay;

    private String platform;

    private String videoType;

    private String playUrl;




    public String getMediaCode() {
        return mediaCode;
    }

    public void setMediaCode(String mediaCode) {
        this.mediaCode = mediaCode;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getPlayByBookMark() {
        return playByBookMark;
    }

    public void setPlayByBookMark(String playByBookMark) {
        this.playByBookMark = playByBookMark;
    }

    public String getPlayByTime() {
        return playByTime;
    }

    public void setPlayByTime(String playByTime) {
        this.playByTime = playByTime;
    }

    public String getActionSource() {
        return actionSource;
    }

    public void setActionSource(String actionSource) {
        this.actionSource = actionSource;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

}
