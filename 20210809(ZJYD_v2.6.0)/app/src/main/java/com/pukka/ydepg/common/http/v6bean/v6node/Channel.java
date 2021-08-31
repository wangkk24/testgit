package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.huawei.chfjydvideo.common.http.v6bean.v6node.Channel.java
 * @author: yh
 * @date: 2017-04-24 09:27
 */

public class Channel extends Metadata  {


    /**
     * ID :
     * name :
     * contentType :
     * channelNO : 123
     * logo : ChannelLogo
     * picture : Picture
     * favorite : Favorite
     * channelSet : PhysicalChannelSet
     * currentPlaybill : PlaybillLite
     * recmExplain :
     * locationCopyrights : [""]
     * extensionFields : [""]
     */

//    /**
//     *频道的内容ID。
//     */
//    @SerializedName("ID")
//    private String ID;
//
//    /**
//     *频道的内容名称。
//
//     */
//    @SerializedName("name")
//    private String name;


    /**
     *第三方系统分配的内容Code。
     */
    @SerializedName("code")
    protected String code;

    /**
     *频道类型，取值包括：

     AUDIO_CHANNEL
     VIDEO_CHANNEL
     */
    @SerializedName("contentType")
    private String contentType;

    /**
     *返回SP操作员设置的系统频道号。

     如果是根据频道ID获取频道信息，而且一个逻辑频道有多个频道号的话，channelNO随机返回一个。

     */
    @SerializedName("channelNO")
    private int channelNO;

    /**
     *频道Logo。

     */
    @SerializedName("logo")
    private ChannelLogo logo;

//    /**
//     *频道海报路径。
//
//     参考“Picture”
//
//     */
//    @SerializedName("picture")
//    private Picture picture;

    /**
     *如果频道被收藏，返回收藏记录。

     */
    @SerializedName("favorite")
    private Favorite favorite;

    /**
     *物理频道属性集合。

     */
    @SerializedName("channelSet")
    private PhysicalChannelSet channelSet;

    /**
     *当前的节目单，取频道下当前时间的节目单；如果当前没有节目单则返回填充节目单。

     */
    @SerializedName("currentPlaybill")
    private PlaybillLite currentPlaybill;

    /**
     *推荐理由。

     说明：
     该字段在对接推荐系统时返回，如推荐系统未返回则为空。

     */
    @SerializedName("recmExplain")
    private String recmExplain;

    /**
     *内容地理版权信息，两位的国家码(遵从ISO 3166-1)。

     */
    @SerializedName("locationCopyrights")
    private List<String> locationCopyrights;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getChannelNO() {
        return channelNO;
    }

    public void setChannelNO(int channelNO) {
        this.channelNO = channelNO;
    }

    public ChannelLogo getLogo() {
        return logo;
    }

    public void setLogo(ChannelLogo logo) {
        this.logo = logo;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Favorite getFavorite() {
        return favorite;
    }

    public void setFavorite(Favorite favorite) {
        this.favorite = favorite;
    }

    public PhysicalChannelSet getChannelSet() {
        return channelSet;
    }

    public void setChannelSet(PhysicalChannelSet channelSet) {
        this.channelSet = channelSet;
    }

    public PlaybillLite getCurrentPlaybill() {
        return currentPlaybill;
    }

    public void setCurrentPlaybill(PlaybillLite currentPlaybill) {
        this.currentPlaybill = currentPlaybill;
    }

    public String getRecmExplain() {
        return recmExplain;
    }

    public void setRecmExplain(String recmExplain) {
        this.recmExplain = recmExplain;
    }

    public List<String> getLocationCopyrights() {
        return locationCopyrights;
    }

    public void setLocationCopyrights(List<String> locationCopyrights) {
        this.locationCopyrights = locationCopyrights;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Channel() {
    }
}
