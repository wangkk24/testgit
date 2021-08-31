package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import com.pukka.ydepg.common.http.v6bean.v6node.Genre;
import com.pukka.ydepg.common.http.v6bean.v6node.ProduceZone;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.SubjectVODList;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: VODListCallBack.java
 * @author: yh
 * @date: 2017-04-26 10:00
 */

public interface VODListCallBack {

    void queryVODListBySubjectSuccess(int total, List<VOD> vodList,String subjectId);

    void queryVODListBySubjectFailed();

    void queryVODSubjectListSuccess(int total, List<Subject> subjects);

    void queryVODSubjectListFailed();

    void querySubjectVODBySubjectIDSuccess(int total, List<SubjectVODList> subjectVODLists);

    void querySubjectVODBySubjectIDFailed();

    void getContentConfigSuccess(List<ProduceZone> produceZoneList, List<Genre> genreList);

    void getContentConfigFailed();

    void querySubjectDetailSuccess(int total,List<Subject> subjects);

    void queryPSBRecommendSuccess(int total, List<VOD> vodDetails);

    void queryPSBRecommendFail();

    void onError();
}
