package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SuperScript implements Serializable {
    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private Integer type;

    @SerializedName("icon")
    private String icon;

    @SerializedName("detailIcon")
    private String detailIcon;

    @SerializedName("subIcon")
    private String subIcon;

    @SerializedName("subChildIcon")
    private String subChildIcon;

    @SerializedName("subPlayIcon")
    private String subPlayIcon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDetailIcon() {
        return detailIcon;
    }

    public void setDetailIcon(String detailIcon) {
        this.detailIcon = detailIcon;
    }

    public String getSubIcon() {
        return subIcon;
    }

    public void setSubIcon(String subIcon) {
        this.subIcon = subIcon;
    }

    public String getSubChildIcon() {
        return subChildIcon;
    }

    public void setSubChildIcon(String subChildIcon) {
        this.subChildIcon = subChildIcon;
    }

    public String getSubPlayIcon() {
        return subPlayIcon;
    }

    public void setSubPlayIcon(String subPlayIcon) {
        this.subPlayIcon = subPlayIcon;
    }
}
