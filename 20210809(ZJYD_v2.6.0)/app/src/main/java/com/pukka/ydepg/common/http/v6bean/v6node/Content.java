package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: Content.java
 * @author: yh
 * @date: 2017-04-24 15:03
 */

public class Content {

    public static final String VIDEO_VOD = "VIDEO_VOD";//视频VOD
    public static final String AUDIO_VOD = "AUDIO_VOD";//音频VOD
    public static final String VIDEO_CHANNEL = "VIDEO_CHANNEL";//视频频道
    public static final String AUDIO_CHANNEL = "AUDIO_CHANNEL";//音频频道
    public static final String PROGRAM = "PROGRAM";//节目单
    public static final String SUBJECT = "SUBJECT";//栏目

    /**
     * contentType :
     * VOD : 12
     * channel : 12
     * subject : 12
     * playbill : 12
     */

    /**
     *内容类型，取值包括：

     VIDEO_VOD：视频VOD
     AUDIO_VOD：音频VOD
     VIDEO_CHANNEL：视频频道
     AUDIO_CHANNEL：音频频道
     PROGRAM：节目单
     SUBJECT：栏目

     */
    @SerializedName("contentType")
    private String contentType;

    /**
     *VOD内容描述，contentType等于VIDEO_VOD和AUDIO_VOD时返回。

     */
    @SerializedName("VOD")
    private VOD VOD;

    /**
     *channel内容描述。contentType等于

     VIDEO_CHANNEL和AUDIO_CHANNEL时返回。

     */
    @SerializedName("channel")
    private Channel channel;

    /**
     *栏目内容描述，contentType等于SUBJECT时返回。

     */
    @SerializedName("subject")
    private Subject subject;

    /**
     *program内容描述，contentType等于PROGRAM时返回。

     */
    @SerializedName("playbill")
    private Playbill playbill;
    /**
     *内容搜索趋势信息。
     *取值包括：
     *•0: 与上次持平；
     *•1: 上升；
     *•2: 下降;
     */
    @SerializedName("tendency")
    private String tendency;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public com.pukka.ydepg.common.http.v6bean.v6node.VOD getVOD() {
        return VOD;
    }

    public void setVOD(com.pukka.ydepg.common.http.v6bean.v6node.VOD VOD) {
        this.VOD = VOD;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Playbill getPlaybill() {
        return playbill;
    }

    public void setPlaybill(Playbill playbill) {
        this.playbill = playbill;
    }

    public String getTendency() {
        return tendency;
    }

    public void setTendency(String tendency) {
        this.tendency = tendency;
    }

    /**
     * the range of field contentType
     */
    public interface ContentType
    {
        String VIDEO_VOD = "VIDEO_VOD";
        String AUDIO_VOD = "AUDIO_VOD";
        String VIDEO_CHANNEL = "VIDEO_CHANNEL";
        String AUDIO_CHANNEL = "AUDIO_CHANNEL";
        String PROGRAM = "PROGRAM";
        String SUBJECT = "SUBJECT";
    }
}
