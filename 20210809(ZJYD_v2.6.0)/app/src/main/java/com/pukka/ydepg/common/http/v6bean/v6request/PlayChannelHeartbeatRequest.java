package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PlayChannelHeartbeatRequest.java
 * @author: yh
 * @date: 2017-04-25 10:45
 */

public class PlayChannelHeartbeatRequest {


    /**
     * channelID :
     * playbillID :
     * mediaID :
     * playSessionKey :
     * playerInstanceId :
     * extensionFields :
     */

    /**
     *频道ID。

     */
    @SerializedName("channelID")
    private String channelID;

    /**
     *如果播放回看节目单，表示节目单ID。

     */
    @SerializedName("playbillID")
    private String playbillID;

    /**
     *播放的媒资ID。

     */
    @SerializedName("mediaID")
    private String mediaID;

    /**
     *播放会话主键

     */
    @SerializedName("playSessionKey")
    private String playSessionKey;

    /**
     *播放实例ID，用于一个终端同时支持多个播放会话的场景。

     */
    @SerializedName("playerInstanceId")
    private String playerInstanceId;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getPlaybillID() {
        return playbillID;
    }

    public void setPlaybillID(String playbillID) {
        this.playbillID = playbillID;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public String getPlaySessionKey() {
        return playSessionKey;
    }

    public void setPlaySessionKey(String playSessionKey) {
        this.playSessionKey = playSessionKey;
    }

    public String getPlayerInstanceId() {
        return playerInstanceId;
    }

    public void setPlayerInstanceId(String playerInstanceId) {
        this.playerInstanceId = playerInstanceId;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
