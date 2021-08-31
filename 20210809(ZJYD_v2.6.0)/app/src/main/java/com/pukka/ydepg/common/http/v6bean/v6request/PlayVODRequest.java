package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.CheckLock;
import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PlayVODRequest.java
 * @author: yh
 * @date: 2017-04-25 10:52
 */

public class PlayVODRequest {

    /**
     * VODID :
     * mediaID :
     * seriesID :
     * checkLock :
     * URLFormat :
     * isReturnProduct :
     * sortType :
     * playSessionKey :
     * extensionFields :
     */

    /**
     *播放的VOD的ID，如果播放子集的话，传子集的VOD ID。

     如果传入的VODID是连续剧父集ID，mediaID只能是父集片花的媒资ID。

     */
    @SerializedName("VODID")
    private String VODID;

    /**
     *播放的VOD媒资ID，可以是正片媒资ID或者是片花媒资ID。

     */
    @SerializedName("mediaID")
    private String mediaID;

    /**
     *如果播放的是子集，传入子集对应的连续剧父集ID，用于连续剧书签的返回。

     */
    @SerializedName("seriesID")
    private String seriesID;

    /**
     *检查VOD是否被加锁和父母字控制，或者如果加锁或父母字控制，需要进行密码校验。如果终端不传，表示平台不需要做加锁和父母字检查，以及解锁功能。

     */
    @SerializedName("checkLock")
    private CheckLock checkLock;

    /**
     *指定返回的URL格式，取值包括：

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
     默认值为1。

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

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public String getSeriesID() {
        return seriesID;
    }

    public void setSeriesID(String seriesID) {
        this.seriesID = seriesID;
    }

    public CheckLock getCheckLock() {
        return checkLock;
    }

    public void setCheckLock(CheckLock checkLock) {
        this.checkLock = checkLock;
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
}
