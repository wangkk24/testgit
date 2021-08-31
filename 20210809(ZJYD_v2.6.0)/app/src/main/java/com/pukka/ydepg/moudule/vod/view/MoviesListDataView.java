package com.pukka.ydepg.moudule.vod.view;

import com.pukka.ydepg.common.http.v6bean.v6node.Genre;
import com.pukka.ydepg.common.http.v6bean.v6node.ProduceZone;
import com.pukka.ydepg.common.http.v6bean.v6node.SortFilterBean;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.YearFilterBean;

import java.util.List;

/**
 * Created by ld on 2018/1/12.
 */

public interface MoviesListDataView extends LoadDataView{
    void showCatagorys(List<Subject> subjects);

    void showMoviesContent(List<VOD> VODList,String subjectId);

    void showFilterContent(List<SortFilterBean> sortFilterBeanList, List<Genre> genres, List<ProduceZone> produceZones, List<YearFilterBean> yearList);

    void showTogalPage(int totalPage);

    void hideCategory();

    void showCategoryName(Subject subject);

    void hideFilter();

    void resetLoadNum();

    int getLoadNum();

    void setLoadNum();

    void showPageNum();

    void setSearchFocus();

    void resetContentStatus();

    void hideLine();

    void resetFilterField();

    void showLoading();

    void hideLoading();
}