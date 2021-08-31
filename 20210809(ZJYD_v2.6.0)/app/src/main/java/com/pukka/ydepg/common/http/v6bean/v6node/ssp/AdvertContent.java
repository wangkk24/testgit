package com.pukka.ydepg.common.http.v6bean.v6node.ssp;

import com.google.gson.annotations.SerializedName;

public class AdvertContent {

    public interface AdvertType{
        String DISPLAY = "DISPLAY";
        String VIDEO   = "VIDEO";
    }

    @SerializedName("adId")
    private String adId;

    @SerializedName("placementCode")
    private String placementCode;

    @SerializedName("adType")
    private String adType;

    @SerializedName("adName")
    private String adName;

    @SerializedName("adDesc")
    private String adDesc;

    @SerializedName("display")
    private AdvertDisplay display;

    @SerializedName("video")
    private AdvertVideo video;

    @SerializedName("ext")
    private String ext;


    //对应此响应消息的请求消息的requestID,用于上报话单
    private String relationID;


    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId)
    {
        this.adId = adId;
    }

    public String getPlacementCode()
    {
        return placementCode;
    }

    public void setPlacementCode(String placementCode)
    {
        this.placementCode = placementCode;
    }

    public String getAdType()
    {
        return adType;
    }

    public void setAdType(String adType)
    {
        this.adType = adType;
    }

    public String getAdName()
    {
        return adName;
    }

    public void setAdName(String adName)
    {
        this.adName = adName;
    }

    public String getAdDesc()
    {
        return adDesc;
    }

    public void setAdDesc(String adDesc)
    {
        this.adDesc = adDesc;
    }

    public AdvertDisplay getDisplay()
    {
        return display;
    }

    public void setDisplay(AdvertDisplay display)
    {
        this.display = display;
    }

    public AdvertVideo getVideo()
    {
        return video;
    }

    public void setVideo(AdvertVideo video)
    {
        this.video = video;
    }

    public String getExt()
    {
        return ext;
    }

    public void setExt(String ext)
    {
        this.ext = ext;
    }


    public String getRelationID() {
        return relationID;
    }

    public void setRelationID(String relationID) {
        this.relationID = relationID;
    }
}