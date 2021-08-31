package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: VODDetail.java
 * @author: yh
 * @date: 2017-04-24 12:37
 */

public class VODDetail extends VOD {


    /**
     * ID :
     * code :
     * name :
     * contentType :
     * introduce :
     * picture :
     * castRoles :
     * VODType :
     * seriesType :
     * episodes :
     * episodeCount :
     * episodeTotalCount :
     * brotherSeasonVODs :
     * series :
     * price :
     * genres :
     * mediaFiles :
     * clipfiles :
     * bgMusicFile :
     * chapters :
     * rentPeriod :
     * rating :
     * startTime :
     * endTime :
     * favorite :
     * isLocked :
     * bookmark :
     * isSubscribed :
     * subscriptionType :
     * produceDate :
     * scoreTimes :
     * averageScore :
     * loyaltyCount :
     * audioLanguages :
     * produceZone :
     * creditVODSendLoyalty :
     * visitTimes :
     * subtitleLanguages :
     * awards :
     * deviceTypes :
     * keyword :
     * advisories :
     * playable :
     * subjectIDs :
     * customFields :
     * companyName :
     * userScore :
     * locationCopyrights :
     * cpId :
     * likes :
     * isLike :
     */




    /**
     *如果是连续剧（包括：普通连续剧/季播剧父集/季播剧）的话，需要返回子集列表。

     */
    @SerializedName("episodes")
    private List<Episode> episodes;



    /**
     *如果VOD是季播剧，返回和VOD归属父集下的所有季播剧，包含当前季。

     比如老友记分为第一季、第二季、等等，如果该VOD是第一季，此参数返回的就是第一季、第二季等。


     说明：
     如果季播剧属于多个父集，返回其归属第一个父集的所有季播剧。

     */
    @SerializedName("brotherSeasonVODs")
    private List<BrotherSeasonVOD> brotherSeasonVODs;






    /**
     *片花媒体文件列表，正片有片花该值才有效。

     VODMediaFile请参见“VODMediaFile”。

     */
    @SerializedName("clipfiles")
    private List<VODMediaFile> clipfiles;

    /**
     *背景音乐媒体文件。

     VODMediaFile请参见“VODMediaFile”。

     */
    @SerializedName("bgMusicFile")
    private List<VODMediaFile> bgMusicFile;

    /**
     *多章节信息。

     Chapter类型请参见“Chapter”。

     */
    @SerializedName("chapters")
    private List<Chapter> chapters;











    /**
     *VOD鉴权通过的产品类型，取值范围：

     0：包周期订购
     1：按次订购
     2：未订购
     */
    @SerializedName("subscriptionType")
    private String subscriptionType;










    /**
     *积分价格。

     此参数仅在支持积分消费时才有意义，仅供页面展示用，实际订购时需要消费的积分从订购的产品信息中获取。

     */
    @SerializedName("loyaltyCount")
    private String loyaltyCount;







    /**
     *观看Credit VOD需要赠送的积分数
     */
    @SerializedName("creditVODSendLoyalty")
    private String creditVODSendLoyalty;







    /**
     *该VOD的获奖情况。

     */
    @SerializedName("awards")
    private String awards;


    /**
     *内容对应的设备分组。

     DeviceType类型请参见“DeviceType”。

     */
    @SerializedName("deviceTypes")
    private DeviceType deviceTypes;


    /**
     *关键字。

     */
    @SerializedName("keyword")
    private String keyword;




    /**
     *VOD是否存在当前终端能播放的正片，取值包括：

     0：不存在
     1：存在
     说明：
     1.如果playable=1,mediafiles表示当前终端可播放的正片媒资。

     2.如果playable=0且查询VOD详情接口的filterType=1，将返回VOD所有正片媒资

     */
    @SerializedName("playable")
    private String playable;




    private int playPosition;
    private String playUrl;
    private boolean isVodItem;
    private int episodeIndex;



    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public List<BrotherSeasonVOD> getBrotherSeasonVODs() {
        return brotherSeasonVODs;
    }

    public void setBrotherSeasonVODs(List<BrotherSeasonVOD> brotherSeasonVODs) {
        this.brotherSeasonVODs = brotherSeasonVODs;
    }

    public List<VODMediaFile> getClipfiles() {
        return clipfiles;
    }

    public void setClipfiles(List<VODMediaFile> clipfiles) {
        this.clipfiles = clipfiles;
    }

    public List<VODMediaFile> getBgMusicFile() {
        return bgMusicFile;
    }

    public void setBgMusicFile(List<VODMediaFile> bgMusicFile) {
        this.bgMusicFile = bgMusicFile;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }


    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getLoyaltyCount() {
        return loyaltyCount;
    }

    public void setLoyaltyCount(String loyaltyCount) {
        this.loyaltyCount = loyaltyCount;
    }

    public String getCreditVODSendLoyalty() {
        return creditVODSendLoyalty;
    }

    public void setCreditVODSendLoyalty(String creditVODSendLoyalty) {
        this.creditVODSendLoyalty = creditVODSendLoyalty;
    }




    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public DeviceType getDeviceTypes() {
        return deviceTypes;
    }

    public void setDeviceTypes(DeviceType deviceTypes) {
        this.deviceTypes = deviceTypes;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    public String getPlayable() {
        return playable;
    }

    public void setPlayable(String playable) {
        this.playable = playable;
    }


    public int getPlayPosition() {
        return playPosition;
    }

    public void setPlayPosition(int playPosition) {
        this.playPosition = playPosition;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public boolean isVodItem() {
        return isVodItem;
    }

    public void setVodItem(boolean vodItem) {
        isVodItem = vodItem;
    }

    public int getEpisodeIndex() {
        return episodeIndex;
    }

    public void setEpisodeIndex(int episodeIndex) {
        this.episodeIndex = episodeIndex;
    }
}
