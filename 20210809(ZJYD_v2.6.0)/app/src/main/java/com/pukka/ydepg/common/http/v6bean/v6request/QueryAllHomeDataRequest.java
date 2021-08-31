package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.HomeParam;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hasee on 2017/5/8.
 */

public class QueryAllHomeDataRequest {
    @SerializedName("request")
    private List<HomeParam> homeParamList;

    public List<HomeParam> getHomeParamList()
    {
        return homeParamList;
    }

    public void setHomeParamList(List<HomeParam> homeParamList)
    {
        this.homeParamList = homeParamList;
    }

    @Override
    public String toString()
    {
        return "QueryAllHomeDataRequest{" +
                "homeParamList=" + homeParamList +
                '}';
    }
}
