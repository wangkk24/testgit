package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wgy on 2017/4/24.
 */

public class StartPicture {

    /**
     * 资源名称
     */
    @SerializedName("name")
    private String name;

    /**
     * 资源类型
     * 0：图片
     * 1：视频
     */
    @SerializedName("type")
    private String type;

    /**
     * vod资源ID
     */
    @SerializedName("vodID")
    private String vodID;

    /**
     * vod资源媒资ID
     */
    @SerializedName("mediaID")
    private String mediaID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVodID() {
        return vodID;
    }

    public void setVodID(String vodID) {
        this.vodID = vodID;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }
}
