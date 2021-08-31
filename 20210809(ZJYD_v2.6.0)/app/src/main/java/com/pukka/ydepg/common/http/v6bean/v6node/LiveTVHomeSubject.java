package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * the metadata of LiveTVHomeSubject
 */
public class LiveTVHomeSubject
{
    /**
     * field:id
     */
    @SerializedName("ID")
    private String id;
    /**
     * field:name
     */
    @SerializedName("name")
    private String name;

    /**
     * field:type
     */
    @SerializedName("type")
    private String type;

    /**
     * the range of field type
     */
    public interface Type
    {
        /**
         * video on demand
         */
        String VOD = "VOD";

        /**
         * video on demand of audio
         */
        String AUDIO_VOD = "AUDIO_VOD";

        /**
         * video on demand of video
         */
        String VIDEO_VOD = "VIDEO_VOD";

        /**
         * video on demand of credit
         */
        String CREDIT_VOD = "CREDIT_VOD";

        /**
         * channel
         */
        String CHANNEL = "CHANNEL";

        /**
         * channel of audio
         */
        String AUDIO_CHANNEL = "AUDIO_CHANNEL";

        /**
         * channel of video
         */
        String VIDEO_CHANNEL = "VIDEO_CHANNEL";

        /**
         * channel of web
         */
        String WEB_CHANNEL = "WEB_CHANNEL";
        /**
         * mix content
         */
        String MIX = "MIX";

        /**
         * vas:values added service
         */
        String VAS = "VAS";

        /**
         * program or playbill
         */
        String PROGRAM = "PROGRAM";

    }

    /**
     * field:recmActionID
     */
    @SerializedName("recmActionID")
    private String recmActionId;
    /**
     * field:channelPlaybilllist
     */
    @SerializedName("channelPlaybillList")
    private List<ChannelPlaybill> channelPlaybilllist;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getRecmActionId()
    {
        return recmActionId;
    }

    public void setRecmActionId(String recmActionId)
    {
        this.recmActionId = recmActionId;
    }

    public List<ChannelPlaybill> getChannelPlaybillList()
    {
        return channelPlaybilllist;
    }

    public void setChannelPlaybillList(List<ChannelPlaybill> channelPlaybilllist)
    {
        this.channelPlaybilllist = channelPlaybilllist;
    }

    @Override
    public String toString()
    {
        return "LiveTVHomeSubject{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", recmActionId='" + recmActionId + '\'' +
                ", channelPlaybilllist=" + channelPlaybilllist +
                '}';
    }
}