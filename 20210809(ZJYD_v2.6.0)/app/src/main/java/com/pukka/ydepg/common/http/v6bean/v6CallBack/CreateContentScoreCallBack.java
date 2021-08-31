package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public interface CreateContentScoreCallBack extends V6CallBack {
    void createContentScoreSuccess(List<Float> newScores);

    void createContentScoreFail();
}
