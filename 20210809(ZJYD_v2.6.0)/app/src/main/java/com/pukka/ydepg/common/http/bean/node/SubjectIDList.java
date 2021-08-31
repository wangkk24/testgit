package com.pukka.ydepg.common.http.bean.node;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 2017/8/24.
 */

public class SubjectIDList
{
    @SerializedName("subjectID")
    private String subjectID;

    @SerializedName("count")
    private int count;

    public SubjectIDList(String subjectID, int count)
    {
        this.subjectID = subjectID;
        this.count = count;
    }
    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "SubjectIDList{" +
                "subjectID='" + subjectID + '\'' +
                ", count=" + count +
                '}';
    }
}
