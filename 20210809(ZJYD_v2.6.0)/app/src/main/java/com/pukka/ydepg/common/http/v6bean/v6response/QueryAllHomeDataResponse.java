package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.HomeData;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hasee on 2017/5/8.
 */

public class QueryAllHomeDataResponse extends BaseResponse{

    @SerializedName("response")
    private List<HomeData> homeDataList;

    public List<HomeData> getHomeDataList()
    {
        return homeDataList;
    }

    public void setHomeDataList(List<HomeData> homeDataList)
    {
        this.homeDataList = homeDataList;
    }

    @Override
    public String toString()
    {
        return "QueryAllHomeDataResponse{" +
                "homeDataList=" + homeDataList +
                '}';
    }
}
