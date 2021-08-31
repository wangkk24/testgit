package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PlaySession.java
 * @author: yh
 * @date: 2017-04-21 17:32
 */

public class PlaySession {


    /**
     * sessionKey :
     * profileID :
     * deviceID :
     */

    /**
     *播放会话主键。

     */
    @SerializedName("sessionKey")
    private String sessionKey;

    /**
     *播放会话的ProfileID。

     */
    @SerializedName("profileID")
    private String profileID;

    /**
     *播放会话的逻辑设备ID。
     */
    @SerializedName("deviceID")
    private String deviceID;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
