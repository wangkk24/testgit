package com.pukka.ydepg.moudule.voice;

/**
 * @author liudong Email :liudong@easier.cn
 * @desc
 * @date 2018/1/30.
 */
public class VoiceEvent {

    public static final String SELECT = "select";

    public static final String NEXT   = "next";

    public static final String PREV   = "prev";

    public static final String SWITCH_CHANNEL = "switch_channel";

    private String action;

    private String channelId;

    private String mediaId;

    public VoiceEvent(String action){
        this.action=action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}