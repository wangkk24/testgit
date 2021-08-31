package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: BookmarkFilter.java
 * @author: yh
 * @date: 2017-04-21 16:35
 */

public class BookmarkFilter {


    /**
     * ratingID : 123
     */

    /**
     * 书签的内容观看级别。

     */
    @SerializedName("ratingID")
    private String ratingID;

    public String getRatingID() {
        return ratingID;
    }

    public void setRatingID(String ratingID) {
        this.ratingID = ratingID;
    }
}
