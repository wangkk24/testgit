package com.pukka.ydepg.common.http.v6bean.v6Entity;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6Entity.FavroitesData.java
 * @date: 2018-02-13 16:51
 * @version: V1.0 描述当前版本功能
 */


public class FavroitesData {
    private Long id;//数据库主键

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 视频名称
     */
    private String name;

    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 前一个节目名称
     */
    private String preProName;

    /**
     * 视当前节目名称
     */
    private String currentProName;

    /**
     * 下一个节目名称
     */
    private String nextProName;

    /**
     * 当前节目开始时间
     */
    private String currentProStartTime;

    /**
     * 当前节目结束时间
     */
    private String currentProEndTime;


    /**
     * 下一个节目开始时间
     */
    private String nextProStartTime;

    /**
     * 0:VOD
     * 1:Channel
     */
    private String type;
    /**
     *添加时间
     */
    private String updatetime;

    private String profileID;


    public FavroitesData() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPreProName() {
        return this.preProName;
    }

    public void setPreProName(String preProName) {
        this.preProName = preProName;
    }

    public String getCurrentProName() {
        return this.currentProName;
    }

    public void setCurrentProName(String currentProName) {
        this.currentProName = currentProName;
    }

    public String getNextProName() {
        return this.nextProName;
    }

    public void setNextProName(String nextProName) {
        this.nextProName = nextProName;
    }

    public String getCurrentProStartTime() {
        return this.currentProStartTime;
    }

    public void setCurrentProStartTime(String currentProStartTime) {
        this.currentProStartTime = currentProStartTime;
    }

    public String getCurrentProEndTime() {
        return this.currentProEndTime;
    }

    public void setCurrentProEndTime(String currentProEndTime) {
        this.currentProEndTime = currentProEndTime;
    }

    public String getNextProStartTime() {
        return this.nextProStartTime;
    }

    public void setNextProStartTime(String nextProStartTime) {
        this.nextProStartTime = nextProStartTime;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdatetime() {
        return this.updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getProfileID() {
        return this.profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }


}

