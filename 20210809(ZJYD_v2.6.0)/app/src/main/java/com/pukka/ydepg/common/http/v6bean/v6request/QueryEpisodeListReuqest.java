package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

public class QueryEpisodeListReuqest {

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
     *连续剧父集是否已订购。
     *取值包括：
     *0: 未订购。
     *1: 已订购。
     *说明：
     *响应中Episode.VOD需要返回isSubscribed时有效。
     *当子集需要返回isSubscribed时，若允许子集使用父集的鉴权结果，isSubscribed直接返回此参数值；否则需针对每个子集进行鉴权得到
     *isSubscribed值。
     */
    @SerializedName("isSubscriberedOnSeries")
    private int isSubscriberedOnSeries;

    /*
     *是否根据用户属性和终端能力进行过滤。
     *取值包括：
     *0: 过滤
     *1: 不过滤
     */
    @SerializedName("isFilter")
    private int isFilter;

    /*
     *一次查询的连续剧子集的总条数，最多查询50条。
     */
    @SerializedName("count")
    private int count;

    /*
     *查询的起始位置。从0开始（即0表示第一个）。
     */
    @SerializedName("offset")
    private int offset;

    /*
     *子集排序方式。
     *取值包括：
     *SITCOMNO:ASC: 按集号升序排序
     *SITCOMNO:DESC: 按集号降序排序
     *说明：
     *如果sortType不传入，平台默认为SITCOMNO:ASC。
     */
    @SerializedName("sortType")
    private String sortType;
    public static final String SITCOMNO_ASC = "SITCOMNO:ASC";
    public static final String SITCOMNO_DESC = "SITCOMNO:DESC";

    /*
     *是否返回预告片。
     *取值包括：
     *0: 返回正片
     *1: 返回预告片
     *2: 返回正片+预告片，且正片排列于预告片前
     *3: 返回正片+预告片，且预告片排列于正片前
     */
    @SerializedName("isIncludeClipVOD")
    private int isIncludeClipVOD;

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

    public int getIsSubscriberedOnSeries() {
        return isSubscriberedOnSeries;
    }

    public void setIsSubscriberedOnSeries(int isSubscriberedOnSeries) {
        this.isSubscriberedOnSeries = isSubscriberedOnSeries;
    }

    public int getIsFilter() {
        return isFilter;
    }

    public void setIsFilter(int isFilter) {
        this.isFilter = isFilter;
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

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public int getIsIncludeClipVOD() {
        return isIncludeClipVOD;
    }

    public void setIsIncludeClipVOD(int isIncludeClipVOD) {
        this.isIncludeClipVOD = isIncludeClipVOD;
    }
}
