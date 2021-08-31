package com.pukka.ydepg.launcher.bean.node;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.Feedback;

import java.io.Serializable;
import java.util.List;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.bean.node.Topic.java
 * @date: 2018-03-11 09:47
 * @version: V1.0 描述当前版本功能
 */


public class Topic implements Serializable{
    @SerializedName("id")
    private String id;
    @SerializedName("relationSubjectId")
    private String relationSubjectId;
    @SerializedName("topicStyleId")
    private String topicStyleId;
    @SerializedName("params")
    private List<NamedParameter> params;
    @SerializedName("customFields")
    private List<NamedParameter> customFields;

    //跑马灯---
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("notice")
    private String notice;
    @SerializedName("posterURL")
    private String posterUrl;
    @SerializedName("topicURL")
    private String topicURL;

    @SerializedName("feedback")
    protected Feedback feedback;

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
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

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTopicURL() {
        return topicURL;
    }

    public void setTopicURL(String topicURL) {
        this.topicURL = topicURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelationSubjectId() {
        return relationSubjectId;
    }

    public void setRelationSubjectId(String relationSubjectId) {
        this.relationSubjectId = relationSubjectId;
    }

    public String getTopicStyleId() {
        return topicStyleId;
    }

    public void setTopicStyleId(String topicStyleId) {
        this.topicStyleId = topicStyleId;
    }

    public List<NamedParameter> getParams() {
        return params;
    }

    public void setParams(List<NamedParameter> params) {
        this.params = params;
    }

    public List<NamedParameter> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<NamedParameter> customFields) {
        this.customFields = customFields;
    }
}
