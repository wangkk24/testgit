package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.LiveTVHomeSubject;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * the response of QueryOTTLiveTVHomeDataResponse
 *
 */
public class QueryOTTLiveTVHomeDataResponse
{

    /**
     * field:introduce
     */
    @SerializedName("result")
    private Result result;
    /**
     * liveTVHomeSubjectList
     */
    @SerializedName("liveTVHomeSubjectList")
    private List<LiveTVHomeSubject> liveTVHomeSubjectList;

    public List<LiveTVHomeSubject> getLiveTVHomeSubjectList()
    {
        return liveTVHomeSubjectList;
    }

    public void setLiveTVHomeSubjectList(List<LiveTVHomeSubject> liveTVHomeSubjectList)
    {
        this.liveTVHomeSubjectList = liveTVHomeSubjectList;
    }

    @Override
    public String toString()
    {
        return "QueryOTTLiveTVHomeDataResponse{" +
                "liveTVHomeSubjectList=" + liveTVHomeSubjectList +
                '}';
    }
}