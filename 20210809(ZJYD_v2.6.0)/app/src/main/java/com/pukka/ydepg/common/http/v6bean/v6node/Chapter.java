package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: Chapter.java
 * @author: yh
 * @date: 2017-04-21 17:36
 */

public class Chapter implements Serializable {


    /**
     * ID : 123
     * title : VOD
     * offTime : 123
     * posters : [""]
     */

    /**
     *章节编号。

     */
    @SerializedName("ID")
    private String ID;

    /**
     *章节标题。

     */
    @SerializedName("title")
    private String title;

    /**
     *章节相对于VOD节目开始时间的秒数。

     */
    @SerializedName("offTime")
    private String offTime;

    /**
     *章节海报的绝对路径，为HTTP URL，例如，http://ip:port/VSP/jsp/image/12.jpg。

     */
    @SerializedName("posters")
    private List<String> posters;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOffTime() {
        return offTime;
    }

    public void setOffTime(String offTime) {
        this.offTime = offTime;
    }

    public List<String> getPosters() {
        return posters;
    }

    public void setPosters(List<String> posters) {
        this.posters = posters;
    }
}
