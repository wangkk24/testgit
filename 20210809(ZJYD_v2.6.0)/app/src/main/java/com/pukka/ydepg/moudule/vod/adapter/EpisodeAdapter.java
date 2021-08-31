package com.pukka.ydepg.moudule.vod.adapter;

import com.pukka.ydepg.common.http.v6bean.v6node.Episode;

import java.util.List;

/**
 * Created by liudo on 2018/3/12.
 */

public interface EpisodeAdapter  {

    void  setDataEpisodesSource(List<String> episodes);
    void  setDataSource(List<Episode> episodes);
     void setSelectEpisode(Episode episode);
}
