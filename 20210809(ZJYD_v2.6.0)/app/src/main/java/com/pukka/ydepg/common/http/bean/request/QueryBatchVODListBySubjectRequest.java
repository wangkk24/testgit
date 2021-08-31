package com.pukka.ydepg.common.http.bean.request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.bean.node.SubjectIDList;
import com.pukka.ydepg.launcher.bean.request.BaseRequest;

import java.util.List;

/**
 * The request body of QueryVODSubjectListRequest
 */
public class QueryBatchVODListBySubjectRequest extends BaseRequest
{
    @SerializedName("conditions")
    private List<QueryVODListBySubjectConditionRequest> conditions;

    public List<QueryVODListBySubjectConditionRequest> getConditions() {
        return conditions;
    }

    public void setConditions(List<QueryVODListBySubjectConditionRequest> conditions) {
        this.conditions = conditions;
    }
}