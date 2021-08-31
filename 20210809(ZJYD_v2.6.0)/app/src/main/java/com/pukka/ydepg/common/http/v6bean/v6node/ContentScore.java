package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: ContentScore.java
 * @author: yh
 * @date: 2017-04-21 17:47
 */

public class ContentScore {


    /**
     * contentID :
     * contentType :
     * score : 11
     */

    /**
     *内容ID。

     */
    @SerializedName("contentID")
    private String contentID;

    /**
     *内容类型，取值包括：

     VOD：点播
     说明：
     1.平台目前仅支持VOD评分，此属性为了后续增加节目单或频道评分预留。

     */
    @SerializedName("contentType")
    private String contentType;

    /**
     *分值，取值范围：1-10。

     */
    @SerializedName("score")
    private String score;

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
