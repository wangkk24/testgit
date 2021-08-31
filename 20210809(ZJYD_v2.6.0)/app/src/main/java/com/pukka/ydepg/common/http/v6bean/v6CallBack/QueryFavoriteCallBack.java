package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import com.pukka.ydepg.common.http.v6bean.v6node.Content;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */
public interface QueryFavoriteCallBack extends V6CallBack {

    void queryFavoriteSuccess(int total,List<Content> favorites);
    void queryFavoriteFail();
}
