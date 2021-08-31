package com.pukka.ydepg.moudule.search.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchDataClassify<T> {

    @SerializedName("default")
    private List<T> common;

    @SerializedName("child")
    private List<T> child;

    public List<T> getCommon() {
        return common;
    }

    public List<T> getChild() {
        return child;
    }
}
