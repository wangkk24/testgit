package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * The response body of QueryVODListBySubjectResponse
 */
public class QuerySubjectDetailResponse extends BaseResponse{
    private static final String TAG = QuerySubjectDetailResponse.class.getSimpleName();

    @SerializedName("subjects")
    private List<Subject> subjectList;

    public List<Subject> getSubjectList()
    {
        if (null == subjectList || subjectList.isEmpty())
        {
            return new ArrayList<>();
        }
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList)
    {
        this.subjectList = subjectList;
    }

    @Override
    public String toString()
    {
        return "QueryVODListBySubjectResponse{" +
                ", subjectList=" + subjectList +
                '}';
    }
}