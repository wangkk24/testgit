package com.pukka.ydepg.common.http.v6bean.v6node.ssp;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class DistributionChannel {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("app")
    private App app;

    @SerializedName("content")
    private Content content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }


    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append("\n\t\tid             = ").append(id)
                .append("\n\t\tname           = ").append(name)
                .append("\n\t\ttype           = ").append(type)
                .append("\n\t\tapp(CmsType)   = ").append(app == null? null : app.getSectcat())
                .append("\n\t\tcontent >>>      ").append(content)
                .toString();
    }
}