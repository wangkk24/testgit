package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */

public class StartPictureConfig {

    /**
     * Verimatrix的加密信息
     */
    @SerializedName("name")
    private String name;

    /**
     * PlayReady的加密信息
     */
    @SerializedName("picture")
    private List<StartPicture> picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StartPicture> getPicture() {
        picture.remove(null);
        return picture;
    }

    public void setPicture(List<StartPicture> picture) {
        this.picture = picture;
    }
}
