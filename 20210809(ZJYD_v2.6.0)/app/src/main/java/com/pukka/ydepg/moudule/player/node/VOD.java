package com.pukka.ydepg.moudule.player.node;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


/**
 * Created by jWX234536 on 2015/8/4.
 */
public class VOD  implements Serializable
{
    @SerializedName("ID")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("seriesID")
    private String seriesId;

    @SerializedName("seriesName")
    private String seriesName;

    /**
     * port
     */
    @SerializedName("picture")
    private String picture;

    @SerializedName("averageScore")
    private String averageScore;


    @SerializedName("simdegree")
    private String simDegree;

    @SerializedName("definition")
    private int definition;

    /**
     * land
     */
    @SerializedName("title")
    private String pictureTitle;


    /**
     * lang big
     */
    @SerializedName("ad")
    private String picturePoster;
    /**
     * 奥运专题 区分海报还是频道 为空为海报 不为空为频道
     */
    private String type;
    /**
     * 奥运专题 频道专用
     */
    private Schedule schedule;

    @SerializedName("latestEpisodeIndex")
    private int latestEpisodeIndex ;

    @SerializedName("categoryIDs")
    private List<String> categoryIDs;

    @SerializedName("latestEpisodeName")
    private String latestEpisodeName;

    @SerializedName("extensionInfo")
    private List<NamedParameter> extensionInfo;

    @SerializedName("startTime")
    private String startTime;
    @SerializedName("endTime")
    private String endTime;
    @SerializedName("director")
    private List<String> director;
    @SerializedName("actor")
    private List<String> actor;
    @Override
    public String toString()
    {
        return "VOD{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", averageScore=" + averageScore +
                ", seriesId='" + seriesId + '\'' +
                ", seriesName='" + seriesName + '\'' +
                ", definition=" + definition +
                ", simDegree='" + simDegree + '\'' +
                ", categoryIDs='" + categoryIDs + '\'' +
                ", extensionInfo='" + extensionInfo + '\'' +
                ", director='" + director.toString() + '\'' +
                ", actor='" + actor.toString() + '\'' +
                '}';
    }

    public String getSimDegree()
    {
        return simDegree;
    }

    public void setSimDegree(String simDegree)
    {
        this.simDegree = simDegree;
    }

    public int getDefinition()
    {
        return definition;
    }

    public void setDefinition(int definition)
    {
        this.definition = definition;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getPicture()
    {
        return picture;
    }

    public String getAverageScore()
    {
        return averageScore;
    }

    public void setAverageScore(String averageScore)
    {
        this.averageScore = averageScore;
    }

    public void setPicture(String picture)
    {
        this.picture = picture;
    }


    public String getSeriesName()
    {
        return seriesName;
    }

    public void setSeriesName(String seriesName)
    {
        this.seriesName = seriesName;
    }

    public String getSeriesId()
    {
        return seriesId;
    }

    public void setSeriesId(String seriesId)
    {
        this.seriesId = seriesId;
    }

    public String getPicturePoster() {
        return picturePoster;
    }

    public void setPicturePoster(String picturePoster) {
        this.picturePoster = picturePoster;
    }

    public String getPictureTitle() {
        return pictureTitle;
    }

    public void setPictureTitle(String pictureTitle) {
        this.pictureTitle = pictureTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void  setSchedule(Schedule schedule){
        this.schedule = schedule;
    }

    public Schedule getSchedule() {
        return schedule;
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

    public List<NamedParameter> getExtensionInfo() {
        return extensionInfo;
    }

    public void setExtensionInfo(List<NamedParameter> extensionInfo) {
        this.extensionInfo = extensionInfo;


    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<String> getDirector() {
        return director;
    }

    public void setDirector(List<String> director) {
        this.director = director;
    }

    public List<String> getActor() {
        return actor;
    }

    public void setActor(List<String> actor) {
        this.actor = actor;
    }
}
