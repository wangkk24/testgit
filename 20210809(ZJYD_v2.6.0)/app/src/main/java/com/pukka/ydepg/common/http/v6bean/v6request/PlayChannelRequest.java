package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.CheckLock;
import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PlayChannelRequest.java
 * @author: yh
 * @date: 2017-04-25 10:11
 */

public class PlayChannelRequest {


    /**
     * channelID :
     * playbillID :
     * mediaID :
     * businessType :
     * checkLock :
     * isReturnURL :
     * URLFormat :
     * isReturnProduct :
     * sortType :
     * playSessionKey :
     * extensionFields :
     */

    /**
     *频道ID。

     */
    @SerializedName("channelID")
    private String channelID;


    /**
     *节目单ID。

     如果播放直播业务，终端未上报节目单ID，平台查询当前时间的直播节目单，如果存在直播节目单，平台进行节目单的授权检查。

     */
    @SerializedName("playbillID")
    private String playbillID;

    /**
     *播放的媒资ID。

     */
    @SerializedName("mediaID")
    private String mediaID;

    /**
     *鉴权的业务类型，取值包括：

     BTV：直播
     PLTV：网络时移
     CUTV：回看
     PREVIEW：直播预览
     IR：即时重播

     */
    @SerializedName("businessType")
    private String businessType;

    /**
     *检查频道或节目单是否被加锁和父母字控制，或者如果加锁或父母字控制，需要进行密码校验。

     如果终端不传，表示平台不需要做加锁和父母字检查，以及解锁功能。

     说明：
     如果是直播业务，当存在直播节目单，平台进行直播节目单的锁和父母字检查，否则进行频道的锁和父母字检查。
     如果非直播业务，如果终端传入了节目单，平台进行节目单的锁和父母字检查，否则进行频道的锁和父母字检查。
     */
    @SerializedName("checkLock")
    private CheckLock checkLock;

    /**
     *如果内容已订购，是否返回播放URL，取值包括：

     0：不返回
     1：返回
     默认值为1。

     说明：
     终端在某些场景仅需要做鉴权，而不需要返回播放URL，比如直播节目单切换时。

     */
    @SerializedName("isReturnURL")
    private String isReturnURL;

    /**
     *如果需要返回播放URL，指定返回的URL格式，取值包括：

     0：默认格式
     1：转换成HLS协议
     默认值是0。

     */
    @SerializedName("URLFormat")
    private String URLFormat;

    /**
     *如果内容未订购是否返回内容定价的产品，取值包括：

     0：不返回
     1：返回
     默认值是1。

     */
    @SerializedName("isReturnProduct")
    private String isReturnProduct;

    /**
     *如果返回产品，指定产品的排序方式：

     PRICE:ASC：按产品价格升序排序
     PRICE:DESC：按产品价格降序排序
     默认值是PRICE:ASC。

     */
    @SerializedName("sortType")
    private String sortType;

    /**
     *如果因为播放会话数超限导致播放失败，用户可以选择中止其他会话的播放行为。

     */
    @SerializedName("playSessionKey")
    private String playSessionKey;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
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

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public CheckLock getCheckLock() {
        return checkLock;
    }

    public void setCheckLock(CheckLock checkLock) {
        this.checkLock = checkLock;
    }

    public String getIsReturnURL() {
        return isReturnURL;
    }

    public void setIsReturnURL(String isReturnURL) {
        this.isReturnURL = isReturnURL;
    }

    public String getURLFormat() {
        return URLFormat;
    }

    public void setURLFormat(String URLFormat) {
        this.URLFormat = URLFormat;
    }

    public String getIsReturnProduct() {
        return isReturnProduct;
    }

    public void setIsReturnProduct(String isReturnProduct) {
        this.isReturnProduct = isReturnProduct;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getPlaySessionKey() {
        return playSessionKey;
    }

    public void setPlaySessionKey(String playSessionKey) {
        this.playSessionKey = playSessionKey;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
