package com.pukka.ydepg.launcher.http.hecaiyun.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BoothCarouselCont {

    @SerializedName("cloudID")
    private String cloudID;

    @SerializedName("cloudContent")
    private CloudContent cloudContent;

    public String getCloudID() {
        return cloudID;
    }

    public void setCloudID(String cloudID) {
        this.cloudID = cloudID;
    }

    public CloudContent getCloudContent() {
        return cloudContent;
    }

    public void setCloudContent(CloudContent cloudContent) {
        this.cloudContent = cloudContent;
    }
}
