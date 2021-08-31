package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubjectChannelList extends Metadata{
    @SerializedName("subject")
    private Subject subject;
    @SerializedName("channelPlaybills")
    private List<ChannelPlaybill> channelPlaybills;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<ChannelPlaybill> getChannelPlaybills() {
        return channelPlaybills;
    }

    public void setChannelPlaybills(List<ChannelPlaybill> channelPlaybills) {
        this.channelPlaybills = channelPlaybills;
    }
}
