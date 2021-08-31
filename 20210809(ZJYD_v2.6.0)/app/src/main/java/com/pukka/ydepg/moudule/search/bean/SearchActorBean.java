package com.pukka.ydepg.moudule.search.bean;

import com.google.gson.annotations.SerializedName;

public class SearchActorBean {
    @SerializedName("name")
    private String name;

    @SerializedName("url")
    private String url;

    @SerializedName("categoryID")
    private String categoryID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }
}
