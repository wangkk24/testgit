package com.pukka.ydepg.moudule.vod.view;

import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;

import java.util.List;

public interface NewDetailDataView extends LoadDataView {

    void showDetail(VODDetail vodDetail, String actionId, List<RecmContents> recmContentsList);

    void showCollection(boolean isCollection);

    void setNewScore(List<Float> newScore);

    void showContentNotExit();

    void startPlay();

}
