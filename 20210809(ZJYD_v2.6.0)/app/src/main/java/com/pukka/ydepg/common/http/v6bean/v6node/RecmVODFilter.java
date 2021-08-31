package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hasee on 2017/5/23.
 */

public class RecmVODFilter {
    /**
     * 流派ID。
     */
    @SerializedName("genreIDs")
    private List<String> genreIDs;
    /**
     *VOD支持的语种，为语言的ISO 639-1双字节缩写，比如en、zh。
     */
    @SerializedName("languages")
    private List<String> languages;
    /**
     * 返回该内容级别以下的推荐内容。
     */
    @SerializedName("ratingID")
    private String ratingID;

    public List<String> getGenreIDs() {
        return genreIDs;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public String getRatingID() {
        return ratingID;
    }

    public void setGenreIDs(List<String> genreIDs) {
        this.genreIDs = genreIDs;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public void setRatingID(String ratingID) {
        this.ratingID = ratingID;
    }
}
