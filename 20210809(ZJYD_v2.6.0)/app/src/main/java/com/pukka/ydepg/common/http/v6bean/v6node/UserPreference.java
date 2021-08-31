package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: UserPreference.java
 * @author: yh
 * @date: 2017-04-24 11:32
 */

public class UserPreference {


    /**
     * 用户偏爱的内容ID。

     */
    @SerializedName("preferContentIDs")
    private List<String> preferContentIDs;

    /**
     * 用户不知道是否喜欢的内容ID。

     */
    @SerializedName("unknownContentIDs")
    private List<String> unknownContentIDs;

    /**
     * 用户不喜欢的内容ID。

     */
    @SerializedName("dislikeContentIDs")
    private List<String> dislikeContentIDs;

    public List<String> getPreferContentIDs() {
        return preferContentIDs;
    }

    public void setPreferContentIDs(List<String> preferContentIDs) {
        this.preferContentIDs = preferContentIDs;
    }

    public List<String> getUnknownContentIDs() {
        return unknownContentIDs;
    }

    public void setUnknownContentIDs(List<String> unknownContentIDs) {
        this.unknownContentIDs = unknownContentIDs;
    }

    public List<String> getDislikeContentIDs() {
        return dislikeContentIDs;
    }

    public void setDislikeContentIDs(List<String> dislikeContentIDs) {
        this.dislikeContentIDs = dislikeContentIDs;
    }
}
