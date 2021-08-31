package com.pukka.ydepg.moudule.player.node;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jWX234536 on 2015/8/4.
 */
public class VODDetail implements Serializable
{
    @SerializedName("ID")
    private String id;

    @SerializedName("name")
    private String name;

    /**
     * 易视腾视频的code
     */
    @SerializedName("CODE")
    private String code;

    @SerializedName("introduce")
    private String introduce;

    /**
     * port
     */
    @SerializedName("picture")
    private String picture;

    /**
     * land
     */
    @SerializedName("title")
    private String pictureTitle;

    /**
     * land  big
     */
    @SerializedName("ad")
    private String picturePoster;

    @SerializedName("stills")
    private List<Still> stills;

    @SerializedName("director")
    private List<String> director;

    @SerializedName("actor")
    private List<String> actor;

    @SerializedName("type")
    private int type;

    @SerializedName("mediaFiles")
    private List<VODMediaFile> mediaFiles;

    @SerializedName("clipFiles")
    private List<VODMediaFile> clipFiles;

    @SerializedName("ratingID")
    private int ratingId;

    @SerializedName("isFavorited")
    private int isFavorited;

    @SerializedName("isSubscribed")
    private int isSubscribed;

    @SerializedName("produceDate")
    private String produceDate;

    @SerializedName("foreignID")
    private String foreignId;

    @SerializedName("averageScore")
    private String averageScore;

    @SerializedName("languages")
    private String languages;

    @SerializedName("episodes")
    private List<Episode> episodeList;

    @SerializedName("like")
    private int like;

    @SerializedName("genres")
    private String genres;

    @SerializedName("recommendedData")
    private List<VOD> recommendedData;

    @SerializedName("latestEpisodeIndex")
    private int latestEpisodeIndex ;

    @SerializedName("categoryIDs")
    private List<String> categoryIDs;

    @SerializedName("latestEpisodeName")
    private String latestEpisodeName;

    @SerializedName("extensionInfo")
    private List<NamedParameter> extensionInfo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPicture()
    {
        return picture;
    }

    public void setPicture(String picture)
    {
        this.picture = picture;
    }
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Still> getStills()
    {
        return stills;
    }

    public void setStills(List<Still> stills)
    {
        this.stills = stills;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public List<String> getDirector()
    {
        return director;
    }

    public void setDirector(List<String> director)
    {
        this.director = director;
    }

    public List<String> getActor()
    {
        return actor;
    }

    public void setActor(List<String> actor)
    {
        this.actor = actor;
    }

    public String getIntroduce()
    {
        return introduce;
    }

    public void setIntroduce(String introduce)
    {
        this.introduce = introduce;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public List<VODMediaFile> getMediaFiles()
    {
        return mediaFiles;
    }

    public void setMediaFiles(List<VODMediaFile> mediaFiles)
    {
        this.mediaFiles = mediaFiles;
    }

    public List<VODMediaFile> getClipFiles()
    {
        return clipFiles;
    }

    public void setClipFiles(List<VODMediaFile> clipFiles)
    {
        this.clipFiles = clipFiles;
    }

    public int getRatingId()
    {
        return ratingId;
    }

    public void setRatingId(int ratingId)
    {
        this.ratingId = ratingId;
    }

    public int getIsFavorited()
    {
        return isFavorited;
    }

    public void setIsFavorited(int isFavorited)
    {
        this.isFavorited = isFavorited;
    }

    public int getIsSubscribed()
    {
        return isSubscribed;
    }

    public void setIsSubscribed(int isSubscribed)
    {
        this.isSubscribed = isSubscribed;
    }

    public String getProduceDate()
    {
        return produceDate;
    }

    public void setProduceDate(String produceDate)
    {
        this.produceDate = produceDate;
    }

    public String getForeignId()
    {
        return foreignId;
    }

    public void setForeignId(String foreignId)
    {
        this.foreignId = foreignId;
    }

    public String getAverageScore()
    {
        return averageScore;
    }

    public void setAverageScore(String averageScore)
    {
        this.averageScore = averageScore;
    }

    public String getLanguages()
    {
        return languages;
    }

    public void setLanguages(String languages)
    {
        this.languages = languages;
    }

    public List<Episode> getEpisodeList()
    {
        return episodeList;
    }

    public void setEpisodeList(List<Episode> episodeList)
    {
        this.episodeList = episodeList;
    }

    public int getLike()
    {
        return like;
    }

    public void setLike(int like)
    {
        this.like = like;
    }

    public String getGenres()
    {
        return genres;
    }

    public void setGenres(String genres)
    {
        this.genres = genres;
    }

    public List<VOD> getRecommendedData()
    {
        return recommendedData;
    }

    public void setRecommendedData(List<VOD> recommendedData)
    {
        this.recommendedData = recommendedData;
    }

    public String getPictureTitle() {
        return pictureTitle;
    }

    public void setPictureTitle(String pictureTitle) {
        this.pictureTitle = pictureTitle;
    }

    public String getPicturePoster() {
        return picturePoster;
    }

    public void setPicturePoster(String picturePoster) {
        this.picturePoster = picturePoster;
    }

    public List<NamedParameter> getExtensionInfo() {
        return extensionInfo;
    }

    public void setExtensionInfo(List<NamedParameter> extensionInfo) {
        this.extensionInfo = extensionInfo;

    }

    public int getLatestEpisodeIndex() {
        return latestEpisodeIndex;
    }

    public void setLatestEpisodeIndex(int latestEpisodeIndex) {
        this.latestEpisodeIndex = latestEpisodeIndex;
    }

    public List<String> getCategoryIDs() {
        return categoryIDs;
    }

    public void setCategoryIDs(List<String> categoryIDs) {
        this.categoryIDs = categoryIDs;
    }

    public String getLatestEpisodeName() {
        return latestEpisodeName;
    }

    public void setLatestEpisodeName(String latestEpisodeName) {
        this.latestEpisodeName = latestEpisodeName;
    }
}
