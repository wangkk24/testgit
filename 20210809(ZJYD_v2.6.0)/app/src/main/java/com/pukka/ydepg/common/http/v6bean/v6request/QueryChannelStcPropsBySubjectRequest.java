package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

public class QueryChannelStcPropsBySubjectRequest {
    /**
     *栏目ID，查询此栏目下的子栏目列表。如果为空，标示查询 “-1”根栏目下的栏目列表。
     */
    @SerializedName("subjectID")
    private String subjectID;

    /**
     * 频道排序方式。
     * 取值包括：
     * CHANNELNO:ASC: 按频道号升序排序
     * ORDERINDEX:DESC: 按栏目下内容自定义序号降序排序，仅对subjectid表示的栏目是叶子栏目时才有效。注意：该顺序与VMPortal上展示的混排频道列表顺序一致。
     * ORDERINDEX:ASC: 按栏目下内容自定义序号升序排序，仅对subjectid表示的栏目是叶子栏目时才有效
     * 说明：
     * 1. sortType未携带，sortType默认取CHANNELNO:ASC。
     * 2. VSP不支持对父栏目按照内容自定义的顺序排序，自定义顺序只针对叶子栏目。
     * */
    @SerializedName("sortType")
    private String sortType;

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    /**
     *
     一次查询的总条数，不能设置为-1，调用者一定要指定获取数据的总条数，最大条数默认不超过50，超过最大条数返回错误。
     */
    @SerializedName("count")
    private String count;

    /**
     * 查询的起始位置。从0开始（即0表示第一个）。
     */
    @SerializedName("offset")
    private String offset;

}
