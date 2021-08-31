package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: ReportChannelRequest.java
 * @author: yh
 * @date: 2017-04-25 10:42
 */

public class ReportChannelRequest {

    /**
     * action :
     * playSessionKey :
     * playerInstanceId :
     * channelID :
     * nextChannelID :
     * playbillID :
     * mediaID :
     * nextMediaID :
     * businessType :
     * isDownload :
     * productID :
     * subjectID :
     * extensionFields :
     */

    public static final int ACTION_PLAY = 0;
    public static final int ACTION_STOP = 1;
    public static final int ACTION_SWITCH = 2;
    public static final int ACTION_TSTV = 3;

    /**
     *播放行为，取值包括：

     0：开始播放
     1：退出播放
     2：切换频道
     3：直播和时移切换
     */
    @SerializedName("action")
    private int action;

    /**
     *播放会话ID，在入参action为1退出播放时候传入。

     */
    @SerializedName("playSessionKey")
    private String playSessionKey;

    /**
     *在多画面播放的时候，每个播放窗口需要不同的实例ID。

     */
    @SerializedName("playerInstanceId")
    private int playerInstanceId;

    /**
     *播放/切换前的频道ID。

     */
    @SerializedName("channelID")
    private String channelID;

    /**
     *如果action=2，表示切换后的频道ID。

     */
    @SerializedName("nextChannelID")
    private String nextChannelID;

    /**
     *如果播放回看节目单，表示节目单ID。

     */
    @SerializedName("playbillID")
    private String playbillID;

    /**
     *播放/切换前的媒资ID。

     */
    @SerializedName("mediaID")
    private String mediaID;

    /**
     *如果action=2，表示切换后的媒资ID。

     */
    @SerializedName("nextMediaID")
    private String nextMediaID;

    /**
     *鉴权的业务类型，取值包括：

     BTV：直播
     PLTV：网络时移
     CUTV：回看
     */
    @SerializedName("businessType")
    private String businessType;

    /**
     *如果播放回看，是否播放本地已下载的内容，取值包括：

     0：否
     1：是
     默认值为0。

     */
    @SerializedName("isDownload")
    private int isDownload;

    /**
     *鉴权通过的产品ID。

     */
    @SerializedName("productID")
    private String productID;

    /**
     *如果从指定的栏目进入频道，携带栏目ID，用于Reporter按栏目统计用户行为。

     */
    @SerializedName("subjectID")
    private String subjectID;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getPlaySessionKey() {
        return playSessionKey;
    }

    public void setPlaySessionKey(String playSessionKey) {
        this.playSessionKey = playSessionKey;
    }

    public int getPlayerInstanceId() {
        return playerInstanceId;
    }

    public void setPlayerInstanceId(int playerInstanceId) {
        this.playerInstanceId = playerInstanceId;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getNextChannelID() {
        return nextChannelID;
    }

    public void setNextChannelID(String nextChannelID) {
        this.nextChannelID = nextChannelID;
    }

    public String getPlaybillID() {
        return playbillID;
    }

    public void setPlaybillID(String playbillID) {
        this.playbillID = playbillID;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public String getNextMediaID() {
        return nextMediaID;
    }

    public void setNextMediaID(String nextMediaID) {
        this.nextMediaID = nextMediaID;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public int getIsDownload() {
        return isDownload;
    }

    public void setIsDownload(int isDownload) {
        this.isDownload = isDownload;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
