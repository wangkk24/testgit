package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wgy on 2017/5/19.
 */

public class QuerySubInfoList {

    /**
     * userID : zh5063462349
     * queryType : 1
     */

    /**
     * 订购用户ID
     */
    @SerializedName("userID")
    private String userID;

    /**
     * 查询订购关系类型
     * 1：按次订购关系
     * 2：包月订购关系
     */
    @SerializedName("queryType")
    private String queryType;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
