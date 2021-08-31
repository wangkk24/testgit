package com.pukka.ydepg.moudule.browse;

import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODListCallBack;
import com.pukka.ydepg.common.http.v6bean.v6node.Genre;
import com.pukka.ydepg.common.http.v6bean.v6node.ProduceZone;
import com.pukka.ydepg.common.http.v6bean.v6node.SubjectVODList;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;

import java.util.List;

/**
 * Created by YuanChao on 2017/4/26.
 */

public abstract class VodSubjectCallBack implements VODListCallBack {
    @Override
    public void queryVODListBySubjectSuccess(int total, List<VOD> vodList,String subjectId) { }

    @Override
    public void queryVODListBySubjectFailed() { }

    @Override
    public void querySubjectVODBySubjectIDSuccess(int total, List<SubjectVODList> subjectVODLists) { }

    @Override
    public void querySubjectVODBySubjectIDFailed() { }

    @Override
    public void getContentConfigSuccess(List<ProduceZone> produceZoneList, List<Genre> genreList) { }

    @Override
    public void getContentConfigFailed() { }
}
