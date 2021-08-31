package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liudo on 2017/12/22.
 */

public class QuerySubjectDetailRequest {
    @SerializedName("subjectIDs")
    private List<String> subjectIds;


    public void setSubjectIds(List<String> subjectIds)
    {
        this.subjectIds = subjectIds;
    }


    public List<String> getSubjectIds()
    {
        return subjectIds;
    }

    @Override
    public String toString()
    {
        return "QueryVODSubjectListRequest{" +
                ", subjectIDs=" + subjectIds +
                '}';
    }

}
