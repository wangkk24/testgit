package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Topic {

    /**
     * id : AAAAAA
     * relationSubjectId : YYYYY
     * topicStyleId : XXXXX
     * customFields : [{"values":["0"],"key":"topicTitle"},{"values":["0"],"key":"topicIntro"}]
     */

    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("name")
    private String name;

    @SerializedName("relationSubjectId")
    private String relationSubjectId;

    @SerializedName("topicStyleId")
    private String topicStyleId;

    @SerializedName("posterURL")
    private String posterURL;

    @SerializedName("topicURL")
    private String topicURL;

    @SerializedName("customFields")
    protected List<NamedParameter> customFields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public String getTopicURL() {
        return topicURL;
    }

    public void setTopicURL(String topicURL) {
        this.topicURL = topicURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<NamedParameter> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<NamedParameter> customFields) {
        this.customFields = customFields;
    }
}
