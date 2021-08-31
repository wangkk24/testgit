package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: ChannelFilter.java
 * @author: yh
 * @date: 2017-04-24 15:10
 */

public class ChannelFilter {

    /**
     * genres : 12
     * subscriptionTypes : 12
     * isFavorite : 12
     * isCUTV : 12
     * isPVR : 12
     */

    /**
     *流派ID。

     */
    @SerializedName("genres")
    private List<String> genres;

    /**
     *订购方式，取值如下：

     -1：所有
     0：已订购且被定价为包月产品或免费包周期产品
     1：已订购且被定价为按次产品或免费按次产品
     2：未订购
     3：未订购且被定价为按次产品
     4：未订购且被定价为包月产品
     默认取值为-1

     说明：
     针对订购信息的过滤，会造成接口性能的大幅下降，所以请在用户低频访问、并且业务场景必须要求根据订购关系过滤的UI中使用。

     */
    @SerializedName("subscriptionTypes")
    private List<String> subscriptionTypes;

    /**
     *频道是否收藏。

     0：未收藏
     1：已收藏
     如果取值为空，表示不针对内容是否收藏进行过滤

     */
    @SerializedName("isFavorite")
    private String isFavorite;

    /**
     *频道是否支持CUTV，取值范围：

     0：不支持
     1：支持
     如果取值为空，表示不针对内容是否支持CUTV进行过滤

     */
    @SerializedName("isCUTV")
    private String isCUTV;

    /**
     *频道是否支持PVR，取值范围：

     1：支持NPVR
     2：支持CPVR
     3：支持NPVR或者CPVR
     如果取值为空，表示不针对内容是否支持PVR进行过滤

     */
    @SerializedName("isPVR")
    private String isPVR;

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getSubscriptionTypes() {
        return subscriptionTypes;
    }

    public void setSubscriptionTypes(List<String> subscriptionTypes) {
        this.subscriptionTypes = subscriptionTypes;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getIsCUTV() {
        return isCUTV;
    }

    public void setIsCUTV(String isCUTV) {
        this.isCUTV = isCUTV;
    }

    public String getIsPVR() {
        return isPVR;
    }

    public void setIsPVR(String isPVR) {
        this.isPVR = isPVR;
    }
}
