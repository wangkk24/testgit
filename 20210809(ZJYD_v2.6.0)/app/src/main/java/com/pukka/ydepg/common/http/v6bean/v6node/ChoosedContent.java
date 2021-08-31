package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

public class ChoosedContent {

    /*
     *内容外部ID
     */
    @SerializedName("contentID")
    private String contentID;

    /*
     *内容添加时间
     *格式：YYYYMMDDHH24MISS
     */
    @SerializedName("createTime")
    private String createTime;

    /*
     *内容失效时间
     *格式：YYYYMMDDHH24MISS
     */
    @SerializedName("expTime")
    private String expTime;

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExpTime() {
        return expTime;
    }

    public void setExpTime(String expTime) {
        this.expTime = expTime;
    }
}
