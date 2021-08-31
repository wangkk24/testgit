package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PlaybillSeries.java
 * @author: yh
 * @date: 2017-04-21 16:47
 */

public class PlaybillSeries implements Serializable {


    /**
     * sitcomName :
     * sitcomNO : 12
     * seasonNO : 12
     * seriesID :
     * lifetimeID :
     * seasonID :
     */

    /**
     *节目子集名称，当节目是连续剧时才有效。

     */
    @SerializedName("sitcomName")
    private String sitcomName;

    /**
     *节目子集编号，当连续剧有父子集关系时才有效。

     */
    @SerializedName("sitcomNO")
    private String sitcomNO;

    /**
     *节目的季号，当节目是季播剧才有效。

     */
    @SerializedName("seasonNO")
    private String seasonNO;

    /**
     *连续剧的系列标识。

     */
    @SerializedName("seriesID")
    private String seriesID;

    /**
     *节目单全局ID，如果同一个内容在几个不同的频道播放，这个ID是相同的。

     */
    @SerializedName("lifetimeID")
    private String lifetimeID;

    /**
     *季播剧标识。

     */
    @SerializedName("seasonID")
    private String seasonID;

    public String getSitcomName() {
        return sitcomName;
    }

    public void setSitcomName(String sitcomName) {
        this.sitcomName = sitcomName;
    }

    public String getSitcomNO() {
        return sitcomNO;
    }

    public void setSitcomNO(String sitcomNO) {
        this.sitcomNO = sitcomNO;
    }

    public String getSeasonNO() {
        return seasonNO;
    }

    public void setSeasonNO(String seasonNO) {
        this.seasonNO = seasonNO;
    }

    public String getSeriesID() {
        return seriesID;
    }

    public void setSeriesID(String seriesID) {
        this.seriesID = seriesID;
    }

    public String getLifetimeID() {
        return lifetimeID;
    }

    public void setLifetimeID(String lifetimeID) {
        this.lifetimeID = lifetimeID;
    }

    public String getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(String seasonID) {
        this.seasonID = seasonID;
    }
}
