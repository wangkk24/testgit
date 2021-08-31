package com.pukka.ydepg.launcher.bean.response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6response.BaseResponse;
import com.pukka.ydepg.launcher.bean.node.Topic;

import java.util.List;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.bean.response.TopicResponse.java
 * @date: 2018-03-11 09:38
 * @version: V1.0 描述当前版本功能
 */


public class TopicResponse extends BaseResponse {
    @SerializedName("nextIntervalPeriod")
    private String nextIntervalPeriod;
    @SerializedName("version")
    private String version;
    @SerializedName("topics")
    private List<Topic> topics;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getNextIntervalPeriod() {
        return nextIntervalPeriod;
    }

    public void setNextIntervalPeriod(String nextIntervalPeriod) {
        this.nextIntervalPeriod = nextIntervalPeriod;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
