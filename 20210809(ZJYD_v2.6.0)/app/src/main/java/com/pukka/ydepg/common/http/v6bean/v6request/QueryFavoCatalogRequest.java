package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

public class QueryFavoCatalogRequest {
    /**
     * 一次查询的总条数，最大条数默认不超过50，最大条数可配置，超过最大条数返回错误
     */
    @SerializedName("count")
    private String count;

    /**
     * 查询的起始位置，从0开始（即0表示第一个
     */
    @SerializedName("offset")
    private String offset;

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
}
