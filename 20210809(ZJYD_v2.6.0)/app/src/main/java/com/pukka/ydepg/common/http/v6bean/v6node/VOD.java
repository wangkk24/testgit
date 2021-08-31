package com.pukka.ydepg.common.http.v6bean.v6node;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.huawei.chfjydvideo.common.http.v6bean.v6node.VOD.java
 * @author: yh
 * @date: 2017-04-21 18:02
 */

public class VOD extends Metadata {
    public static final String KEY_SUPERSCRIPT = "superScript";
    public static final String KEY_SUPERSCRIPT_VALID_TIME = "superScriptValidTime";

    protected boolean isXiri = false;

    protected boolean isProfileVod = false;

    //0：是主账号；1：不是主账号  1使用"我的"页面观看历史效果；0使用两行效果
    protected String primaryAccount = "0";

    /**
     *第三方系统分配的内容Code。
     */
    @SerializedName("code")
    protected String code;

    /**
     *九天itemid
     */
    @SerializedName("itemid")
    protected String itemid;

    /**
     *点播内容类型。

     取值范围：

     AUDIO_VOD：音频点播
     VIDEO _VOD：视频点播
     */
    @SerializedName("contentType")
    protected String contentType;

    /**
     *内容简介。

     */
    @SerializedName("introduce")
    protected String introduce;

    /**
     *VOD类型。

     取值范围：

     0：非电视剧
     1：普通连续剧
     2：季播剧父集
     3：季播剧
     */
    @SerializedName("VODType")
    protected String VODType = "0";

    /**
     *如果VODType是连续剧/季播剧的类型，字段必填，取值包括：

     0：普通连续剧
     1：Movie Series
     */
    @SerializedName("seriesType")
    protected String seriesType;



    /**
     *子集总集数。

     当VODType为1（连续剧）返回连续剧下面的子集总集数。
     当VODType为2（季播剧父集）返回连续剧下面的季播剧总集数。
     当VODType为3（季播剧）时，返回季播剧下面的子集数。

     */
    @SerializedName("episodeCount")
    protected String episodeCount;

    /**
     *当VODType=2季播剧父集时，返回连续剧所有季里面的子集总数。

     */
    @SerializedName("episodeTotalCount")
    protected String episodeTotalCount;

    /**
     *内容价格。

     最小货币单位。

     */
    @SerializedName("price")
    protected String price;

    /**
     *内容的剩余观看时长，即用户鉴权通过的订购关系失效时间距离当前时间的间隔。

     如果用户未订购该内容返回0。

     单位：秒。

     */
    @SerializedName("rentPeriod")
    protected String rentPeriod;



    /**
     *观看级别。

     */
    @SerializedName("rating")
    protected Rating rating;

    /**
     *VOD上线时间，取值为距离1970年1月1号的毫秒数。

     */
    @SerializedName("startTime")
    protected String startTime;



    /**
     *VOD下线时间，取值为距离1970年1月1号的毫秒数。

     */
    @SerializedName("endTime")
    protected String endTime;

    /**
     *如果VOD被收藏，返回收藏记录。

     */
    @SerializedName("favorite")
    protected Favorite favorite;

    /**
     *标识内容是否被加锁。

     取值范围：

     1：已加锁
     0：未加锁
     */
    @SerializedName("isLocked")
    protected String isLocked;



    /**
     *书签。

     */
    @SerializedName("bookmark")
    protected Bookmark bookmark;


    /**
     *是否已订购。

     取值范围：

     0：未订购
     1：订购
     */
    @SerializedName("isSubscribed")
    protected String isSubscribed="0";

    /**
     *VOD出品日期，格式为yyyyMMdd。

     */
    @SerializedName("produceDate")
    protected String produceDate;

    /**
     *VOD被评价次数。

     */
    @SerializedName("scoreTimes")
    protected String scoreTimes;

    /**
     *VOD被评价的均值。

     保留到小数点后一位

     */
    @SerializedName("averageScore")
    protected String averageScore;


    /**
     *已播放次数。

     */
    @SerializedName("visitTimes")
    protected String visitTimes;


    /**
     *内容类别，类似Violence、Sexual等信息。

     */
    @SerializedName("advisories")
    protected List<String> advisories;


    /**
     *发行公司名。

     */
    @SerializedName("companyName")
    protected String companyName;

    /**
     *用户对该VOD的评分值，如果用户已评分，取值范围是1-10，否则取值是0。

     */
    @SerializedName("userScore")
    protected String userScore;


    /**
     *用户对该内容的点赞总数。

     */
    @SerializedName("likes")
    protected String likes;


    /**
     *当前登录的用户是否点赞

     0:当前未点赞
     1:当前用户已点赞
     */
    @SerializedName("isLike")
    protected String isLike;




    /**
     *内容归属的内容提供商 ID

     */
    @SerializedName("cpId")
    protected String cpId;


    /**
     *内容的演职员信息。

     CastRole类型请参见“CastRole”

     */
    @SerializedName("castRoles")
    protected List<CastRole> castRoles;


    /**
     *父集列表，当VOD是子集或者季播剧时有效。

     Sitcom类型请参见“Sitcom”。

     */
    @SerializedName("series")
    protected List<Sitcom> series;


    /**
     *VOD媒体文件列表。

     VODMediaFile请参见“VODMediaFile”。
     。

     */
    @SerializedName("mediaFiles")
    protected List<VODMediaFile> mediaFiles;

    /**
     *VOD支持的音轨语种，为语言的ISO 639-1双字节缩写。

     */
    @SerializedName("audioLanguages")
    protected List<ISOCode> audioLanguages;

    /**
     *出品国家信息。

     */
    @SerializedName("produceZone")
    protected ProduceZone produceZone;


    /**
     *字幕的语言，为ISO 639-1双字母缩写。

     */
    @SerializedName("subtitleLanguages")
    protected List<ISOCode> subtitleLanguages;


    /**
     *VOD的扩展属性，其中扩展属性的Key由局点CMS定制。

     */
    @SerializedName("customFields")
    protected List<NamedParameter> customFields;



    /**
     *内容地理版权信息，两位的国家码(遵从ISO 3166-1)

     */
    @SerializedName("locationCopyrights")
    protected List<String> locationCopyrights;


    @SerializedName("maxSitcomNO")
    protected String maxSitcomNO;




    @SerializedName("VODNum")
    protected String vodNum;


    /**
     *关联的叶子栏目ID。

     */
    @SerializedName("subjectIDs")
    protected List<String> subjectIDs;

    /**
     * 试看 秒
     */
    @SerializedName("previewDuration")
    private int previewDuration = -1;

    @SerializedName("cmsType")
    protected String cmsType;

    @SerializedName("feedback")
    protected Feedback feedback;

    protected String notice;

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public Feedback getFeedback() {
        if (null == feedback){
            feedback = new Feedback();
        }
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int  getPreviewDuration()
    {
        return previewDuration;
    }

    public void setPreviewDuration(int previewDuration)
    {
        this.previewDuration = previewDuration;
    }

    public boolean isProfileVod() {
        return isProfileVod;
    }

    public void setProfileVod(boolean profileVod) {
        isProfileVod = profileVod;
    }

    public String getPrimaryAccount() {
        return primaryAccount;
    }

    public void setPrimaryAccount(String primaryAccount) {
        this.primaryAccount = primaryAccount;
    }

    /**
     * 新需求，通过判断价格，来判断是否订购
     * @return
     */
    public boolean isOrder() {
        if (!TextUtils.isEmpty(price)) {
            double p;
            try {
                p = Double.parseDouble(price);
            } catch (Exception e) {
                p = -1;
            }
            if(p!=0){
                if(isSubscribed.equals("0")){
                    return false;
                }
            }
        }else{
            if(isSubscribed.equals("0")){
                return false;
            }
        }
        return true;
    }

    public List<String> getSubjectIDs() {
        return subjectIDs;
    }

    public void setSubjectIDs(List<String> subjectIDs) {
        this.subjectIDs = subjectIDs;
    }

    public String getVodNum() {
        return vodNum;
    }

    public void setVodNum(String vodNum) {
        this.vodNum = vodNum;
    }

    public String getMaxSitcomNO() {
        return maxSitcomNO;
    }

    public void setMaxSitcomNO(String maxSitcomNO) {
        this.maxSitcomNO = maxSitcomNO;
    }

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

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public String getVODType() {
        if (TextUtils.isEmpty(VODType)){
            VODType = "0";
        }
        return VODType;
    }

    public void setVODType(String VODType) {
        this.VODType = VODType;
    }

    public String getSeriesType() {
        return seriesType;
    }

    public void setSeriesType(String seriesType) {
        this.seriesType = seriesType;
    }

    public String getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(String episodeCount) {
        this.episodeCount = episodeCount;
    }

    public String getEpisodeTotalCount() {
        return episodeTotalCount;
    }

    public void setEpisodeTotalCount(String episodeTotalCount) {
        this.episodeTotalCount = episodeTotalCount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRentPeriod() {
        return rentPeriod;
    }

    public void setRentPeriod(String rentPeriod) {
        this.rentPeriod = rentPeriod;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
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

    public Bookmark getBookmark() {
        return bookmark;
    }

    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

    public String getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(String isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public String getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(String produceDate) {
        this.produceDate = produceDate;
    }

    public String getScoreTimes() {
        return scoreTimes;
    }

    public void setScoreTimes(String scoreTimes) {
        this.scoreTimes = scoreTimes;
    }

    public String getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(String averageScore) {
        this.averageScore = averageScore;
    }

    public String getVisitTimes() {
        return visitTimes;
    }

    public void setVisitTimes(String visitTimes) {
        this.visitTimes = visitTimes;
    }

    public List<String> getAdvisories() {
        return advisories;
    }

    public void setAdvisories(List<String> advisories) {
        this.advisories = advisories;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public String getUserScore() {
        return userScore;
    }

    public void setUserScore(String userScore) {
        this.userScore = userScore;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public List<CastRole> getCastRoles() {
        return castRoles;
    }

    public void setCastRoles(List<CastRole> castRoles) {
        this.castRoles = castRoles;
    }

    public List<Sitcom> getSeries() {
        return series;
    }

    public void setSeries(List<Sitcom> series) {
        this.series = series;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<VODMediaFile> getMediaFiles() {
        return mediaFiles;
    }

    public void setMediaFiles(List<VODMediaFile> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }

    public List<ISOCode> getAudioLanguages() {
        return audioLanguages;
    }

    public void setAudioLanguages(List<ISOCode> audioLanguages) {
        this.audioLanguages = audioLanguages;
    }

    public ProduceZone getProduceZone() {
        return produceZone;
    }

    public void setProduceZone(ProduceZone produceZone) {
        this.produceZone = produceZone;
    }

    public List<ISOCode> getSubtitleLanguages() {
        return subtitleLanguages;
    }

    public void setSubtitleLanguages(List<ISOCode> subtitleLanguages) {
        this.subtitleLanguages = subtitleLanguages;
    }

    public List<NamedParameter> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<NamedParameter> customFields) {
        this.customFields = customFields;
    }

    public List<String> getLocationCopyrights() {
        return locationCopyrights;
    }

    public void setLocationCopyrights(List<String> locationCopyrights) {
        this.locationCopyrights = locationCopyrights;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public VOD() {
    }
    /**
     * the range of field vodType
     */
    public interface VODType
    {
        String UN_TV_SERIES = "0";

        String NORMAL_SERIES = "1";

        String MOVIE_SERIES = "2";

        String SEASON_PLAY = "3";
    }

    public boolean isXiri() {
        return isXiri;
    }

    public void setXiri(boolean xiri) {
        isXiri = xiri;
    }



    public String getCmsType() {
        return cmsType;
    }

    public void setCmsType(String cmsType) {
        this.cmsType = cmsType;
    }
}
