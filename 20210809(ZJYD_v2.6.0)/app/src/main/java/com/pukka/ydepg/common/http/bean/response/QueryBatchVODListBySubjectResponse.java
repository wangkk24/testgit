package com.pukka.ydepg.common.http.bean.response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6response.BaseResponse;
import com.pukka.ydepg.launcher.bean.node.SubjectVODLists;
import com.pukka.ydepg.launcher.bean.node.SubjectVodsList;

import java.util.List;

/**
 */
public class QueryBatchVODListBySubjectResponse extends BaseResponse {

    @SerializedName("subjectVODLists")
    private List<SubjectVODLists> subjectVODLists;

    public List<SubjectVODLists> getSubjectVODLists() {
        return subjectVODLists;
    }

    public void setSubjectVODLists(List<SubjectVODLists> subjectVODLists) {
        this.subjectVODLists = subjectVODLists;
    }
}