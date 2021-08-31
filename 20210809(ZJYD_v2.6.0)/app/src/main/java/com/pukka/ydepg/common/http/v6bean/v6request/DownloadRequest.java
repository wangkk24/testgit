package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.CheckLock;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */

public class DownloadRequest {
    @SerializedName("VODID")
    private String VODID;
    @SerializedName("episodeIDs")
    private List<String> episodeIDs;
    @SerializedName("mediaIDs")
    private List<String> mediaIDs;
    @SerializedName("checkLock")
    private CheckLock checkLock;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getVODID() {
        return VODID;
    }

    public void setVODID(String VODID) {
        this.VODID = VODID;
    }

    public List<String> getEpisodeIDs() {
        return episodeIDs;
    }

    public void setEpisodeIDs(List<String> episodeIDs) {
        this.episodeIDs = episodeIDs;
    }

    public List<String> getMediaIDs() {
        return mediaIDs;
    }

    public void setMediaIDs(List<String> mediaIDs) {
        this.mediaIDs = mediaIDs;
    }

    public CheckLock getCheckLock() {
        return checkLock;
    }

    public void setCheckLock(CheckLock checkLock) {
        this.checkLock = checkLock;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
