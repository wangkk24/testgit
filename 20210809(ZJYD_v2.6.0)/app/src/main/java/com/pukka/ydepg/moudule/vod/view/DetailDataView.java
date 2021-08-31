package com.pukka.ydepg.moudule.vod.view;

import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;

import java.util.List;

/**
 * Created by jiaxing on 2017/8/25.
 */
public interface DetailDataView extends LoadDataView {
    void showDetail(VODDetail vodDetail, String actionId, List<RecmContents> recmContentsList);

    void showCollection(boolean isCollection);

    void setNewScore(List<Float> newScore);

    void showContentNotExit();
}