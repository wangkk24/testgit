package com.pukka.ydepg.common.http.bean.request;

import com.pukka.ydepg.common.http.bean.node.SubjectIDList;
import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.launcher.bean.request.BaseRequest;

import java.util.List;

/**
 * The request body of QueryVODSubjectListRequest
 */
public class QueryEpgHomeVodRequest extends BaseRequest
{
    private String navId;
    @SerializedName("everyoneIsWatchingCount")
    private int everyoneIsWatchingCount;

    @SerializedName("topPicksForYouCount")
    private int topPicksForYouCount;

    @SerializedName("subjectIDList")
    private List<SubjectIDList> subjectIDList;

    @SerializedName("VODSortType")
    private String VODSortType;

    private boolean isQueryFromCache;

    public boolean isQueryFromCache() {
        return isQueryFromCache;
    }

    public void setQueryFromCache(boolean queryFromCache) {
        isQueryFromCache = queryFromCache;
    }

    public String getNavId() {
        return navId;
    }

    public void setNavId(String navId) {
        this.navId = navId;
    }

    public int getEveryoneIsWatchingCount() {
        return everyoneIsWatchingCount;
    }

    public void setEveryoneIsWatchingCount(int everyoneIsWatchingCount) {
        this.everyoneIsWatchingCount = everyoneIsWatchingCount;
    }

    public int getTopPicksForYouCount() {
        return topPicksForYouCount;
    }

    public void setTopPicksForYouCount(int topPicksForYouCount) {
        this.topPicksForYouCount = topPicksForYouCount;
    }

    public List<SubjectIDList> getSubjectIDList() {
        return subjectIDList;
    }

    public void setSubjectIDList(List<SubjectIDList> subjectIDList) {
        this.subjectIDList = subjectIDList;
    }

    public String getVODSortType() {
        return VODSortType;
    }

    public void setVODSortType(String VODSortType) {
        this.VODSortType = VODSortType;
    }
}