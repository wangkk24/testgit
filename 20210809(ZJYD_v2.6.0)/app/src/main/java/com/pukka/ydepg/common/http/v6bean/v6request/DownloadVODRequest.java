package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.CheckLock;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: DownloadVODRequest.java
 * @author: yh
 * @date: 2017-04-25 10:57
 */

public class DownloadVODRequest {


    /**
     * VODID :
     * episodeIDs : ["",""]
     * mediaIDs : ["",""]
     * checkLock :
     */

    /**
     * VOD的ID，可是普通VOD、连续剧父集或VOD子集。
     */
    @SerializedName("VODID")
    private String VODID;

    /**
     * 检查VOD是否被加锁和父母字控制，或者如果加锁或父母字控制，需要进行密码校验。
     * 如果终端不传，表示VSP不需要做加锁和父母字检查，以及解锁功能。
     */
    @SerializedName("checkLock")
    private CheckLock checkLock;

    /**
     * 如果VODID是连续剧父集，表示待下载的子集ID，且必须携带。
     */
    @SerializedName("episodeIDs")
    private List<String> episodeIDs;

    /**
     *待下载的VOD媒资ID。
     * 说明
     * 如果VODID是普通VOD或者VOD子集，只支持传入一个媒资ID。否则，必须传入待下载的VOD子集的媒资ID，episodeIDs和mediaIDs是一对一关系。
     */
    @SerializedName("mediaIDs")
    private List<String> mediaIDs;

    private transient String postURL;
    private transient String switchNum;
    private transient String voddetailId;
    private transient String name;

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

    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public String getSwitchNum() {
        return switchNum;
    }

    public void setSwitchNum(String switchNum) {
        this.switchNum = switchNum;
    }

    public String getVoddetailId() {
        return voddetailId;
    }

    public void setVoddetailId(String voddetailId) {
        this.voddetailId = voddetailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DownloadVODRequest{" +
                "VODID='" + VODID + '\'' +
                ", checkLock=" + checkLock +
                ", episodeIDs=" + episodeIDs +
                ", mediaIDs=" + mediaIDs +
                ", postURL='" + postURL + '\'' +
                ", switchNum='" + switchNum + '\'' +
                ", voddetailId='" + voddetailId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
