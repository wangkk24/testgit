package com.pukka.ydepg.common.http.v6bean.v6node.ssp;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Content {

    //外部code
    @SerializedName("id")
    private String id;

    //内容名字
    @SerializedName("title")
    private String title;

    //流派
    @SerializedName("genre")
    private String genre;

    //内容时长,单位为秒
    @SerializedName("len")
    private Integer len;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getLen() {
        return len;
    }

    public void setLen(Integer len) {
        this.len = len;
    }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append("\n\t\t\tid            = ").append(id)
                .append("\n\t\t\ttitle         = ").append(title)
                .append("\n\t\t\tlen(unit:s)   = ").append(len)
                .append("\n\t\t\tgenre         = ").append(genre)
                .toString();
    }
}
