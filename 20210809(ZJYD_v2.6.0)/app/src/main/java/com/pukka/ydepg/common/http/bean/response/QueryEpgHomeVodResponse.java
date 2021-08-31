package com.pukka.ydepg.common.http.bean.response;

import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6response.BaseResponse;
import com.pukka.ydepg.launcher.bean.node.SubjectVodsList;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 */
public class QueryEpgHomeVodResponse extends BaseResponse
{
    private boolean isNeedRefresh = true;

    private String md5Value;

    @SerializedName("topRecmActionID")
    private  String topRecmActionID;

    @SerializedName("everyoneIsWatching")
    private List<VOD> everyoneIsWatching;

    @SerializedName("topPicksForYou")
    private List<VOD> topPicksForYou;

    @SerializedName("subjectVodLists")
    private List<SubjectVodsList> subjectVodLists;

    public String getTopRecmActionID() {
        return topRecmActionID;
    }

    public void setTopRecmActionID(String topRecmActionID) {
        this.topRecmActionID = topRecmActionID;
    }

    public List<VOD> getEveryoneIsWatching() {
        return everyoneIsWatching;
    }

    public void setEveryoneIsWatching(List<VOD> everyoneIsWatching) {
        this.everyoneIsWatching = everyoneIsWatching;
    }

    public List<VOD> getTopPicksForYou() {
        return topPicksForYou;
    }

    public void setTopPicksForYou(List<VOD> topPicksForYou) {
        this.topPicksForYou = topPicksForYou;
    }

    public List<SubjectVodsList> getSubjectVodLists() {
        return subjectVodLists;
    }

    public void setSubjectVodLists(List<SubjectVodsList> subjectVodLists) {
        this.subjectVodLists = subjectVodLists;
    }

    public boolean isNeedRefresh() {
        return isNeedRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
        isNeedRefresh = needRefresh;
    }

    public String getMd5Value() {
        return md5Value;
    }

    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value;
    }
}