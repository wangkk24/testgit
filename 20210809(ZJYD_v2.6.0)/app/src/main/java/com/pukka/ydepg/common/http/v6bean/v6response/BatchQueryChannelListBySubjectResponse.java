package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.SubjectChannelList;

import java.util.List;

public class BatchQueryChannelListBySubjectResponse  extends BaseResponse{
    @SerializedName("subjectChannelList")
    List<SubjectChannelList> subjectChannelList;

    public List<SubjectChannelList> getSubjectChannelList() {
        return subjectChannelList;
    }

    public void setSubjectChannelList(List<SubjectChannelList> subjectChannelList) {
        this.subjectChannelList = subjectChannelList;
    }
}
