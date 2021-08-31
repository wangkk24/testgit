package com.pukka.ydepg.moudule.vod.playerController;

import android.view.View;

import com.pukka.ydepg.common.http.v6bean.v6node.Episode;

import java.util.List;

public interface ControllerEpisodeAdapter {

    void    setDataEpisodesSource(List<String> episodes);
    void    setDataSource(List<Episode> episodes);
    void    setSelectEpisode(Episode episode);
    void    setOnKeyListener(View.OnKeyListener onKeyListener);
}
