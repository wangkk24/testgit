package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import com.pukka.ydepg.common.http.v6bean.v6node.Topic;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;

import java.util.List;

public interface VODDuplicateCallBack {

    void queryDuplicateSuccessByVODs(int total, List<VOD> listVod,String sceneId);

    void queryDuplicateSuccessByTopics(int total, List<Topic> vodDetails,String sceneId);

    void queryDuplicateFail();
}