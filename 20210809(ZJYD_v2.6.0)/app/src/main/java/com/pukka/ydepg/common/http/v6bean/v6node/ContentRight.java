package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: ContentRight.java
 * @author: yh
 * @date: 2017-04-24 14:26
 */

public class ContentRight implements Serializable {


    /**
     * enable : 12
     * isContentValid : 12
     * isSubscribed : 12
     * isValid : 12
     * length : 12
     * bizMediaId : 12
     * isSupportFF : 12
     * isSupportFB : 12
     * timelengthOfFFFB : 12
     * isSupportFFFBbyBookmark : 12
     * timelengthAfterPlay : 12
     */

    /**
     *业务是否支持，取值包括：

     0：不支持
     1：支持

     */
    @SerializedName("enable")
    private String enable;


    /**
     *如果enable=1，此字段必填，当前用户是否可以使用此业务，取值包括：

     0：不能使用
     1：可以使用

     */
    @SerializedName("isContentValid")
    private String isContentValid;

    /**
     *用户是否购买了业务定价的产品。

     取值范围：

     0：未订购
     1：已订购
     */
    @SerializedName("isSubscribed")
    private String isSubscribed;

    /**如果isSubscribed=1，当前用户是否可以使用此业务，取值包括：

     0：不能使用
     1：可以使用

     *
     */
    @SerializedName("isValid")
    private String isValid;

    /**
     *PLTV/CPLTV/NPVRRecord/CUTV/IR业务的录制时长，单位为秒，其中NPVRRecord取值0表示不限制。

     */
    @SerializedName("length")
    private String length;

    /**
     *表示业务实际使用的mediaId，比如对DVB频道进行NPVR录制，录制的是某个IPTV/OTT逻辑频道。仅PLTV/NPVR/CUTV支持

     */
    @SerializedName("bizMediaId")
    private String bizMediaId;

    /**
     *是否支持快进，默认值0。

     0：不支持
     1：支持
     仅CUTV支持

     */
    @SerializedName("isSupportFF")
    private String isSupportFF;

    /**
     *是否支持快退，默认值0。

     0：不支持
     1：支持
     仅CUTV支持

     */
    @SerializedName("isSupportFB")
    private String isSupportFB;

    /**
     *若不支持快进快退（当isSupportFF=0和isSupportFB=0时），则表示从片头开始多长时间内支持快进快退，单位是秒。

     仅CUTV支持

     */
    @SerializedName("timelengthOfFFFB")
    private String timelengthOfFFFB;

    /**
     *若不支持快进快退（当isSupportFF=0和isSupportFB=0时），是否支持书签以前内容快进快退，默认值0。

     0：不支持
     1：支持
     仅CUTV支持

     */
    @SerializedName("isSupportFFFBbyBookmark")
    private String isSupportFFFBbyBookmark;

    /**
     *CUTV内容播放结束后，再播放多久，单位是秒。

     仅CUTV支持

     */
    @SerializedName("timelengthAfterPlay")
    private String timelengthAfterPlay;

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getIsContentValid() {
        return isContentValid;
    }

    public void setIsContentValid(String isContentValid) {
        this.isContentValid = isContentValid;
    }

    public String getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(String isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getBizMediaId() {
        return bizMediaId;
    }

    public void setBizMediaId(String bizMediaId) {
        this.bizMediaId = bizMediaId;
    }

    public String getIsSupportFF() {
        return isSupportFF;
    }

    public void setIsSupportFF(String isSupportFF) {
        this.isSupportFF = isSupportFF;
    }

    public String getIsSupportFB() {
        return isSupportFB;
    }

    public void setIsSupportFB(String isSupportFB) {
        this.isSupportFB = isSupportFB;
    }

    public String getTimelengthOfFFFB() {
        return timelengthOfFFFB;
    }

    public void setTimelengthOfFFFB(String timelengthOfFFFB) {
        this.timelengthOfFFFB = timelengthOfFFFB;
    }

    public String getIsSupportFFFBbyBookmark() {
        return isSupportFFFBbyBookmark;
    }

    public void setIsSupportFFFBbyBookmark(String isSupportFFFBbyBookmark) {
        this.isSupportFFFBbyBookmark = isSupportFFFBbyBookmark;
    }

    public String getTimelengthAfterPlay() {
        return timelengthAfterPlay;
    }

    public void setTimelengthAfterPlay(String timelengthAfterPlay) {
        this.timelengthAfterPlay = timelengthAfterPlay;
    }
}
