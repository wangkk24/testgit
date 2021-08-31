package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BatchQueryChannelListBySubjectRequest {
    @SerializedName("conditions")
    private List<QueryChannelListBySubjectRequest> conditions;

    public List<QueryChannelListBySubjectRequest> getConditions() {
        return conditions;
    }

    public void setConditions(List<QueryChannelListBySubjectRequest> conditions) {
        this.conditions = conditions;
    }
}
