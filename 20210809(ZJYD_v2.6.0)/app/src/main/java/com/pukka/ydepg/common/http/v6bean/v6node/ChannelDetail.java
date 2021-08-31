package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.huawei.chfjydvideo.common.http.v6bean.v6node.ChannelDetail.java
 * @author: yh
 * @date: 2017-04-24 14:18
 */

public class ChannelDetail extends Metadata implements Serializable {


    /**
     * ID :
     * code :
     * channelNO :
     * name :
     * introduce :
     * contentType :
     * isPPV :
     * rating :
     * picture :
     * logo :
     * price :
     * audioLanguages :
     * subtitleLanguages :
     * deviceTypes :
     * genres :
     * customFields :
     * isCUTVDependonLivetv :
     * subjectIDs :
     * favorite :
     * isLocked :
     * hasPIP :
     * physicalChannels :
     * pipPhysicalChannel :
     * locationCopyrights :
     * extensionFields :
     */

//    /**
//     * 内容ID。
//     */
//    @SerializedName("ID")
//    private String ID;


    /**
     * 第三方系统分配的内容Code。
     */
    @SerializedName("code")
    private String code;


    /**
     * 返回SP操作员设置的系统频道号。
     * <p>
     * 如果是根据频道ID获取频道信息，而且一个逻辑频道有多个频道号的话，ChannelNO随机返回一个。
     */
    @SerializedName("channelNO")
    private String channelNO;



    /**
     * 时移时长 s秒
     */
    @SerializedName("PLTVLength")
    private int pltvLength;


    //    /**
//     * 内容名称。
//     */
//    @SerializedName("name")
//    private String name;

    /**
     * 内容简介。
     */
    @SerializedName("introduce")
    private String introduce;

    /**
     * 内容类型。
     * <p>
     * AUDIO_CHANNEL
     * VIDEO_CHANNEL
     */
    @SerializedName("contentType")
    private String contentType;

    /**
     * 频道是否支持节目单级的授权。
     * <p>
     * 取值范围：
     * <p>
     * 0：不支持
     * 1：支持
     */
    @SerializedName("isPPV")
    private String isPPV;

    /**
     * 观看级别。
     */
    @SerializedName("rating")
    private Rating rating;

//    /**
//     * 海报图片路径。
//     * <p>
//     * 参见“Picture”类型。
//     */
//    @SerializedName("picture")
//    private Picture picture;

    /**
     * 频道Logo。
     * <p>
     * 请参见“ChannelLogo”类型。
     */
    @SerializedName("logo")
    private ChannelLogo logo;

    /**
     * 内容价格。
     * <p>
     * 单位为最小货币单位。
     */
    @SerializedName("price")
    private String price;

    /**
     * 频道支持的音轨语种，基线版本为ISO 639-1双字母缩写，DT CMLS同步的是ISO-639-3标准。
     * <p>
     * 简码对应的名称是平台根据当前的用户语种，查询ISO标准编码表获取。当前支持的语种包括：
     * <p>
     * English（默认，如果用户语种不在这几种内，返回该语种对应的语种）
     * Chinese
     * Arabic
     * Hungarian
     * German
     */
    @SerializedName("audioLanguages")
    private List<ISOCode> audioLanguages;

    /**
     * 频道支持的字幕语种，基线版本为ISO 639-1双字母缩写，DT CMLS同步的是ISO-639-3标准。
     * <p>
     * 简码对应的名称是平台根据当前的用户语种，查询ISO标准编码表获取。当前支持的语种包括：
     * <p>
     * English（默认，如果用户语种不在这几种内，返回该语种对应的语种）
     * Chinese
     * Arabic
     * Hungarian
     * German
     */
    @SerializedName("subtitleLanguages")
    private List<ISOCode> subtitleLanguages;

    /**
     * 内容关联的CP设备类型。
     */
    @SerializedName("deviceTypes")
    private List<DeviceType> deviceTypes;

//    /**
//     * 频道的流派信息。
//     */
//    @SerializedName("genres")
//    private List<Genre> genres;

    /**
     * 频道元数据的自定义扩展属性，其中扩展属性的Key由局点CMS定制。
     */
    @SerializedName("customFields")
    private List<NamedParameter> customFields;

    private List<QueryPlaybill> queryPlaybillList;

    /**
     * Catch-up TV是否依赖于直播特性，取值包括：
     * <p>
     * 0：不依赖
     * 1：依赖
     * 默认值为0。
     * <p>
     * 说明：
     * 如果依赖，用户必须已经订购了该频道的直播特性才可以订购Catch-up TV（包括订购频道内容/媒资的Catch-up TV特性、订购节目单的Catch-up TV特性，以及订购包含了Catch-up TV特性、节目单的套餐包）。
     * 如果不依赖，则无需订购该频道的直播特性，可以直接订购Catch-up TV
     */
    @SerializedName("isCUTVDependonLivetv")
    private String isCUTVDependonLivetv;

    /**
     * 频道/VAS关联的栏目ID。
     */
    @SerializedName("subjectIDs")
    private List<String> subjectIDs;

    /**
     * 如果频道被收藏，返回收藏记录。
     */
    @SerializedName("favorite")
    private Favorite favorite;

    /**
     * 标识内容是否有童锁。
     * <p>
     * 取值范围：
     * <p>
     * 1：有童锁
     * 0：无童锁
     */
    @SerializedName("isLocked")
    private String isLocked;

    /**
     * 是否包含PIP小流频道。
     * <p>
     * 0：否
     * 1：是
     * IPTV频道和OTT频道都支持，其中IPTV/OTT分别处理如下：
     * <p>
     * IPTV频道的PIP使用PhysicalChannel.isSupportPIP中isSupportPIP=1的物理媒资；
     * OTT频道的PIP使用PhysicalChannel.isSupportPIP中isSupportPIP=1的物理媒资，取其最低码率的码流播放。
     */
    @SerializedName("hasPIP")
    private String hasPIP;

    /**
     * 物理频道信息。
     */
    @SerializedName("physicalChannels")
    private List<PhysicalChannel> physicalChannels;

    /**
     * IPTV领域的PIP小流物理频道信息，OTT领域的PIP小流是多码率的物理频道中码率最小的码流，不在这这个字段中体现。
     */
    @SerializedName("pipPhysicalChannel")
    private PhysicalChannel pipPhysicalChannel;

    /**
     * 内容地理版权信息，两位的国家码(遵从ISO 3166-1)
     */
    @SerializedName("locationCopyrights")
    private List<String> locationCopyrights;

    /**
     * 扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    @SerializedName("visibility")
    private String visibility;




    //非平台返回,本地逻辑信息,用于多码率切换, 码率及其对应频道ID的映射
    private Map<String,String> mapRate2ID = new HashMap<>();

    public Map<String, String> getMapRate2ID() {
        return mapRate2ID;
    }

    public void setMapRate2ID(Map<String, String> mapRate2ID) {
        this.mapRate2ID = mapRate2ID;
    }






    public int getPltvLength()
    {
        return pltvLength;
    }

    public void setPltvLength(int pltvLength)
    {
        this.pltvLength = pltvLength;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChannelNO() {
        return channelNO;
    }

    public void setChannelNO(String channelNO) {
        this.channelNO = channelNO;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getIsPPV() {
        return isPPV;
    }

    public void setIsPPV(String isPPV) {
        this.isPPV = isPPV;
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

    public ChannelLogo getLogo() {
        return logo;
    }

    public void setLogo(ChannelLogo logo) {
        this.logo = logo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<ISOCode> getAudioLanguages() {
        return audioLanguages;
    }

    public void setAudioLanguages(List<ISOCode> audioLanguages) {
        this.audioLanguages = audioLanguages;
    }

    public List<ISOCode> getSubtitleLanguages() {
        return subtitleLanguages;
    }

    public void setSubtitleLanguages(List<ISOCode> subtitleLanguages) {
        this.subtitleLanguages = subtitleLanguages;
    }

    public List<DeviceType> getDeviceTypes() {
        return deviceTypes;
    }

    public void setDeviceTypes(List<DeviceType> deviceTypes) {
        this.deviceTypes = deviceTypes;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<NamedParameter> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<NamedParameter> customFields) {
        this.customFields = customFields;
    }

    public String getIsCUTVDependonLivetv() {
        return isCUTVDependonLivetv;
    }

    public void setIsCUTVDependonLivetv(String isCUTVDependonLivetv) {
        this.isCUTVDependonLivetv = isCUTVDependonLivetv;
    }

    public List<String> getSubjectIDs() {
        return subjectIDs;
    }

    public void setSubjectIDs(List<String> subjectIDs) {
        this.subjectIDs = subjectIDs;
    }

    public Favorite getFavorite() {
        return favorite;
    }

    public void setFavorite(Favorite favorite) {
        this.favorite = favorite;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getHasPIP() {
        return hasPIP;
    }

    public void setHasPIP(String hasPIP) {
        this.hasPIP = hasPIP;
    }

    public List<PhysicalChannel> getPhysicalChannels() {
        return physicalChannels;
    }

    public void setPhysicalChannels(List<PhysicalChannel> physicalChannels) {
        this.physicalChannels = physicalChannels;
    }

    public PhysicalChannel getPipPhysicalChannel() {
        return pipPhysicalChannel;
    }

    public void setPipPhysicalChannel(PhysicalChannel pipPhysicalChannel) {
        this.pipPhysicalChannel = pipPhysicalChannel;
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

    public List<QueryPlaybill> getQueryPlaybillList() {
        return queryPlaybillList;
    }

    public void setQueryPlaybillList(List<QueryPlaybill> queryPlaybillList) {
        this.queryPlaybillList = queryPlaybillList;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

}
