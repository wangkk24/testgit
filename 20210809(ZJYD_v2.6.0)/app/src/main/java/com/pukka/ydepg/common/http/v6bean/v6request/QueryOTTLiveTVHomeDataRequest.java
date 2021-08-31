package com.pukka.ydepg.common.http.v6bean.v6request;


import com.google.gson.annotations.SerializedName;

/**
 * the request of QueryOTTLiveTVHomeDataRequest
 *
 */
public class QueryOTTLiveTVHomeDataRequest
{
    @SerializedName("date")
    private String date;

    /**
     * count
     */
    @SerializedName("count")
    private String count;

    @SerializedName("whatsOnCount")
    private String whatsOnCount;

    @SerializedName("happyNextCount")
    private String happyNextCount;

    /**
     * subjectCount
     */
    @SerializedName("subjectCount")
    private String subjectCount;

    @SerializedName("channelSubjectID")
    private String channelSubjectID;

    public String getCount()
    {
        return count;
    }

    public void setCount(String count)
    {
        this.count = count;
    }

    public String getSubjectCount()
    {
        return subjectCount;
    }

    public void setSubjectCount(String subjectCount)
    {
        this.subjectCount = subjectCount;
    }

    public String getChannelSubjectId()
    {
        return channelSubjectID;
    }

    public void setChannelSubjectId(String channelSubjectID)
    {
        this.channelSubjectID = channelSubjectID;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getWhatsOnCount()
    {
        return whatsOnCount;
    }

    public void setWhatsOnCount(String whatsOnCount)
    {
        this.whatsOnCount = whatsOnCount;
    }

    public String getHappyNextCount()
    {
        return happyNextCount;
    }

    public void setHappyNextCount(String happyNextCount)
    {
        this.happyNextCount = happyNextCount;
    }

    @Override
    public String toString()
    {
        return "QueryOTTLiveTVHomeDataRequest{" +
                "date='" + date + '\'' +
                ", count='" + count + '\'' +
                ", whatsOnCount='" + whatsOnCount + '\'' +
                ", happyNextCount='" + happyNextCount + '\'' +
                ", subjectCount='" + subjectCount + '\'' +
                ", channelSubjectID='" + channelSubjectID + '\'' +
                '}';
    }
}