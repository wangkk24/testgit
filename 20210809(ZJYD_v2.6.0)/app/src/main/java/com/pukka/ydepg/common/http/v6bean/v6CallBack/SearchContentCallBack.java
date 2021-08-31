package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import com.pukka.ydepg.common.http.v6bean.v6node.CastDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public interface SearchContentCallBack extends V6CallBack {
    void searchContentSuccess(int total,List<Content> contents,String key);

    void searchContentFail(String key);

    void getCastDetailSuccess(List<CastDetail> mCastDetails);

    void  getCastDetailFail();
}
