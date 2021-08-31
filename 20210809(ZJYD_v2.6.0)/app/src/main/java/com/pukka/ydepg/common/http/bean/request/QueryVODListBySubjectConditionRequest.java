package com.pukka.ydepg.common.http.bean.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eason on 13-Nov-19.
 */

public class QueryVODListBySubjectConditionRequest {


    @SerializedName("subjectID")
    private String subjectID;

    @SerializedName("count")
    private Integer count;

    @SerializedName("offset")
    private Integer offset;

    @SerializedName("sortType")
    private String sortType;


    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}
