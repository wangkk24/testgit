package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;

import java.util.List;

/**
 * Created by Administrator on 2017/6/8.
 */

public interface VODDetailCallBack {

    void getVODDetailSuccess(VODDetail vodDetail, String recmActionID, List<RecmContents> recmContents);

    void getVODDetailFailed();

    void onError();
}
