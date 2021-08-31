package com.pukka.ydepg.common.http.v6bean.v6node.ssp;

import com.google.gson.annotations.SerializedName;

public class App {

    public App(String sectcat) {
        this.sectcat = sectcat;
    }

    //Vod对象的cmsType字段,影片的一级分类
    @SerializedName("sectcat")
    private String sectcat;

    public String getSectcat() {
        return sectcat;
    }

    public void setSectcat(String sectcat) {
        this.sectcat = sectcat;
    }
}
