package com.pukka.ydepg.common.http.bean.node;



import com.pukka.ydepg.common.http.v6bean.v6node.Episode;

import java.util.List;

/**
 * Created by liudong on 2018/01/22 0011.
 */

public class EpisodesBean
{
    private String vodId;

    private List<Episode> episodeList;

    public EpisodesBean(String vodId, List<Episode> episodeList) {
        this.vodId = vodId;
        this.episodeList = episodeList;
    }

    public String getVodId() {
        return vodId;
    }

    public void setVodId(String vodId) {
        this.vodId = vodId;
    }

    public List<Episode> getEpisodeList() {
        return episodeList;
    }

    public void setEpisodeList(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }
}
