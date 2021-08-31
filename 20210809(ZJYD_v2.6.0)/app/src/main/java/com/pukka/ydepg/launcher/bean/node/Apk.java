package com.pukka.ydepg.launcher.bean.node;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.Feedback;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Eason on 19-Jun-20.
 */

public class Apk implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("pkg")
    private String pkg;

    @SerializedName("cls")
    private String cls;

    @SerializedName("notice")
    private String notice;

    @SerializedName("posterURL")
    private String posterURL;

    @SerializedName("extras")
    private List<NamedParameter> extras;

    @SerializedName("feedback")
    protected Feedback feedback;

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

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

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public List<NamedParameter> getExtras() {
        return extras;
    }

    public void setExtras(List<NamedParameter> extras) {
        this.extras = extras;
    }
}
