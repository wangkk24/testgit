package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.huawei.chfjydvideo.common.http.v6bean.v6node.Playbill.java
 * @author: yh
 * @date: 2017-04-24 10:22
 */

public class Playbill extends Metadata {


    /**
     * ID :
     * channel : Channel
     * name :
     * introduce :
     * startTime : String
     * endTime : String
     * rating : Rating
     * picture : Picture
     * isCUTV : 123
     * genres : ["Genre"]
     * CUTVStatus : 123
     * playbillSeries : PlaybillSeries
     * isLocked : 123
     * bookmark : String
     * reminder : Reminder
     * playTimes : String
     * isFillProgram : 12
     * isNPVR : 12
     * isCPVR : 12
     * hasRecordingPVR : 12
     * isInstantRestart : 21
     * isBlackout : 12
     */
//
//    /**
//     *节目的内部唯一标识。
//
//     */
//    @SerializedName("ID")
//    private String ID;

    /**
     *第三方系统分配的内容Code。
     */
    @SerializedName("code")
    protected String code;

    /**
     *节目所属的频道。

     */
    @SerializedName("channel")
    private Channel channel;

//    /**
//     *内容名称。
//
//     填充节目单时，填充为空字符。
//
//     终端侧根据需要展示自定义的字段。
//
//     */
//    @SerializedName("name")
//    private String name;

    /**
     *内容简介。

     */
    @SerializedName("introduce")
    private String introduce;

    /**
     *节目开始时间。

     取值为距离1970年1月1号的毫秒数。

     */
    @SerializedName("startTime")
    private String startTime;

    /**
     *节目结束时间点。

     取值为距离1970年1月1号的毫秒数

     */
    @SerializedName("endTime")
    private String endTime;

    /**
     *观看级别。

     填充节目单时，填充为最低级别（有业务需要时，可向平台要求配置）。

     */
    @SerializedName("rating")
    private Rating rating;
//
//    /**
//     *节目海报路径。
//
//     */
//    @SerializedName("picture")
//    private Picture picture;

    /**
     *是否支持录播。

     取值范围：

     0：不支持
     1：支持
     */
    @SerializedName("isCUTV")
    private String isCUTV;

    /**
     *录制状态，取值包括：

     0：录制失败
     1：录制成功且有效
     对于支持录制的直播节目单，平台会返回录制成功，不支持录制的直播节目单，返回录制失败。

     */
    @SerializedName("CUTVStatus")
    private String CUTVStatus;

    /**
     *连续剧节目单属性。

     */
    @SerializedName("playbillSeries")
    private PlaybillSeries playbillSeries;

    /**
     *标识内容是否被加锁。

     取值范围：

     1：已加锁
     0：未加锁
     */
    @SerializedName("isLocked")
    private String isLocked;

    /**
     *书签观看的断点时间。

     单位：秒。

     说明：
     仅录制成功的CUTV节目单添加书签后才有该参数。

     */
    @SerializedName("bookmark")
    private String bookmark;

    /**
     *如果节目单添加提醒，返回提醒记录。

     */
    @SerializedName("reminder")
    private Reminder reminder;

    /**
     *节目观看次数。

     说明：
     当前获取录播排行总榜单的数据。

     */
    @SerializedName("playTimes")
    private String playTimes;

    /**
     *当前节目单是否为填充节目单。

     0：普通节目单。
     1：填充节目单
     2：填充节目单，表示平台有普通的节目单，但是由于被过滤不能返回给终端，方便呈现，节目单里面的取值都按照填充节目单的数值进行赋值。
     默认为普通节目单。

     */
    @SerializedName("isFillProgram")
    private String isFillProgram;

    /**
     *是否支持网络录制。

     取值范围：

     0：不支持
     1：支持
     */
    @SerializedName("isNPVR")
    private String isNPVR;

    /**
     *是否支持本地录制

     取值范围：

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("isCPVR")
    private String isCPVR;

    /**
     *节目单是否关联了录制中的PVR录制计划，取值包括：

     0：不关联
     1：关联节目单录制计划
     2：关联系列录制子计划

     */
    @SerializedName("hasRecordingPVR")
    private String hasRecordingPVR;

    /**
     *是否支持InstantRestart。

     取值范围：

     0：不支持
     1：支持
     */
    @SerializedName("isInstantRestart")
    private String isInstantRestart;

    /**
     *是否blackout，取值包括：

     0：不支持

     1：支持

     默认值为1。

     */
    @SerializedName("isBlackout")
    private String isBlackout;

//    /**
//     *流派信息。
//
//     */
//    @SerializedName("genres")
//    private List<Genre> genres;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public String getIsCUTV() {
        return isCUTV;
    }

    public void setIsCUTV(String isCUTV) {
        this.isCUTV = isCUTV;
    }

    public String getCUTVStatus() {
        return CUTVStatus;
    }

    public void setCUTVStatus(String CUTVStatus) {
        this.CUTVStatus = CUTVStatus;
    }

    public PlaybillSeries getPlaybillSeries() {
        return playbillSeries;
    }

    public void setPlaybillSeries(PlaybillSeries playbillSeries) {
        this.playbillSeries = playbillSeries;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    public String getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(String playTimes) {
        this.playTimes = playTimes;
    }

    public String getIsFillProgram() {
        return isFillProgram;
    }

    public void setIsFillProgram(String isFillProgram) {
        this.isFillProgram = isFillProgram;
    }

    public String getIsNPVR() {
        return isNPVR;
    }

    public void setIsNPVR(String isNPVR) {
        this.isNPVR = isNPVR;
    }

    public String getIsCPVR() {
        return isCPVR;
    }

    public void setIsCPVR(String isCPVR) {
        this.isCPVR = isCPVR;
    }

    public String getHasRecordingPVR() {
        return hasRecordingPVR;
    }

    public void setHasRecordingPVR(String hasRecordingPVR) {
        this.hasRecordingPVR = hasRecordingPVR;
    }

    public String getIsInstantRestart() {
        return isInstantRestart;
    }

    public void setIsInstantRestart(String isInstantRestart) {
        this.isInstantRestart = isInstantRestart;
    }

    public String getIsBlackout() {
        return isBlackout;
    }

    public void setIsBlackout(String isBlackout) {
        this.isBlackout = isBlackout;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
