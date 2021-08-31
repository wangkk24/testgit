package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PlayVODHeartbeatRequest.java
 * @author: yh
 * @date: 2017-04-25 10:59
 */

public class PlayVODHeartbeatRequest {

    /**
     * VODID :
     * mediaID :
     * extensionFields : [""]
     */

    /**
     * VOD的ID。

     */
    @SerializedName("VODID")
    private String VODID;

    /**
     * 播放的媒资ID。

     */
    @SerializedName("mediaID")
    private String mediaID;

    /**
     * 扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getVODID() {
        return VODID;
    }

    public void setVODID(String VODID) {
        this.VODID = VODID;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
