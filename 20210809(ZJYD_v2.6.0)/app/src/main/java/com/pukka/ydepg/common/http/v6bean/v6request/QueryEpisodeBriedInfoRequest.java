package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

public class QueryEpisodeBriedInfoRequest {

    @SerializedName("VODID")
    private String VODID;

    /*
     *编号的类型。
     *取值包括：
     *0: 内容内键，取值由VSP生成。
     *1: 内容Code，取值由第三方系统生成。
     */
    @SerializedName("IDType")
    private int IDType;

    /*
     *一次查询的连续剧子集的总条数，传入-1时表示查询所有。
     */
    @SerializedName("count")
    private int count;


    /*
     *查询的起始位置。从0开始（即0表示第一个）。
     */
    @SerializedName("offset")
    private int offset;

    public String getVODID() {
        return VODID;
    }

    public void setVODID(String VODID) {
        this.VODID = VODID;
    }

    public int getIDType() {
        return IDType;
    }

    public void setIDType(int IDType) {
        this.IDType = IDType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
