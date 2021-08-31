package com.pukka.ydepg.moudule.player.node;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;



public class VODMediaFile implements Serializable
{
    @SerializedName("ID")
    private String id;

    @SerializedName("isdownload")
    private int isdownload;

    @SerializedName("elapseTime")
    private int elapseTime;

    @SerializedName("bitrate")
    private int bitrate;

    @SerializedName("definition")
    private int definition;

    @SerializedName("HDCPEnable")
    private int hdcpEnable;

    @SerializedName("macrovision")
    private int macrovision;

    @SerializedName("dimension")
    private int dimension;

    @SerializedName("CGMSA")
    private int cgmsa;

    @SerializedName("picture")
    private String picture;

    @SerializedName("latestEpisodeIndex")
    private int latestEpisodeIndex ;

    @SerializedName("categoryIDs")
    private List<String> categoryIDs;

    @SerializedName("latestEpisodeName")
    private String latestEpisodeName;

    @SerializedName("extensionInfo")
    private List<NamedParameter> extensionInfo;

    public String getPicture()
    {
        return picture;
    }

    public void setPicture(String picture)
    {
        this.picture = picture;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getIsdownload()
    {
        return isdownload;
    }

    public void setIsdownload(int isdownload)
    {
        this.isdownload = isdownload;
    }

    public int getElapseTime()
    {
        return elapseTime;
    }

    public void setElapseTime(int elapseTime)
    {
        this.elapseTime = elapseTime;
    }

    public int getBitrate()
    {
        return bitrate;
    }

    public void setBitrate(int bitrate)
    {
        this.bitrate = bitrate;
    }


    public int getHdcpEnable()
    {
        return hdcpEnable;
    }

    public void setHdcpEnable(int hdcpEnable)
    {
        this.hdcpEnable = hdcpEnable;
    }

    public int getDefinition()
    {
        return definition;
    }

    public void setDefinition(int definition)
    {
        this.definition = definition;
    }


    public int getDimension()
    {
        return dimension;
    }

    public void setDimension(int dimension)
    {
        this.dimension = dimension;
    }

    public int getMacrovision()
    {
        return macrovision;
    }

    public void setMacrovision(int macrovision)
    {
        this.macrovision = macrovision;
    }

    public int getCgmsa()
    {
        return cgmsa;
    }

    public void setCgmsa(int cgmsa)
    {
        this.cgmsa = cgmsa;
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
